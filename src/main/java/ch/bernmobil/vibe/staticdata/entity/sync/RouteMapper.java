package ch.bernmobil.vibe.staticdata.entity.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;

@Entity
public class RouteMapper {
    private static HashMap<String, RouteMapper> mappings = new HashMap<>();

    @Id
    public String gtfsId;
    public Long id;

    private RouteMapper(String gtfsId, Long id) {
        this.gtfsId = gtfsId;
        this.id = id;
    }

    public static void addMapping(String gtfsId, Long id) {
        mappings.put(gtfsId, new RouteMapper(gtfsId, id));
    }

    public static RouteMapper getMappingByStopId(String gtfsId) {
        return mappings.get(gtfsId);
    }
    public static List<RouteMapper> getAll() {
        return new ArrayList<>(mappings.values());
    }
    public static class BatchReader implements ItemReader<RouteMapper> {
        ListItemReader<RouteMapper> reader;

        @Override
        public RouteMapper read() throws Exception{
            if(reader == null) {
                reader = new ListItemReader<>(RouteMapper.getAll());
            }

            return reader.read();
        }
    }
}
