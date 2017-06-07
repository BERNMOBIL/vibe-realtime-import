package ch.bernmobil.vibe.realtimedata.repository;

import static java.util.stream.Collectors.toList;
import static org.jooq.impl.DSL.table;

import ch.bernmobil.vibe.shared.contract.JourneyMapperContract;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JourneyMapperRepository extends BaseRepository<JourneyMapping> {
    @Autowired
    public JourneyMapperRepository(@Qualifier("MapperDslContext")DSLContext dslContext) {
        super(JourneyMapping.class, dslContext);
    }

    public Optional<JourneyMapping> findByGtfsTripId(String gtfsTripId) {
        return Optional.ofNullable(getEntries().get(gtfsTripId));
    }

    @Override
    protected Table<Record> getTable() {
        return table(JourneyMapperContract.TABLE_NAME);
    }

    @Override
    protected Collection<Field<?>> getFields() {
        final String[] columnsToFetch = {JourneyMapperContract.GTFS_TRIP_ID,
            JourneyMapperContract.GTFS_SERVICE_ID, JourneyMapperContract.ID};
        return Arrays.stream(columnsToFetch).map(DSL::field).collect(toList());
    }

    @Override
    protected Consumer<JourneyMapping> getConsumer() {
        return journeyMapping -> getEntries().put(journeyMapping.getGtfsTripId(), journeyMapping);
    }
}
