package ch.bernmobil.vibe.staticdata.entity.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;

@Entity
public class CalendarDateMapper {
    private static HashMap<Long, CalendarDateMapper> mappings = new HashMap<>();

    @Id
    public Long gtfsId;
    public Long id;

    private CalendarDateMapper(Long gtfsId, Long id) {
        this.gtfsId = gtfsId;
        this.id = id;
    }

    public static void addMapping(Long gtfsId, Long id) {
        mappings.put(gtfsId, new CalendarDateMapper(gtfsId, id));
    }

    public static CalendarDateMapper getMappingByCalendarId(Long gtfsId) {
        return mappings.get(gtfsId);
    }
    public static List<CalendarDateMapper> getAll() {
        return new ArrayList<>(mappings.values());
    }
    public static class BatchReader implements ItemReader<CalendarDateMapper> {
        ListItemReader<CalendarDateMapper> reader;

        @Override
        public CalendarDateMapper read() throws Exception{
            if(reader == null) {
                reader = new ListItemReader<>(CalendarDateMapper.getAll());
            }

            return reader.read();
        }
    }

}
