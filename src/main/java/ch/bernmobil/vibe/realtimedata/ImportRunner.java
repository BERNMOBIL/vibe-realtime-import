package ch.bernmobil.vibe.realtimedata;

import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;
import ch.bernmobil.vibe.realtimedata.mapper.JourneyMapper;
import ch.bernmobil.vibe.realtimedata.mapper.StopMapper;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.google.transit.realtime.GtfsRealtime.TripUpdate;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;
import java.net.URL;
import java.sql.Time;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ImportRunner implements CommandLineRunner{
    private JdbcTemplate staticDataTemplate;
    private JdbcTemplate mapperTemplate;

    @Override
    public void run(String... args) throws Exception {
        List<FeedEntity> feedEntities = loadRealtimeUpdates();
        JourneyMapper.loadMappings(mapperTemplate);
        StopMapper.loadMappings(mapperTemplate);
        List<ScheduleUpdate> scheduleUpdates = convert(feedEntities);
        save(scheduleUpdates);
    }

    @Autowired
    public void setMapperJdbcTemplate(@Qualifier("MapperDataSource") DataSource mapperDataSource) {
        mapperTemplate = new JdbcTemplate(mapperDataSource);
    }

    @Autowired
    public void setStaticJdbcTemplate(@Qualifier("StaticDataSource") DataSource staticDataSource) {
        staticDataTemplate = new JdbcTemplate(staticDataSource);
    }

    private void save(List<ScheduleUpdate> scheduleUpdates) {
        String insertQuery = "INSERT INTO schedule_update(actual_arrival, actual_departure, schedule) VALUES(?, ?, ?)";
        int[] types = new int[] {Types.TIME, Types.TIME, Types.INTEGER};
        Object[] params;

        for(ScheduleUpdate scheduleUpdate : scheduleUpdates) {
            params = new Object[]{scheduleUpdate.getActualArrival(), scheduleUpdate.getActualDeparture(), scheduleUpdate.getSchedule()};
            staticDataTemplate.update(insertQuery, params, types);
        }
    }



    public List<ScheduleUpdate> convert(List<FeedEntity> feedEntities) {
        Map<String, StopTimeUpdate> validStopTimeUpdates = new HashMap<>();
        String query = "SELECT * FROM schedule WHERE ";

        for (FeedEntity feedEntity : feedEntities) {
            TripUpdate tripUpdate = feedEntity.getTripUpdate();
            String gtfsTripId = tripUpdate.getTrip().getTripId();

            for(StopTimeUpdate stopTimeUpdate : tripUpdate.getStopTimeUpdateList()) {
                String gtfsStopId = stopTimeUpdate.getStopId();
                gtfsTripId = "38_000827_11_90075_94201_15:59_16:13"; //TODO: remove this mocks
                gtfsStopId = "90075_0";
                Integer journeyId = JourneyMapper.getIdByGtfsTripId(gtfsTripId);
                Integer stopId = StopMapper.getIdByGtfsId(gtfsStopId);
                if(journeyId != null && stopId != null) {
                    validStopTimeUpdates.put(getKey(journeyId, stopId), stopTimeUpdate);
                    query = extendQuery(query, journeyId, stopId);
                }
            }
        }

        return createScheduleUpdates(validStopTimeUpdates, query.substring(0, query.length()-4));
    }

    private List<ScheduleUpdate> createScheduleUpdates(Map<String, StopTimeUpdate> validStopTimeUpdates, String query) {
        List<ScheduleUpdate> scheduleUpdates = new ArrayList<>();

        if(!validStopTimeUpdates.isEmpty()) {
            for (Map row : staticDataTemplate.queryForList(query)) {
                StopTimeUpdate stopTimeUpdate = validStopTimeUpdates.get(getKey((int)row.get("journey"), (int)row.get("stop")));
                ScheduleUpdate scheduleUpdate = new ScheduleUpdate();
                scheduleUpdate.setActualDeparture(parseUpdateTime(stopTimeUpdate.getDeparture().getTime()));
                scheduleUpdate.setActualArrival(parseUpdateTime(stopTimeUpdate.getArrival().getTime()));
                scheduleUpdate.setSchedule((Integer)row.get("id"));
                scheduleUpdates.add(scheduleUpdate);
            }
        }

        return scheduleUpdates;
    }

    private String extendQuery(String query, Integer journeyId, Integer stopId) {
        return query + "journey = " + journeyId + " AND stop = " + stopId + " OR ";
    }

    private String getKey(int journeyId, int stopId) {
        return ":journey:"+journeyId+":stop:"+stopId;
    }


    private List<FeedEntity> loadRealtimeUpdates() throws Exception {
        URL url = new URL("https://wp-test.bernmobil.ch/gtfs/827/realtime?apikey=b4059f45-9b52-4511-y68f-0fdfd0fa11c1");
        FeedMessage feedMessage = FeedMessage.parseFrom(url.openStream());
        return feedMessage.getEntityList();
    }

    private static Time parseUpdateTime(Long timestamp) {
        return timestamp == 0 ? null : Time.valueOf(
            LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()).toLocalTime());
    }
}
