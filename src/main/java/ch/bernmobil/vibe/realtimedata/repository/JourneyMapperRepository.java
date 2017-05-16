package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.QueryBuilder;
import ch.bernmobil.vibe.realtimedata.QueryBuilder.Predicate;
import ch.bernmobil.vibe.realtimedata.UpdateManager;
import ch.bernmobil.vibe.realtimedata.entity.UpdateHistory;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;

import ch.bernmobil.vibe.realtimedata.contract.JourneyMapperContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JourneyMapperRepository {
    private HashMap<String, UUID> mappings;
    private static JdbcTemplate jdbcTemplate;
    private UpdateManager updateManager;

    public Optional<UUID> getIdByGtfsTripId(String gtfsTripId) {
        return Optional.ofNullable(mappings.get(gtfsTripId));
    }

    @Autowired
    public JourneyMapperRepository(@Qualifier("MapperDataSource") DataSource mapperDataSource,
        UpdateManager updateManager) {
        jdbcTemplate = new JdbcTemplate(mapperDataSource);
        this.updateManager = updateManager;
    }

    public void load(Timestamp updateTimestamp) {
        String query = new QueryBuilder()
            .select(JourneyMapperContract.TABLE_NAME)
            .where(Predicate.equals(JourneyMapperContract.UPDATE, String.format("'%s'", updateTimestamp)))
            .getQuery();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        mappings = new HashMap<>(rows.size());
        for (Map row : rows) {
            mappings.put((String)row.get(JourneyMapperContract.GTFS_TRIP_ID), (UUID)row.get(JourneyMapperContract.ID));
        }
    }
}
