package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.shared.QueryBuilder;
import ch.bernmobil.vibe.shared.QueryBuilder.Predicate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;

import ch.bernmobil.vibe.shared.contract.JourneyMapperContract;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class JourneyMapperRepository extends BaseRepository<JourneyMapping> {

    @Autowired
    public JourneyMapperRepository(@Qualifier("MapperDataSource") DataSource mapperDataSource) {
        super(mapperDataSource, new JourneyMappingRowMapper());
    }

    public Optional<JourneyMapping> findByGtfsTripId(String gtfsTripId) {
        return Optional.ofNullable(mappings.get(gtfsTripId));
    }

    public void load(Timestamp updateTimestamp) {
        String query = new QueryBuilder()
            .select(JourneyMapperContract.TABLE_NAME)
            .where(Predicate.equals(JourneyMapperContract.UPDATE, String.format("'%s'", updateTimestamp)))
            .getQuery();
        super.load(query, journeyMapping -> mappings.put(journeyMapping.getGtfsTripId(), journeyMapping));
    }

    private static class JourneyMappingRowMapper implements RowMapper<JourneyMapping> {

        @Override
        public JourneyMapping mapRow(ResultSet rs, int rowNum) throws SQLException {
            String gtfsTripId = rs.getString(JourneyMapperContract.GTFS_TRIP_ID);
            String gtfsServiceId = rs.getString(JourneyMapperContract.GTFS_SERVICE_ID);
            UUID id = rs.getObject(JourneyMapperContract.ID, UUID.class);
            return new JourneyMapping(gtfsTripId, gtfsServiceId, id);
        }
    }
}
