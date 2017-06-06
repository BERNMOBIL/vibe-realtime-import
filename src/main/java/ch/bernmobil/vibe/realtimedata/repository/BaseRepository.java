package ch.bernmobil.vibe.realtimedata.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public abstract class BaseRepository<T> {
    private Map<String, T> mappings;
    private JdbcTemplate jdbcTemplate;
    private RowMapper<T> rowMapper;

    public BaseRepository(DataSource dataSource, RowMapper<T> rowMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.rowMapper = rowMapper;
    }

    /**
     * Execute query, collect and save results in the mappings field with help of the consumer.
     * @param query Database-Query to execute to collect data from Database
     * @param forEachConsumer Consumer which is executed on each collected row.
     */
    public void load(String query, Consumer<T> forEachConsumer) {
        List<T> list = jdbcTemplate.query(query, rowMapper);
        mappings = new HashMap<>(list.size());
        list.forEach(forEachConsumer);
    }

    /**
     *
     * @return mappings loaded with the load method
     */
    public Map<String, T> getMappings() {
        return mappings;
    }
}
