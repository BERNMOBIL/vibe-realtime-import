package ch.bernmobil.vibe.realtimedata;

import static com.google.transit.realtime.GtfsRealtime.FeedEntity;
import static com.google.transit.realtime.GtfsRealtime.FeedMessage;
import static com.google.transit.realtime.GtfsRealtime.TripUpdate;

import ch.bernmobil.vibe.realtimedata.entity.Schedule;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;
import ch.bernmobil.vibe.realtimedata.entity.sync.JourneyMapper;
import ch.bernmobil.vibe.realtimedata.entity.sync.StopMapper;
import ch.bernmobil.vibe.realtimedata.repository.JourneyMapperRepository;
import ch.bernmobil.vibe.realtimedata.repository.ScheduleRepository;
import ch.bernmobil.vibe.realtimedata.repository.ScheduleUpdateRepository;
import ch.bernmobil.vibe.realtimedata.repository.StopMapperRepository;
import com.google.protobuf.TextFormat;
import com.google.transit.realtime.GtfsRealtime;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class RealtimeImport {

    public static void main(String[] args) {
        SpringApplication.run(RealtimeImport.class, args);
    }

    @Component
    public class ImportRunner implements CommandLineRunner {

        private final JourneyMapperRepository journeyMapperRepository;
        private final StopMapperRepository stopMapperRepository;
        private final ScheduleUpdateRepository scheduleUpdateRepository;
        private final ScheduleRepository scheduleRepository;

        @Autowired
        public ImportRunner(JourneyMapperRepository journeyMapperRepository,
            StopMapperRepository stopMapperRepository,
            ScheduleUpdateRepository scheduleUpdateRepository,
            ScheduleRepository scheduleRepository) {
            this.journeyMapperRepository = journeyMapperRepository;
            this.stopMapperRepository = stopMapperRepository;
            this.scheduleUpdateRepository = scheduleUpdateRepository;
            this.scheduleRepository = scheduleRepository;
        }

        @Override
        public void run(String... args) throws Exception {
            List<GtfsRealtime.FeedEntity> list = getRealtimeList();

            for(FeedEntity feedEntity: list) {
                TripUpdate tripUpdate = feedEntity.getTripUpdate();

                JourneyMapper journeyMapper = journeyMapperRepository.findFirstByGtfsTripId(tripUpdate.getTrip().getTripId());

                if(journeyMapper == null) {
                    continue;
                }

                Long journey = journeyMapper.getId();

                for(TripUpdate.StopTimeUpdate stopTimeUpdate: tripUpdate.getStopTimeUpdateList()) {
                    StopMapper stopMapper = stopMapperRepository.findFirstByGtfsId(stopTimeUpdate.getStopId());

                    if(stopMapper == null) {
                        continue;
                    }

                    Schedule schedule = scheduleRepository.findFirstByJourneyAndStop(journey, stopMapper.getId());

                    if(schedule == null) {
                        continue;
                    }

                    ScheduleUpdate scheduleUpdate = new ScheduleUpdate();
                    scheduleUpdate.setActualDeparture(parseUpdateTime(stopTimeUpdate.getDeparture().getTime()));
                    scheduleUpdate.setActualArrival(parseUpdateTime(stopTimeUpdate.getArrival().getTime()));

                    scheduleUpdate = scheduleUpdateRepository.save(scheduleUpdate);
                    schedule.setScheduleUpdate(scheduleUpdate.getId());
                    scheduleRepository.save(schedule);
                }
            }
        }
    }

    private Time parseUpdateTime(Long timestamp) {
        return timestamp == 0 ? null : Time.valueOf(LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()).toLocalTime());
    }

    private static List<GtfsRealtime.FeedEntity> getRealtimeList() throws Exception {
        URL url = new URL("https://wp-test.bernmobil.ch/gtfs/827/realtime?apikey=b4059f45-9b52-4511-y68f-0fdfd0fa11c1");
        FeedMessage feedMessage = FeedMessage.parseFrom(url.openStream());

        return feedMessage.getEntityList();
    }

    private static List<GtfsRealtime.FeedEntity> getMockList() throws Exception{
        InputStream inputStream = new URL("http://localhost:9000/api?filename=realtime.pb").openStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        FeedMessage.Builder builder = FeedMessage.newBuilder();
        TextFormat.merge(reader, builder);
        return builder.build().getEntityList();
    }
}
