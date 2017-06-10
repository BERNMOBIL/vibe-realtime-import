package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.shared.contract.StopMapperContract;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;
import static org.jooq.impl.DSL.table;

/**
 * Database-Repository for accessing the {@link StopMapping}'s information's created on Static-Update.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Component
public class StopMapperRepository extends BaseRepository<StopMapping> {
    /**
     * Constructs an instance using a {@link DSLContext}
     * @param dslContext Object of the JOOQ Query Builder to access the database
     */
    @Autowired
    public StopMapperRepository(@Qualifier("MapperDslContext")DSLContext dslContext) {
        super(StopMapping.class, dslContext);
    }

    /**
     * Search a already loaded {@link StopMapping} by it's GTFS-Id
     * @param gtfsId search-criteria
     * @return May containing a found {@link StopMapping}
     */
    public Optional<StopMapping> findByGtfsId(String gtfsId) {
        return Optional.ofNullable(getEntries().get(gtfsId));
    }

    /**
     * Hook for the {@link #load(Timestamp)}-Method using the Template Method Pattern
     * @return Table used in a query executed with a Jooq {@link DSLContext}
     */
    @Override
    protected Table<Record> getTable() {
        return table(StopMapperContract.TABLE_NAME);
    }

    /**
     * Hook for the {@link #load(Timestamp)}-Method using the Template Method Pattern
     * @return Fields used in a query executed with a Jooq {@link DSLContext}
     */
    @Override
    protected Collection<Field<Object>> getFields() {
        final String[] columnsToFetch = {StopMapperContract.GTFS_ID, StopMapperContract.ID};
        return Arrays.stream(columnsToFetch).map(DSL::field).collect(toList());
    }

    /**
     * Hook for the {@link #load(Timestamp)}-Method using the Template Method Pattern
     * @return {@link Consumer} used to save the loaded {@link StopMapping}s from the {@link #load(Timestamp)}-Method
     */
    protected Consumer<StopMapping> getConsumer() {
        return stopMapping -> getEntries().put(stopMapping.getGtfsId(), stopMapping);
    }
}
