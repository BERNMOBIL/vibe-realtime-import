package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.shared.contract.JourneyMapperContract;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
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
 * Database repository for accessing the {@link JourneyMapping}'s information created on Static-Update.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
@Component
public class JourneyMapperRepository extends BaseRepository<JourneyMapping> {
    /**
     * Constructs an instance using a {@link DSLContext}
     * @param dslContext Object of the JOOQ Query Builder to access the database
     */
    @Autowired
    public JourneyMapperRepository(@Qualifier("MapperDslContext")DSLContext dslContext) {
        super(JourneyMapping.class, dslContext);
    }

    /**
     * Search an already loaded {@link JourneyMapping} by its GTFS-Trip-Id
     * @param gtfsTripId search-criteria
     * @return {@link Optional} which may contain a {@link JourneyMapping}
     */
    public Optional<JourneyMapping> findByGtfsTripId(String gtfsTripId) {
        return Optional.ofNullable(getEntries().get(gtfsTripId));
    }

    /**
     * Hook for the {@link #load(Timestamp)}-Method using the Template Method Pattern
     * @return Table used in a query executed with a Jooq {@link DSLContext}
     */
    @Override
    protected Table<Record> getTable() {
        return table(JourneyMapperContract.TABLE_NAME);
    }

    /**
     * Hook for the {@link #load(Timestamp)}-Method using the Template Method Pattern
     * @return Fields used in a query executed with a Jooq {@link DSLContext}
     */
    @Override
    protected Collection<Field<Object>> getFields() {
        final String[] columnsToFetch = {JourneyMapperContract.GTFS_TRIP_ID,
            JourneyMapperContract.GTFS_SERVICE_ID, JourneyMapperContract.ID};
        return Arrays.stream(columnsToFetch).map(DSL::field).collect(toList());
    }

    /**
     * Hook for {@link #load(Timestamp)} using the "Template Method Pattern".
     * @return {@link Consumer} used to save the loaded {@link JourneyMapping}s from {@link #load(Timestamp)},
     */
    @Override
    protected Consumer<JourneyMapping> getConsumer() {
        return journeyMapping -> getEntries().put(journeyMapping.getGtfsTripId(), journeyMapping);
    }
}
