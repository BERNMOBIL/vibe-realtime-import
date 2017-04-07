package ch.bernmobil.vibe.realtimedata.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

public class StopMapper {
    private static HashMap<String, Integer> mappings = new HashMap<>();

    public static Integer getIdByGtfsId(String gtfsId) {
        return mappings.get(gtfsId);
    }

    public static void loadMappings(JdbcTemplate template) {
        String sql = "SELECT * FROM stop_mapper";

        List<Map<String, Object>> rows = template.queryForList(sql);

        for (Map row : rows) {
            mappings.put((String)row.get("gtfs_id"), (Integer)row.get("id"));
        }
    }
}
