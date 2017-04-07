package ch.bernmobil.vibe.realtimedata.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

public class JourneyMapper {
    private static HashMap<String, Integer> mappings = new HashMap<>();

    public static Integer getIdByGtfsTripId(String gtfsTripId) {
        return mappings.get(gtfsTripId);
    }

    public static void loadMappings(JdbcTemplate template) {
        String sql = "SELECT * FROM journey_mapper";

        List<Map<String, Object>> rows = template.queryForList(sql);

        for (Map row : rows) {
            mappings.put((String)row.get("gtfs_trip_id"), (Integer)row.get("id"));
        }
    }
}
