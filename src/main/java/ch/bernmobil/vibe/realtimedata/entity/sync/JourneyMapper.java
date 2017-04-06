package ch.bernmobil.vibe.realtimedata.entity.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;

@Entity
public class JourneyMapper {
    @Id
    private Long id;
    private String gtfsTripId;
    private String gtfsServiceId;

    public Long getId() {
        return id;
    }

    public String getGtfsTripId() {
        return gtfsTripId;
    }

    public String getGtfsServiceId() {
        return gtfsServiceId;
    }
}
