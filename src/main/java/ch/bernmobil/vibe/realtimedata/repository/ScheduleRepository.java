package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.QueryBuilder;
import ch.bernmobil.vibe.realtimedata.contract.ScheduleContract;
import ch.bernmobil.vibe.realtimedata.entity.Schedule;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ScheduleRepository {
    private final JdbcTemplate jdbcTemplate;

    private Map<String, Schedule> schedules;

    public ScheduleRepository(@Qualifier("StaticDataSource")DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void load() {
        String query = new QueryBuilder().select(ScheduleContract.TABLE_NAME).getQuery();
        schedules = new HashMap<>();
        for (Map<String, Object> row : jdbcTemplate.queryForList(query)) {
            int journeyId = (int)row.get(ScheduleContract.JOURNEY);
            int stopId = (int)row.get(ScheduleContract.STOP);
            int id = (int)row.get(ScheduleContract.ID);
            Schedule schedule = new Schedule(id, journeyId, stopId);
            schedules.put(schedule.getJourneyId() + ":" + schedule.getStopId(), schedule);
        }
    }

    public Schedule get(int journeyId, int stopId) {
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
