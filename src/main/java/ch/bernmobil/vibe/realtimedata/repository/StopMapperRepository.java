package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.shared.QueryBuilder;
import ch.bernmobil.vibe.shared.QueryBuilder.Predicate;
import ch.bernmobil.vibe.shared.UpdateManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;

import ch.bernmobil.vibe.shared.contract.StopMapperContract;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class StopMapperRepository {
    private HashMap<String, StopMapping> mappings;
    private static JdbcTemplate jdbcTemplate;
    private UpdateManager updateManager;

    public Optional<StopMapping> findByGtfsId(String gtfsId) {
        return Optional.ofNullable(mappings.get(gtfsId));
    }

    @Autowired
    public StopMapperRepository(@Qualifier("MapperDataSource") DataSource mapperDataSource,
        UpdateManager updateManager) {
        jdbcTemplate = new JdbcTemplate(mapperDataSource);
        this.updateManager = updateManager;
    }

    public void load(Timestamp updateTimestamp) {
        String query = new QueryBuilder()
            .select(StopMapperContract.TABLE_NAME)
            .where(Predicate.equals(StopMapperContract.UPDATE, String.format("'%s'", updateTimestamp)))
            .getQuery();

        List<StopMapping> list = jdbcTemplate.query(query, new StopMappingRowMapper());
        mappings = new HashMap<>(list.size());
        list.forEach(stopMapping -> mappings.put(stopMapping.getGtfsId(), stopMapping));
    }

    private static class StopMappingRowMapper implements RowMapper<StopMapping> {

        @Override
        public StopMapping mapRow(ResultSet rs, int rowNum) throws SQLException {
            String gtfsId = rs.getString(StopMapperContract.GTFS_ID);
            UUID id = rs.getObject(StopMapperContract.ID, UUID.class);
            return new StopMapping(gtfsId, id);
        }
    }

}
