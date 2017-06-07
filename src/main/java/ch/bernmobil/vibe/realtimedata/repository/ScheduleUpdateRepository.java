package ch.bernmobil.vibe.realtimedata.repository;

import static java.util.stream.Collectors.toList;
import static org.jooq.impl.DSL.table;

import ch.bernmobil.vibe.shared.contract.ScheduleUpdateContract;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ScheduleUpdateRepository {
    private final DSLContext dslContext;

    public ScheduleUpdateRepository(@Qualifier("StaticDslContext")DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public void save(Collection<ScheduleUpdate> scheduleUpdates) {
        Collection<Field<?>> fields = Arrays.stream(ScheduleUpdateContract.COLUMNS).map(DSL::field).collect(toList());
        for(ScheduleUpdate scheduleUpdate : scheduleUpdates) {
            dslContext
                .insertInto(table(ScheduleUpdateContract.TABLE_NAME), fields)
                .values(DSL.val(UUID.randomUUID()), DSL.val(scheduleUpdate.getSchedule()),
                    DSL.value(scheduleUpdate.getActualArrival()),
                    DSL.value(scheduleUpdate.getActualDeparture()))
                .execute();
        }
    }

    public void deleteAll() {
        dslContext.truncate(ScheduleUpdateContract.TABLE_NAME).execute();
    }
}
