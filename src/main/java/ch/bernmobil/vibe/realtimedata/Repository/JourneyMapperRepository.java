package ch.bernmobil.vibe.realtimedata.Repository;

import ch.bernmobil.vibe.realtimedata.QueryBuilder;
import ch.bernmobil.vibe.realtimedata.QueryBuilder.Predicate;
import ch.bernmobil.vibe.realtimedata.UpdateManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JourneyMapperRepository {
    private HashMap<String, Integer> mappings = new HashMap<>();
    private static JdbcTemplate jdbcTemplate;
    private UpdateManager updateManager;

    public Integer getIdByGtfsTripId(String gtfsTripId) {
        return mappings.get(gtfsTripId);
    }

    @Autowired
    public JourneyMapperRepository(@Qualifier("MapperDataSource") DataSource mapperDataSource) {
        jdbcTemplate = new JdbcTemplate(mapperDataSource);
        updateManager = new UpdateManager(mapperDataSource);
        load();
    }

    private void load() {
        String query = new QueryBuilder()
            .Select("journey_mapper")
            .Where(Predicate.equals("update", "'" + updateManager.getLatestUpdateTimestamp() + "'"))
            .getQuery();


        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        for (Map row : rows) {
            mappings.put((String)row.get("gtfs_trip_id"), (Integer)row.get("id"));
        }
    }
}