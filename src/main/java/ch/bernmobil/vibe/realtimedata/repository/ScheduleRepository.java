package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.QueryBuilder;
import ch.bernmobil.vibe.realtimedata.QueryBuilder.Predicate;
import ch.bernmobil.vibe.realtimedata.UpdateManager;
import ch.bernmobil.vibe.realtimedata.contract.ScheduleContract;
import ch.bernmobil.vibe.realtimedata.contract.StopMapperContract;
import ch.bernmobil.vibe.realtimedata.entity.Schedule;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ScheduleRepository {
    private final JdbcTemplate jdbcTemplate;
    private Map<String, Schedule> schedules;

    public ScheduleRepository(@Qualifier("StaticDataSource")DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        schedules = new HashMap<>();
    }

    public void load() {
        String query = new QueryBuilder()
            .select(ScheduleContract.TABLE_NAME)
            .where(Predicate.equals(ScheduleContract.UPDATE, String.format("'%s'", UpdateManager.getLatestUpdateTimestamp())))
            .getQuery();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        schedules = new HashMap<>(rows.size());
        for (Map<String, Object> row : rows) {
            UUID journeyId = (UUID)row.get(ScheduleContract.JOURNEY);
            UUID stopId = (UUID)row.get(ScheduleContract.STOP);
            UUID id = (UUID)row.get(ScheduleContract.ID);
            Schedule schedule = new Schedule(id, journeyId, stopId);
            schedules.put(schedule.getJourneyId() + ":" + schedule.getStopId(), schedule);
        }
    }

    public Schedule get(UUID journeyId, UUID stopId) {
        return schedules.get(journeyId + ":" + stopId);
    }

    public void addScheduleId(List<ScheduleUpdateInformation> scheduleUpdateInformations) {
        for(ScheduleUpdateInformation info : scheduleUpdateInformations) {
            Schedule schedule = get(info.getJourneyId(), info.getStopId());
            if(schedule != null) {
                info.setScheduleId(schedule.getId());
            }
        }
    }
}
