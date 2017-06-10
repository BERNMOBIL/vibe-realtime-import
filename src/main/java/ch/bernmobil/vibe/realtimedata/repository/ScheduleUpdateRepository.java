package ch.bernmobil.vibe.realtimedata.repository;

import static java.util.stream.Collectors.toList;
import static org.jooq.impl.DSL.table;

import ch.bernmobil.vibe.shared.contract.ScheduleUpdateContract;
import ch.bernmobil.vibe.shared.entity.ScheduleUpdate;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertValuesStepN;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Database-Repository for accessing the {@link ScheduleUpdate}-information's.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Component
public class ScheduleUpdateRepository {
    private final DSLContext dslContext;
    /**
     * Constructs an instance using a {@link DSLContext}
     * @param dslContext Object of the JOOQ Query Builder to access the database
     */
    public ScheduleUpdateRepository(@Qualifier("StaticDslContext")DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /**
     * Saves a {@link Collection} of {@link ScheduleUpdate}'s to the database using a Jooq {@link DSLContext}
     * <p>Notice: The {@link DSLContext} batch method is used because of the better performance</p>
     * @param scheduleUpdates {@link Collection} to be saved
     */
    public void save(Collection<ScheduleUpdate> scheduleUpdates) {
        Collection<Field<?>> fields = Arrays.stream(ScheduleUpdateContract.COLUMNS).map(DSL::field).collect(toList());
        Collection<InsertValuesStepN<Record>> insertStatements = scheduleUpdates
            .stream()
            .map((su) -> dslContext.insertInto(table(ScheduleUpdateContract.TABLE_NAME), fields)
                .values(DSL.val(UUID.randomUUID()),DSL.val(su.getSchedule()),
                    DSL.val(su.getActualArrival()), DSL.val(su.getActualDeparture())))
            .collect(toList());

        dslContext.batch(insertStatements).execute();
    }

    /**
     * Deletes all {@link ScheduleUpdate}s from the Database
     */
    public void deleteAll() {
        dslContext.truncate(ScheduleUpdateContract.TABLE_NAME).execute();
    }
}
