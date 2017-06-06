package ch.bernmobil.vibe.realtimedata;

import ch.bernmobil.vibe.shared.UpdateHistoryEntry;
import ch.bernmobil.vibe.realtimedata.repository.JourneyMapperRepository;
import ch.bernmobil.vibe.realtimedata.repository.RealtimeUpdateRepository;
import ch.bernmobil.vibe.realtimedata.repository.ScheduleRepository;
import ch.bernmobil.vibe.realtimedata.repository.ScheduleUpdateRepository;
import ch.bernmobil.vibe.realtimedata.repository.StopMapperRepository;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;
import ch.bernmobil.vibe.shared.UpdateHistoryRepository;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.TripUpdate;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
@EnableScheduling
public class ImportRunner {
    @Value("${bernmobil.locale.timezone}")
    private String timezone;
    private final Logger logger = Logger.getLogger(ImportRunner.class);

    private final JourneyMapperRepository journeyMapperRepository;
    private final StopMapperRepository stopMapperRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleUpdateRepository scheduleUpdateRepository;
    private final RealtimeUpdateRepository realtimeUpdateRepository;
    private final UpdateHistoryRepository updateHistoryRepository;

    @Autowired
    public ImportRunner(
        JourneyMapperRepository journeyMapperRepository,
        StopMapperRepository stopMapperRepository,
        ScheduleRepository scheduleRepository,
        ScheduleUpdateRepository scheduleUpdateRepository,
        RealtimeUpdateRepository realtimeUpdateRepository,
        UpdateHistoryRepository updateHistoryRepository) {
        this.journeyMapperRepository = journeyMapperRepository;
        this.stopMapperRepository = stopMapperRepository;
        this.scheduleRepository = scheduleRepository;
        this.scheduleUpdateRepository = scheduleUpdateRepository;
        this.realtimeUpdateRepository = realtimeUpdateRepository;
        this.updateHistoryRepository = updateHistoryRepository;
    }

    /**
     * Scheduled Task which imports all Realtime-Updates.
     * Processing-Steps:
     * 1. Load all necessary information's (schedules, stopMappings, journeyMappings)
     * 2. Delete old Updates
     * 3. Load FeedEntities containing the Update Informations
     * 4. Extract updateInformations from the FeedEntities
     * 5. Populate the updateInformations with the not in the Feed containing scheduleId
     * 6. Convert updateInformations to concrete ScheduleUpdate Entity
     * 7. Save
     * @throws Exception
     */
    @Scheduled(fixedDelay = 30 * 1000)
    public void run() throws Exception {
        UpdateHistoryEntry lastSuccessUpdate = updateHistoryRepository.findLastSuccessUpdate();
        if(lastSuccessUpdate == null) {
            logger.warn("No successful update entry found - Realtime Update aborted");
            return;
        }
        logger.info(String.format("Start Realtime Update - load static data with timestamp: %s", lastSuccessUpdate.getTime()));
        scheduleRepository.load(lastSuccessUpdate.getTime());
        stopMapperRepository.load(lastSuccessUpdate.getTime());
        journeyMapperRepository.load(lastSuccessUpdate.getTime());
        logger.info("Delete old schedule updates");
        scheduleUpdateRepository.deleteAll();
        logger.info("Fetching from Realtime feed");
        List<FeedEntity> feedEntities = realtimeUpdateRepository.findAll();
        logger.debug(String.format("Fetching successful. Found %d entries", feedEntities.size()));
        logger.info("Convert GTFS updates");
        List<ScheduleUpdateInformation> updateInformationList = extractScheduleUpdateInformation(feedEntities);
        scheduleRepository.addScheduleId(updateInformationList);
        Collection<ScheduleUpdate> scheduleUpdates = convert(updateInformationList);
        logger.info("Save updates");
        scheduleUpdateRepository.save(scheduleUpdates);
        logger.info("Finish Realtime Update");
    }

