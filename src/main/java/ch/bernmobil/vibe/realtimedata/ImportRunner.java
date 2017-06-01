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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
@EnableScheduling
public class ImportRunner {
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

    @Scheduled(fixedRate = 30 * 1000)
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
        logger.info("Fetching from Realtime feed");
        List<FeedEntity> feedEntities = realtimeUpdateRepository.findAll();
        logger.debug(String.format("Fetching successful. Found %d entries", feedEntities.size()));
        logger.info("Convert GTFS updates");
        List<ScheduleUpdateInformation> updateInformationList = extractScheduleUpdateInformation(feedEntities);
        scheduleRepository.addScheduleId(updateInformationList);
        List<ScheduleUpdate> scheduleUpdates = convert(updateInformationList);
        logger.info("Save updates");
        scheduleUpdateRepository.save(scheduleUpdates);
        logger.info("Finish Realtime Update");
    }

    private List<ScheduleUpdateInformation> extractScheduleUpdateInformation(List<FeedEntity> feedEntities) {
        List<ScheduleUpdateInformation> validStopTimeUpdates = new ArrayList<>();
        int numTotalUpdates = 0;
        for (FeedEntity feedEntity : feedEntities) {
            TripUpdate tripUpdate = feedEntity.getTripUpdate();
            String gtfsTripId = tripUpdate.getTrip().getTripId();
            numTotalUpdates += tripUpdate.getStopTimeUpdateCount();

            List<ScheduleUpdateInformation> convertedUpdates =
                    tripUpdate.getStopTimeUpdateList()
                    .parallelStream()
                    .map(stopTimeUpdate -> convertToScheduleUpdateInformation(stopTimeUpdate, gtfsTripId))
                    .filter(Objects::nonNull)
                    .collect(toList());
            validStopTimeUpdates.addAll(convertedUpdates);

        }
        logger.info(String.format("Update Statistic: %d of %d were valid.", validStopTimeUpdates.size(), numTotalUpdates));
        return validStopTimeUpdates;
    }

    private ScheduleUpdateInformation convertToScheduleUpdateInformation(StopTimeUpdate stopTimeUpdate, String gtfsTripId) {
        String gtfsStopId = stopTimeUpdate.getStopId();
        Optional<JourneyMapping> journeyMapping = journeyMapperRepository.findByGtfsTripId(gtfsTripId);
        Optional<StopMapping> stopMapping = stopMapperRepository.findByGtfsId(gtfsStopId);
        if(journeyMapping.isPresent() && stopMapping.isPresent()) {
            return new ScheduleUpdateInformation(stopTimeUpdate, journeyMapping.get().getId(), stopMapping.get().getId());
        }
        logger.warn(String.format("No matching entry found for TripId: '%s' and StopId: '%s'", gtfsTripId, gtfsStopId));
        return null;
    }

    private List<ScheduleUpdate> convert(List<ScheduleUpdateInformation> scheduleUpdateInformations) {
        List<ScheduleUpdate> scheduleUpdates = new ArrayList<>();
        for(ScheduleUpdateInformation info : scheduleUpdateInformations) {
            scheduleUpdates.add(ScheduleUpdate.convert(info));
        }
        return scheduleUpdates;
    }
}
