package ch.bernmobil.vibe.realtimedata;

import ch.bernmobil.vibe.realtimedata.repository.JourneyMapperRepository;
import ch.bernmobil.vibe.realtimedata.repository.RealtimeUpdateRepository;
import ch.bernmobil.vibe.realtimedata.repository.ScheduleRepository;
import ch.bernmobil.vibe.realtimedata.repository.ScheduleUpdateRepository;
import ch.bernmobil.vibe.realtimedata.repository.StopMapperRepository;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;
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
    private static Logger logger = Logger.getLogger(ImportRunner.class);

    @Autowired
    public ImportRunner(
        JourneyMapperRepository journeyMapperRepository,
        StopMapperRepository stopMapperRepository,
        ScheduleRepository scheduleRepository,
        ScheduleUpdateRepository scheduleUpdateRepository,
        RealtimeUpdateRepository realtimeUpdateRepository,
        UpdateManager updateManager) {
        this.journeyMapperRepository = journeyMapperRepository;
        this.stopMapperRepository = stopMapperRepository;
        this.scheduleRepository = scheduleRepository;
        this.scheduleUpdateRepository = scheduleUpdateRepository;
        this.realtimeUpdateRepository = realtimeUpdateRepository;
        this.updateManager = updateManager;
    }

    @Scheduled(fixedRate = 30 * 1000)
    public void run() throws Exception {
        while(!updateManager.checkUpdateCollision()) {
            logger.info("update collision, try again..");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        logger.info("Start Realtime Update - load static data");
        scheduleRepository.load();
        stopMapperRepository.load();
        journeyMapperRepository.load();
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

        for (FeedEntity feedEntity : feedEntities) {
            TripUpdate tripUpdate = feedEntity.getTripUpdate();
            String gtfsTripId = tripUpdate.getTrip().getTripId();
            for(StopTimeUpdate stopTimeUpdate : tripUpdate.getStopTimeUpdateList()) {
                String gtfsStopId = stopTimeUpdate.getStopId();
                Optional<UUID> journeyId = journeyMapperRepository.getIdByGtfsTripId(gtfsTripId);
                Optional<UUID> stopId = stopMapperRepository.getIdByGtfsId(gtfsStopId);
                //TODO: debug logs
                if(journeyId.isPresent() && stopId.isPresent()) {
                    validStopTimeUpdates.add(new ScheduleUpdateInformation(stopTimeUpdate, journeyId.get(), stopId.get()));
                } else {
                    logger.warn("Update not found -> TripId: " + gtfsTripId + " -- StopId: " + gtfsStopId);
                }
            }
        }
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
