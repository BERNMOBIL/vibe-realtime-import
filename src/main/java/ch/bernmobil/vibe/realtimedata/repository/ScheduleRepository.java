package ch.bernmobil.vibe.realtimedata.repository;

import static java.util.stream.Collectors.toList;
import static org.jooq.impl.DSL.table;

import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;
import ch.bernmobil.vibe.shared.contract.ScheduleContract;
import ch.bernmobil.vibe.shared.entitiy.Schedule;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ScheduleRepository extends BaseRepository<Schedule> {
    public ScheduleRepository(@Qualifier("StaticDslContext")DSLContext dslContext) {
        super(Schedule.class, dslContext);
    }

    public void addScheduleId(List<ScheduleUpdateInformation> scheduleUpdateInformationList) {
        for(ScheduleUpdateInformation info : scheduleUpdateInformationList) {
            Schedule schedule = getEntries().get(concatKey(info.getJourneyId(), info.getStopId()));
            if(schedule != null) {
                info.setScheduleId(schedule.getId());
            }
        }
    }

    private static String concatKey(UUID journeyId, UUID stopId) {
        return String.format("%s:%s", journeyId, stopId);
    }

    @Override
    protected Table<Record> getTable() {
        return table(ScheduleContract.TABLE_NAME);
    }

    @Override
    protected Collection<Field<?>> getFields() {
        final String[] columnsToFetch = {ScheduleContract.ID, ScheduleContract.PLATFORM,
            ScheduleContract.PLANNED_ARRIVAL, ScheduleContract.PLANNED_DEPARTURE,
            ScheduleContract.STOP, ScheduleContract.JOURNEY};
        return Arrays.stream(columnsToFetch).map(DSL::field).collect(toList());
    }

    @Override
    protected Consumer<Schedule> getConsumer() {
        return schedule -> getEntries().put(concatKey(schedule.getJourney(), schedule.getStop()), schedule);
    }
}
