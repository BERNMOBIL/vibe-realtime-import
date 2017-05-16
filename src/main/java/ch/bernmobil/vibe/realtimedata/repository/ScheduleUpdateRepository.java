package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.QueryBuilder.PreparedStatement;
import ch.bernmobil.vibe.realtimedata.contract.ScheduleUpdateContract;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;
import java.util.Collection;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class ScheduleUpdateRepository {
    private final String insertQuery;
    private final JdbcTemplate jdbcTemplate;

    public ScheduleUpdateRepository(@Qualifier("StaticDataSource")DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        insertQuery = new PreparedStatement()
            .Insert(ScheduleUpdateContract.TABLE_NAME, ScheduleUpdateContract.COLUMNS)
            .getQuery();
    }

    public void save(Collection<ScheduleUpdate> scheduleUpdates) {
        for(ScheduleUpdate scheduleUpdate : scheduleUpdates) {
            jdbcTemplate.update(insertQuery, UUID.randomUUID(), scheduleUpdate.getSchedule(),
                scheduleUpdate.getActualArrival(), scheduleUpdate.getActualDeparture());
        }
    }
}
