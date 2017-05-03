package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.QueryBuilder.PreparedStatement;
import ch.bernmobil.vibe.realtimedata.contract.ScheduleContract;
import ch.bernmobil.vibe.realtimedata.contract.ScheduleUpdateContract;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
            .insert(ScheduleUpdateContract.TABLE_NAME,
                    new String[]{ScheduleUpdateContract.SCHEDULE,
                            ScheduleUpdateContract.ACTUAL_ARRIVAL,
                            ScheduleUpdateContract.ACTUAL_DEPARTURE})
            .getQuery();
        insertTypes = new int[] {Types.INTEGER, Types.TIME, Types.TIME};

    }

    public void save(Collection<ScheduleUpdate> scheduleUpdates) {
        List<Object[]> newScheduleUpdates = new ArrayList<>(scheduleUpdates.size());
        for(ScheduleUpdate scheduleUpdate : scheduleUpdates) {
            newScheduleUpdates.add(new Object[]{
                scheduleUpdate.getSchedule(),
                scheduleUpdate.getActualArrival(),
                scheduleUpdate.getActualDeparture()
            });
        }
        jdbcTemplate.batchUpdate(insertQuery, newScheduleUpdates, insertTypes);
    }
}
