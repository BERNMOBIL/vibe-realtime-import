package ch.bernmobil.vibe.realtimedata.Repository;

import ch.bernmobil.vibe.realtimedata.QueryBuilder;
import ch.bernmobil.vibe.realtimedata.QueryBuilder.PreparedStatement;
import ch.bernmobil.vibe.realtimedata.entity.Schedule;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;
import java.sql.Time;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ScheduleUpdateRepository {
    private final String insertQuery;
    private final int[] insertTypes;
    private final JdbcTemplate jdbcTemplate;

    public ScheduleUpdateRepository(@Qualifier("StaticDataSource")DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        insertQuery = new PreparedStatement()
            .Insert("schedule_update", "actual_arrival", "actual_departure", "schedule")
            .getQuery();
        insertTypes = new int[] {Types.TIME, Types.TIME, Types.INTEGER};

    }

    public void save(Collection<ScheduleUpdate> scheduleUpdates) {
        List<Object[]> newScheduleUpdates = new ArrayList<>();
        for(ScheduleUpdate scheduleUpdate : scheduleUpdates) {
            newScheduleUpdates.add(new Object[]{
                scheduleUpdate.getActualArrival(),
                scheduleUpdate.getActualDeparture(),
                scheduleUpdate.getSchedule()
            });
        }
        jdbcTemplate.batchUpdate(insertQuery, newScheduleUpdates, insertTypes);
    }

    public void save(ScheduleUpdate scheduleUpdate) {
        Object[] params = new Object[]{
            scheduleUpdate.getActualArrival(),
            scheduleUpdate.getActualDeparture(),
            scheduleUpdate.getSchedule()
        };
        jdbcTemplate.update(insertQuery, params, insertTypes);
    }
}
