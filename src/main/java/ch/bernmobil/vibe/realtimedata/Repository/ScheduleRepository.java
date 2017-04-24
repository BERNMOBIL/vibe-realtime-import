package ch.bernmobil.vibe.realtimedata.Repository;

import ch.bernmobil.vibe.realtimedata.QueryBuilder;
import ch.bernmobil.vibe.realtimedata.entity.Schedule;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;
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
        load();
    }


    private void load() {
        String query = new QueryBuilder().Select("schedule").getQuery();
        schedules = new HashMap<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        for (Map row : rows) {
            Integer journeyId = (Integer)row.get("journey");
            Integer stopId = (Integer)row.get("stop");
            Integer id = (Integer)row.get("id");
            add(new Schedule(id, journeyId, stopId));
        }
    }

    public void add(Schedule schedule) {
        schedules.put(schedule.getJourneyId() + ":" + schedule.getStopId(), schedule);
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
