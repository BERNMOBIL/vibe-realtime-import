package ch.bernmobil.vibe.staticdata.entity.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;

@Entity
public class StopMapper {
    private static HashMap<String, StopMapper> mappings = new HashMap<>();

    @Id
    public String gtfsId;
    public Long id;

    private StopMapper(String gtfsId, Long id) {
        this.gtfsId = gtfsId;
        this.id = id;
    }

    public static void addMapping(String gtfsId, Long id) {
        mappings.put(gtfsId, new StopMapper(gtfsId, id));
    }

    public static StopMapper getMappingByStopId(String gtfsId) {
        return mappings.get(gtfsId);
    }

    public static List<StopMapper> getAll() {
        return new ArrayList<>(mappings.values());
    }
    public static class BatchReader implements ItemReader<StopMapper> {
        ListItemReader<StopMapper> reader;

        @Override
        public StopMapper read() throws Exception{
            if(reader == null) {
                reader = new ListItemReader<>(StopMapper.getAll());
            }

            return reader.read();
        }
    }
}
