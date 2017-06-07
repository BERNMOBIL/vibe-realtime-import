package ch.bernmobil.vibe.realtimedata.repository;

import static java.util.stream.Collectors.toList;
import static org.jooq.impl.DSL.table;

import ch.bernmobil.vibe.shared.contract.StopMapperContract;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
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
public class StopMapperRepository extends BaseRepository<StopMapping> {

    @Autowired
    public StopMapperRepository(@Qualifier("MapperDslContext")DSLContext dslContext) {
        super(StopMapping.class, dslContext);
    }

    public Optional<StopMapping> findByGtfsId(String gtfsId) {
        return Optional.ofNullable(getMappings().get(gtfsId));
    }

    @Override
    protected Table<Record> getTable() {
        return table(StopMapperContract.TABLE_NAME);
    }

    @Override
    protected Collection<Field<?>> getFields() {
        final String[] columnsToFetch = {StopMapperContract.GTFS_ID, StopMapperContract.ID};
        return Arrays.stream(columnsToFetch).map(DSL::field).collect(toList());
    }

    protected Consumer<StopMapping> getConsumer() {
        return stopMapping -> getMappings().put(stopMapping.getGtfsId(), stopMapping);
    }
}
