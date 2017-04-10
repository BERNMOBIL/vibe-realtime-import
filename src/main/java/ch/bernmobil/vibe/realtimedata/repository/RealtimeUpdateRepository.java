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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

//@Component
//@ComponentScan
public class RealtimeUpdateRepository {

    private static List<ScheduleUpdate> scheduleUpdates = null;

    private DataSource mapperDataSource;
    private DataSource staticDataSource;

    @Autowired
    public void setMapperDataSource(@Qualifier("MapperDataSource") DataSource mapperDataSource) {
        this.mapperDataSource = mapperDataSource;
        mapperTemplate = new JdbcTemplate(mapperDataSource);

    }

    @Autowired
    public void setStaticDataSource(@Qualifier("StaticDataSource") DataSource staticDataSource) {
        this.staticDataSource = staticDataSource;
        staticDataTemplate = new JdbcTemplate(staticDataSource);
    }

    private JdbcTemplate staticDataTemplate;
    private JdbcTemplate mapperTemplate;

    public List<ScheduleUpdate> extractScheduleUpdates(List<FeedEntity> feedEntities) {
            return new ArrayList<>();
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
