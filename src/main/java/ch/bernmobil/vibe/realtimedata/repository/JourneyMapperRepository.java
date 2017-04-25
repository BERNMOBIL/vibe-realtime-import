package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.QueryBuilder;
import ch.bernmobil.vibe.realtimedata.QueryBuilder.Predicate;
import ch.bernmobil.vibe.realtimedata.UpdateManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;

import ch.bernmobil.vibe.realtimedata.contract.JourneyMapperContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JourneyMapperRepository {
    private HashMap<String, Integer> mappings = new HashMap<>();
    private static JdbcTemplate jdbcTemplate;
    private UpdateManager updateManager;

    public Optional<Integer> getIdByGtfsTripId(String gtfsTripId) {
        return Optional.ofNullable(mappings.get(gtfsTripId));
    }

    @Autowired
    public JourneyMapperRepository(@Qualifier("MapperDataSource") DataSource mapperDataSource, UpdateManager updateManager) {
        jdbcTemplate = new JdbcTemplate(mapperDataSource);
        this.updateManager = updateManager;
    }

    public void load() {
        updateManager.loadUpdateHistory();
        String query = new QueryBuilder()
            .select(JourneyMapperContract.TABLE_NAME)
            .where(Predicate.equals(JourneyMapperContract.UPDATE, String.format("'%s'", UpdateManager.getLatestUpdateTimestamp())))
            .getQuery();

        for (Map row : jdbcTemplate.queryForList(query)) {
            mappings.put((String)row.get(JourneyMapperContract.GTFS_TRIP_ID), (Integer)row.get(JourneyMapperContract.ID));
        }
    }
}
