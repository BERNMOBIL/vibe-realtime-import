package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.QueryBuilder;
import ch.bernmobil.vibe.realtimedata.QueryBuilder.Predicate;
import ch.bernmobil.vibe.realtimedata.UpdateManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;

import ch.bernmobil.vibe.realtimedata.contract.StopMapperContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class StopMapperRepository {
    private HashMap<String, UUID> mappings;
    private static JdbcTemplate jdbcTemplate;
    private UpdateManager updateManager;

    public Optional<UUID> getIdByGtfsId(String gtfsId) {
        return Optional.ofNullable(mappings.get(gtfsId));
    }

    @Autowired
    public StopMapperRepository(@Qualifier("MapperDataSource")DataSource mapperDataSource, UpdateManager updateManager) {
        jdbcTemplate = new JdbcTemplate(mapperDataSource);
        this.updateManager = updateManager;

    }

    public void load() {
        String query = new QueryBuilder()
            .select(StopMapperContract.TABLE_NAME)
            .where(Predicate.equals(StopMapperContract.UPDATE, String.format("'%s'", UpdateManager.getLatestUpdateTimestamp())))
            .getQuery();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        mappings = new HashMap<>(rows.size());
        for (Map row : rows) {
            mappings.put((String)row.get(StopMapperContract.GTFS_ID), (UUID)row.get(StopMapperContract.ID));
        }
    }


}
