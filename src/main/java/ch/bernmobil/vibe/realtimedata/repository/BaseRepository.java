package ch.bernmobil.vibe.realtimedata.repository;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * An abstract Class which defines minimal functionality of a default repository
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 *
 * @param <T> The Type of the Entity handled by the repository
 */
public abstract class BaseRepository<T> {
    private static final String UPDATE_COLUMN = "update";
    private final Class<T> classType;
    private Map<String, T> entries;
    @SuppressWarnings("WeakerAccess")
    protected DSLContext dslContext;

    /**
     *
     * @param entityClassType Type of Entity <p>Note: Has to be passed via Constructor because of Java-Type-Erasure</p>
     * @param dslContext Jooq DSLContext to use for queries within this repository.
     */
    BaseRepository(Class<T> entityClassType, DSLContext dslContext) {
        this.dslContext = dslContext;
        this.classType = entityClassType;
    }

    /**
     * Loads records from database using the {@link DSLContext}, convert the records to Java POJO's and save them into {@link #entries} field
     * The method uses three helpers to build the query:
     * <ul>
     *     <li>{@link #getTable()}</li>
     *     <li>{@link #getFields()}</li>
     *     <li>{@link #getConsumer()}</li>
     * </ul>
     * @param updateTimestamp refers to the version of Data to Load
     */
    public void load(Timestamp updateTimestamp) {
        Field<Timestamp> updateField = DSL.field(UPDATE_COLUMN, Timestamp.class);
        List<T> list = dslContext
            .select(getFields())
            .from(getTable())
            .where(updateField.equal(updateTimestamp))
            .fetchInto(classType);
        entries = new HashMap<>(list.size());
        list.forEach(getConsumer());
    }

    /**
     * Helper-Method for {@link #load(Timestamp)}
     * @return Any implementing method has to return the table to load from the database
     */
    protected abstract Table<Record> getTable();

    /**
     * Helper-Method for {@link #load(Timestamp)}
     * @return Any implementing method has to return the fields to load from the database
     */
    protected abstract Collection<Field<?>> getFields();

    /**
     * Helper-Method for {@link #load(Timestamp)}
     * @return Any implementing method hast to return a Consumer, which put the loaded records into the entries map
     */
    protected abstract Consumer<T> getConsumer();
    /**
     *
     * @return entries loaded with the load method
     */
    @SuppressWarnings("WeakerAccess")
    public Map<String, T> getEntries() {
        return entries;
    }
}
