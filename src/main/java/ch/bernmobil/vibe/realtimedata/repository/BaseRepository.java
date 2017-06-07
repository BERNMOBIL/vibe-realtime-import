package ch.bernmobil.vibe.realtimedata.repository;

import static org.jooq.impl.DSL.inline;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;


public abstract class BaseRepository<T> {

    private final Class<T> classType;
    private Map<String, T> mappings;
    private DSLContext dslContext;

    BaseRepository(Class<T> entityClassType, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.classType = entityClassType;
    }


    public void load(Timestamp updateTimestamp) {
        //TODO: remove raw string
        Field<Timestamp> updateField = DSL.field("update", Timestamp.class);
        List<T> list = dslContext
            .select(getFields())
            .from(getTable())
            .where(updateField.equal(inline(updateTimestamp)))
            .fetchInto(classType);
        mappings = new HashMap<>(list.size());

        list.forEach(getConsumer());
    }

    protected abstract Table<Record> getTable();
    protected abstract Collection<Field<?>> getFields();
    protected abstract Consumer<T> getConsumer();
    /**
     *
     * @return mappings loaded with the load method
     */
    Map<String, T> getMappings() {
        return mappings;
    }
}
