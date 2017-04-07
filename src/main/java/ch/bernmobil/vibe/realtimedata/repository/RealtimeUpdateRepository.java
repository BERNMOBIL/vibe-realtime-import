package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;
import ch.bernmobil.vibe.realtimedata.mapper.JourneyMapper;
import ch.bernmobil.vibe.realtimedata.mapper.StopMapper;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.google.transit.realtime.GtfsRealtime.TripUpdate;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class RealtimeUpdateRepository {

    private static List<ScheduleUpdate> scheduleUpdates = null;

    private final JdbcTemplate staticDataTemplate;
    private final JdbcTemplate mapperTemplate;
    public RealtimeUpdateRepository(DataSource staticDataSource, DataSource mapperDataSource) {
        staticDataTemplate = new JdbcTemplate(staticDataSource);
        mapperTemplate = new JdbcTemplate(mapperDataSource);
    }

    public List<ScheduleUpdate> extractScheduleUpdates(List<FeedEntity> feedEntities) {
        List<ScheduleUpdate> scheduleUpdates = new ArrayList<>();
        Map<String, StopTimeUpdate> validStopTimeUpdates = new HashMap<>();

        String query = "SELECT * FROM schedule WHERE ";

        for (FeedEntity feedEntity : feedEntities) {
            TripUpdate tripUpdate = feedEntity.getTripUpdate();

            String gtfsTripId = tripUpdate.getTrip().getTripId();

            for(StopTimeUpdate stopTimeUpdate : tripUpdate.getStopTimeUpdateList()) {
                String gtfsStopId = stopTimeUpdate.getStopId();

                //TODO: Remove this mock data.
                gtfsTripId = "38_000827_11_90075_94201_15:59_16:13";
                gtfsStopId = "90075_0";
                //END Mock Data

                Integer journeyId = JourneyMapper.getIdByGtfsTripId(gtfsTripId);
                Integer stopId = StopMapper.getIdByGtfsId(gtfsStopId);
                if(journeyId == null || stopId == null) {
                    continue;
                }

                validStopTimeUpdates.put(":journey:"+journeyId+":stop:"+stopId, stopTimeUpdate);
                query += "journey = " + journeyId + " AND stop = " + stopId + " OR ";
            }
        }
        query = query.substring(0, query.length()-4);

        if(!validStopTimeUpdates.isEmpty()) {
            for (Map row : staticDataTemplate.queryForList(query)) {
                StopTimeUpdate stopTimeUpdate = validStopTimeUpdates.get(":journey:"+row.get("journey")+":stop:"+row.get("stop"));
                ScheduleUpdate scheduleUpdate = new ScheduleUpdate();
                scheduleUpdate.setActualDeparture(parseUpdateTime(stopTimeUpdate.getDeparture().getTime()));
                scheduleUpdate.setActualArrival(parseUpdateTime(stopTimeUpdate.getArrival().getTime()));
                scheduleUpdate.setSchedule((Integer)row.get("id"));
                scheduleUpdates.add(scheduleUpdate);
            }
        }

        return scheduleUpdates;
    }


    public List<ScheduleUpdate> getScheduleUpdates() throws Exception {
        if(scheduleUpdates == null) {
            List<FeedEntity> feedEntities = loadRealtimeUpdates();
            JourneyMapper.loadMappings(mapperTemplate);
            StopMapper.loadMappings(mapperTemplate);
            scheduleUpdates = extractScheduleUpdates(feedEntities);
        }
        return scheduleUpdates;
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
