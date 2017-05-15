package ch.bernmobil.vibe.realtimedata;

import ch.bernmobil.vibe.realtimedata.entity.UpdateHistory;
import ch.bernmobil.vibe.realtimedata.repository.JourneyMapperRepository;
import ch.bernmobil.vibe.realtimedata.repository.RealtimeUpdateRepository;
import ch.bernmobil.vibe.realtimedata.repository.ScheduleRepository;
import ch.bernmobil.vibe.realtimedata.repository.ScheduleUpdateRepository;
import ch.bernmobil.vibe.realtimedata.repository.StopMapperRepository;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;
import ch.bernmobil.vibe.realtimedata.repository.UpdateHistoryRepository;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.TripUpdate;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.UUID;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class ImportRunner {
    private final JourneyMapperRepository journeyMapperRepository;
    private final StopMapperRepository stopMapperRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleUpdateRepository scheduleUpdateRepository;
    private final RealtimeUpdateRepository realtimeUpdateRepository;
    private final UpdateManager updateManager;
    private final UpdateHistoryRepository updateHistoryRepository;
    private static Logger logger = Logger.getLogger(ImportRunner.class);

    @Autowired
    public ImportRunner(
        JourneyMapperRepository journeyMapperRepository,
        StopMapperRepository stopMapperRepository,
        ScheduleRepository scheduleRepository,
        ScheduleUpdateRepository scheduleUpdateRepository,
        RealtimeUpdateRepository realtimeUpdateRepository,
        UpdateManager updateManager,
        UpdateHistoryRepository updateHistoryRepository) {
        this.journeyMapperRepository = journeyMapperRepository;
        this.stopMapperRepository = stopMapperRepository;
        this.scheduleRepository = scheduleRepository;
        this.scheduleUpdateRepository = scheduleUpdateRepository;
        this.realtimeUpdateRepository = realtimeUpdateRepository;
        this.updateManager = updateManager;
        this.updateHistoryRepository = updateHistoryRepository;
    }

    @Scheduled(fixedRate = 30 * 1000)
    public void run() throws Exception {
        UpdateHistory latestUpdate = updateHistoryRepository.findLastSuccessUpdate();
        if(latestUpdate == null) {
            logger.info("No Static Data was found - Realtime Update aborted");
            return;
        }
        logger.info("Start Realtime Update - load static data with timestamp: " + latestUpdate.getTime());
        scheduleRepository.load(latestUpdate.getTime());
        stopMapperRepository.load(latestUpdate.getTime());
        journeyMapperRepository.load(latestUpdate.getTime());
        logger.info("Load updates");
        List<FeedEntity> feedEntities = realtimeUpdateRepository.getFeedEntities();
        List<ScheduleUpdateInformation> updateInformations = fetchScheduleUpdateInformations(feedEntities);
        logger.info("Build and save updates");
        scheduleRepository.addScheduleId(updateInformations);
        List<ScheduleUpdate> scheduleUpdates = convert(updateInformations);
        scheduleUpdateRepository.save(scheduleUpdates);
        logger.info("Finish Realtime Update");
    }

    public List<ScheduleUpdateInformation> fetchScheduleUpdateInformations(List<FeedEntity> feedEntities) {
        List<ScheduleUpdateInformation> validStopTimeUpdates = new ArrayList<>();
        int numTotalUpdates = 0;
        for (FeedEntity feedEntity : feedEntities) {
            TripUpdate tripUpdate = feedEntity.getTripUpdate();
            String gtfsTripId = tripUpdate.getTrip().getTripId();
            for(StopTimeUpdate stopTimeUpdate : tripUpdate.getStopTimeUpdateList()) {
                String gtfsStopId = stopTimeUpdate.getStopId();
                Optional<UUID> journeyId = journeyMapperRepository.getIdByGtfsTripId(gtfsTripId);
                Optional<UUID> stopId = stopMapperRepository.getIdByGtfsId(gtfsStopId);
                //TODO: debug logs
                numTotalUpdates++;
                if(journeyId.isPresent() && stopId.isPresent()) {
                    validStopTimeUpdates.add(new ScheduleUpdateInformation(stopTimeUpdate, journeyId.get(), stopId.get()));
                } else {
                    logger.warn("Update not found -> TripId: " + gtfsTripId + " -- StopId: " + gtfsStopId);
                }
            }
        }

        logger.info(String.format("Update Statistic: %d/%d were valid.", validStopTimeUpdates.size(), numTotalUpdates));

        return validStopTimeUpdates;
    }

    private List<ScheduleUpdate> convert(List<ScheduleUpdateInformation> scheduleUpdateInformations) {
        List<ScheduleUpdate> scheduleUpdates = new ArrayList<>();
        for(ScheduleUpdateInformation info : scheduleUpdateInformations) {
            scheduleUpdates.add(ScheduleUpdate.convert(info));
        }
        return scheduleUpdates;
    }
}
