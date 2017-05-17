package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.shared.QueryBuilder;
import ch.bernmobil.vibe.shared.QueryBuilder.Predicate;
import ch.bernmobil.vibe.shared.contract.ScheduleContract;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;

import ch.bernmobil.vibe.shared.entitiy.Schedule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ScheduleRepository {
    private final JdbcTemplate jdbcTemplate;
    private Map<String, Schedule> schedules;
    public ScheduleRepository(@Qualifier("StaticDataSource") DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        schedules = new HashMap<>();
    }

    public void load(Timestamp updateTimestamp) {
        String query = new QueryBuilder()
            .select(ScheduleContract.TABLE_NAME)
            .where(Predicate.equals(ScheduleContract.UPDATE, String.format("'%s'", updateTimestamp)))
            .getQuery();

        List<Schedule> rows = jdbcTemplate.query(query, new ScheduleRowMapper());
        schedules = new HashMap<>(rows.size());
        //TODO: document
        rows.forEach(schedule -> schedules.put(concatKey(schedule.getJourney(), schedule.getStop()), schedule));
    }

    public void addScheduleId(List<ScheduleUpdateInformation> scheduleUpdateInformationList) {
        for(ScheduleUpdateInformation info : scheduleUpdateInformationList) {
            Schedule schedule = schedules.get(concatKey(info.getJourneyId(), info.getStopId()));
            if(schedule != null) {
                info.setScheduleId(schedule.getId());
            }
        }
    }

    private static String concatKey(UUID journeyId, UUID stopId) {
        return String.format("%s:%s", journeyId, stopId);
    }

    private static class ScheduleRowMapper implements RowMapper<ch.bernmobil.vibe.shared.entitiy.Schedule> {
        @Override
        public ch.bernmobil.vibe.shared.entitiy.Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
            UUID id = rs.getObject(ScheduleContract.ID, UUID.class);
            String platform = rs.getString(ScheduleContract.PLATFORM);
            Time plannedArrival = rs.getTime(ScheduleContract.PLANNED_ARRIVAL);
            Time plannedDeparture = rs.getTime(ScheduleContract.PLANNED_DEPARTURE);
            UUID stop = rs.getObject(ScheduleContract.STOP, UUID.class);
            UUID journey = rs.getObject(ScheduleContract.JOURNEY, UUID.class);
            return new Schedule(id, platform, plannedArrival, plannedDeparture, stop, journey);
        }
    }
}
