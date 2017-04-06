package ch.bernmobil.vibe.realtimedata.entity.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;

@Entity
public class CalendarDateMapper {
    @Id
    private Long gtfsId;
    private Long id;

    public Long getGtfsId() {
        return gtfsId;
    }

    public Long getId() {
        return id;
    }
}
