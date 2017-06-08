package ch.bernmobil.vibe.realtimedata.repository;

import static java.util.stream.Collectors.toList;
import static org.jooq.impl.DSL.table;

import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;
import ch.bernmobil.vibe.shared.contract.ScheduleContract;
import ch.bernmobil.vibe.shared.entitiy.Schedule;

import java.sql.Timestamp;
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

/**
 * Database-Repository for accessing the {@link Schedule}-information's.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Component
public class ScheduleRepository extends BaseRepository<Schedule> {
    /**
     * Constructs an instance using a {@link DSLContext}
     * @param dslContext Object of the JOOQ Query Builder to access the database
     */
    public ScheduleRepository(@Qualifier("StaticDslContext")DSLContext dslContext) {
        super(Schedule.class, dslContext);
    }

    /**
     * Populates a {@link List} of {@link ScheduleUpdateInformation} with the corresponding {@link Schedule}-Id.
     * The Schedule ID is fetched from the {@link Schedule}-Table using a {@link ch.bernmobil.vibe.shared.entitiy.Journey}-Id and {@link ch.bernmobil.vibe.shared.entitiy.Stop}-Id
     * <p>Notice: The {@link Schedule}-Id is needed to match a {@link ch.bernmobil.vibe.shared.entitiy.ScheduleUpdate} to the right {@link Schedule}</p>
     * <p>Notice: If a {@link Schedule} can not be found, it will be ignored</p>
     * @param scheduleUpdateInformationList to be populated by the {@link Schedule}-Id
     */
    public void addScheduleId(List<ScheduleUpdateInformation> scheduleUpdateInformationList) {
        for(ScheduleUpdateInformation info : scheduleUpdateInformationList) {
            Schedule schedule = getEntries().get(concatKey(info.getJourneyId(), info.getStopId()));
            if(schedule != null) {
                info.setScheduleId(schedule.getId());
            }
        }
    }

    /**
     * Helper-Method used to create a unique Key for a {@link Schedule} using the {@link ch.bernmobil.vibe.shared.entitiy.Journey}-Id and {@link ch.bernmobil.vibe.shared.entitiy.Stop}-Id
     * @param journeyId of a Schedule
     * @param stopId of a Schedule
     * @return created Key
     */
    private static String concatKey(UUID journeyId, UUID stopId) {
        return String.format("%s:%s", journeyId, stopId);
    }

    /**
     * Hook for the {@link #load(Timestamp)}-Method using the Template Method Pattern
     * @return Table used in a query executed with a Jooq {@link DSLContext}
     */
    @Override
    protected Table<Record> getTable() {
        return table(ScheduleContract.TABLE_NAME);
    }

    /**
     * Hook for the {@link #load(Timestamp)}-Method using the Template Method Pattern
     * @return Fields used in a query executed with a Jooq {@link DSLContext}
     */
    @Override
    protected Collection<Field<?>> getFields() {
        final String[] columnsToFetch = {ScheduleContract.ID, ScheduleContract.PLATFORM,
            ScheduleContract.PLANNED_ARRIVAL, ScheduleContract.PLANNED_DEPARTURE,
            ScheduleContract.STOP, ScheduleContract.JOURNEY};
        return Arrays.stream(columnsToFetch).map(DSL::field).collect(toList());
    }

    /**
     * Hook for the {@link #load(Timestamp)}-Method using the Template Method Pattern
     * @return {@link Consumer} used to save the loaded {@link Schedule}s from the {@link #load(Timestamp)}-Method
     */
    @Override
    protected Consumer<Schedule> getConsumer() {
        return schedule -> getEntries().put(concatKey(schedule.getJourney(), schedule.getStop()), schedule);
    }
}
