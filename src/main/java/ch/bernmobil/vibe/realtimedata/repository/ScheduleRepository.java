package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;
import ch.bernmobil.vibe.shared.QueryBuilder;
import ch.bernmobil.vibe.shared.QueryBuilder.Predicate;
import ch.bernmobil.vibe.shared.contract.ScheduleContract;
import ch.bernmobil.vibe.shared.entitiy.Schedule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Component
public class ScheduleRepository extends BaseRepository<Schedule> {

    public ScheduleRepository(@Qualifier("StaticDataSource") DataSource dataSource) {
        super(dataSource, new ScheduleRowMapper());
    }

    public void load(Timestamp updateTimestamp) {
        String query = new QueryBuilder()
            .select(ScheduleContract.TABLE_NAME)
            .where(Predicate.equals(ScheduleContract.UPDATE, String.format("'%s'", updateTimestamp)))
            .getQuery();
        //TODO: document
        super.load(query, schedule -> getMappings().put(concatKey(schedule.getJourney(), schedule.getStop()), schedule));
    }

    public void addScheduleId(List<ScheduleUpdateInformation> scheduleUpdateInformationList) {
        for(ScheduleUpdateInformation info : scheduleUpdateInformationList) {
            Schedule schedule = getMappings().get(concatKey(info.getJourneyId(), info.getStopId()));
            if(schedule != null) {
                info.setScheduleId(schedule.getId());
            }
        }
    }

    public static String concatKey(UUID journeyId, UUID stopId) {
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
