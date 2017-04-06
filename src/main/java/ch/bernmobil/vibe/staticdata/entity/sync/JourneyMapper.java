package ch.bernmobil.vibe.staticdata.entity.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;

@Entity
public class JourneyMapper {
    private static HashMap<String, JourneyMapper> mappings = new HashMap<>();
    private static HashMap<String, String> internalKeyMappings = new HashMap<>();

    @Id
    public String gtfsTripId;
    public String gtfsServiceId;
    public Long id;

    private JourneyMapper(String gtfsTripId, String gtfsServiceId, Long id) {
        this.gtfsTripId = gtfsTripId;
        this.gtfsServiceId = gtfsServiceId;
        this.id = id;
    }

    public static void addMapping(String gtfsTripId, String gtfsServiceId, Long id) {
        internalKeyMappings.put(gtfsServiceId, gtfsTripId);
        mappings.put(gtfsTripId, new JourneyMapper(gtfsTripId, gtfsServiceId, id));
    }

    public static JourneyMapper getMappingByTripId(String gtfsTripId) {
        return mappings.get(gtfsTripId);
    }

    public static JourneyMapper getMappingByServiceId(String gtfsServiceId) {
        return mappings.get(internalKeyMappings.get(gtfsServiceId));
    }
    public static List<JourneyMapper> getAll() {
        return new ArrayList<>(mappings.values());
    }

    public static class BatchReader implements ItemReader<JourneyMapper> {
        ListItemReader<JourneyMapper> reader;

        @Override
        public JourneyMapper read() throws Exception{
            if(reader == null) {
                reader = new ListItemReader<>(JourneyMapper.getAll());
            }

            return reader.read();
        }
    }


}