    /**
     * Extract the Information needed for the ScheduleUpdate from the protobuf-informations.
     * The following Informations are extracted from the feedEntities:
     * - tripId
     * - stopId
     * - actualArrivalTime
     * - actualDepartureTime
     * <p>Note: If information's can't be extracted, the stopTimeUpdate will be ignored.</p>
     * @param feedEntities Realtime-Updates, parsed by com.google.transit library
     * @return List of ScheduleUpdateInformation's containing the extracted Informations from the feedEntities
     */
    List<ScheduleUpdateInformation> extractScheduleUpdateInformation(List<FeedEntity> feedEntities) {
        List<ScheduleUpdateInformation> validStopTimeUpdates = new ArrayList<>();
        int numTotalUpdates = 0;
        for (FeedEntity feedEntity : feedEntities) {
            TripUpdate tripUpdate = feedEntity.getTripUpdate();
            String gtfsTripId = tripUpdate.getTrip().getTripId();
            numTotalUpdates += tripUpdate.getStopTimeUpdateCount();

            List<ScheduleUpdateInformation> convertedUpdates =
                    tripUpdate.getStopTimeUpdateList()
                    .stream()//TODO: ParallelStream?
                    .map(stopTimeUpdate -> convertToScheduleUpdateInformation(stopTimeUpdate, gtfsTripId))
                    .filter(Objects::nonNull)
                    .collect(toList());
            validStopTimeUpdates.addAll(convertedUpdates);

        }
        logger.info(String.format("Update Statistic: %d of %d were valid.", validStopTimeUpdates.size(), numTotalUpdates));
        return validStopTimeUpdates;
    }

    /**
     * Creates the ScheduleUpdateInformation Object from the stopTimeUpdate.
     * <p>Note: This is a help function and is used from the extractScheduleUpdateInformation method only</p>
     * @param stopTimeUpdate containing the GTFS-Realtime information's
     * @param gtfsTripId corresponds to the tripId of the first parameter "stopTimeUpdate"
     * @return ScheduleUpdateInformation object containing the informations of the stopTimeUpdate
     */
    ScheduleUpdateInformation convertToScheduleUpdateInformation(StopTimeUpdate stopTimeUpdate, String gtfsTripId) {
        String gtfsStopId = stopTimeUpdate.getStopId();
        Optional<JourneyMapping> journeyMapping = journeyMapperRepository.findByGtfsTripId(gtfsTripId);
        Optional<StopMapping> stopMapping = stopMapperRepository.findByGtfsId(gtfsStopId);
        if(journeyMapping.isPresent() && stopMapping.isPresent()) {
            Time actualArrival = parseUpdateTime(stopTimeUpdate.getArrival().getTime(), timezone);
            Time actualDeparture = parseUpdateTime(stopTimeUpdate.getDeparture().getTime(), timezone);
            return new ScheduleUpdateInformation(actualArrival, actualDeparture,
                journeyMapping.get().getId(), stopMapping.get().getId());
        }
        logger.warn(String.format("No matching entry found for TripId: '%s' and StopId: '%s'", gtfsTripId, gtfsStopId));
        return null;
    }

    /**
     * Converts scheduleUpdateInformation objects into valid ScheduleUpdate entities.
     * <p>Note: There can be only one single ScheduleUpdate by Schedule. If more than one exists, the last processed will be kept.</p>
     * @param scheduleUpdateInformations A List of ScheduleUpdatesInformations to be converted to ScheduleUpdate
     * @return ScheduleUpdates ready for saving to the Database
     */
    Collection<ScheduleUpdate> convert(List<ScheduleUpdateInformation> scheduleUpdateInformations) {
        Map<UUID, ScheduleUpdate> scheduleUpdates = new HashMap<>();
        for(ScheduleUpdateInformation info : scheduleUpdateInformations) {
            if(scheduleUpdates.containsKey(info.getScheduleId())) {
                logger.warn(String.format("Schedule update with schedule-id %s already exists. It will overwrite any existing updates", info.getScheduleId()));
            }
            scheduleUpdates.put(info.getScheduleId(), ScheduleUpdate.convert(info));
        }
        return scheduleUpdates.values();
    }

    /**
     * Parses a timestamp (UTC-Time) to a sql.Time Object
     * @param timestamp to convert
     * @param timezone to use while conversion
     * @return sql.Time object in the passed timezone
     */
    static Time parseUpdateTime(Long timestamp, String timezone) {
        return timestamp == 0 ? null : Time.valueOf(
            LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of(timezone)).toLocalTime());
    }
}
