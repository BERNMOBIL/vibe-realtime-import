package ch.bernmobil.vibe.realtimedata;

import ch.bernmobil.vibe.realtimedata.Repository.JourneyMapperRepository;
import ch.bernmobil.vibe.realtimedata.Repository.RealtimeUpdateRepository;
import ch.bernmobil.vibe.realtimedata.Repository.ScheduleRepository;
import ch.bernmobil.vibe.realtimedata.Repository.ScheduleUpdateRepository;
import ch.bernmobil.vibe.realtimedata.Repository.StopMapperRepository;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.TripUpdate;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ImportRunner implements CommandLineRunner{
    private final JourneyMapperRepository journeyMapperRepository;
    private final StopMapperRepository stopMapperRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleUpdateRepository scheduleUpdateRepository;
    private final RealtimeUpdateRepository realtimeUpdateRepository;

    private static Logger logger = Logger.getLogger(ImportRunner.class);

    @Autowired
    public ImportRunner(
        JourneyMapperRepository journeyMapperRepository,
        StopMapperRepository stopMapperRepository,
        ScheduleRepository scheduleRepository,
        ScheduleUpdateRepository scheduleUpdateRepository,
        RealtimeUpdateRepository realtimeUpdateRepository) {
        this.journeyMapperRepository = journeyMapperRepository;
        this.stopMapperRepository = stopMapperRepository;
        this.scheduleRepository = scheduleRepository;
        this.scheduleUpdateRepository = scheduleUpdateRepository;
        this.realtimeUpdateRepository = realtimeUpdateRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<FeedEntity> feedEntities = realtimeUpdateRepository.getFeedEntities();
        List<ScheduleUpdateInformation> updateInformations = fetchScheduleUpdateInformations(feedEntities);
        scheduleRepository.addScheduleId(updateInformations);
        List<ScheduleUpdate> scheduleUpdates = convert(updateInformations);
        scheduleUpdateRepository.save(scheduleUpdates);
    }

    public List<ScheduleUpdateInformation> fetchScheduleUpdateInformations(List<FeedEntity> feedEntities) {
        List<ScheduleUpdateInformation> validStopTimeUpdates = new ArrayList<>();

        for (FeedEntity feedEntity : feedEntities) {
            TripUpdate tripUpdate = feedEntity.getTripUpdate();
            String gtfsTripId = tripUpdate.getTrip().getTripId();
            for(StopTimeUpdate stopTimeUpdate : tripUpdate.getStopTimeUpdateList()) {
                String gtfsStopId = stopTimeUpdate.getStopId();
                Integer journeyId = journeyMapperRepository.getIdByGtfsTripId(gtfsTripId);
                Integer stopId = stopMapperRepository.getIdByGtfsId(gtfsStopId);
                if(journeyId != null && stopId != null) {
                    ScheduleUpdateInformation scheduleUpdateInformation = new ScheduleUpdateInformation(stopTimeUpdate);
                    scheduleUpdateInformation.setJourneyId(journeyId);
                    scheduleUpdateInformation.setStopId(stopId);
                    validStopTimeUpdates.add(scheduleUpdateInformation);
                } else {
                    logger.info("Update not found -> TripId: " + gtfsTripId + " -- StopId: " + gtfsStopId);
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
