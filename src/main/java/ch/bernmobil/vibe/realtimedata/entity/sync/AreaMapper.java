package ch.bernmobil.vibe.realtimedata.entity.sync;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AreaMapper {

    @Id
    public String gtfsId;
    public Long id;

    public String getGtfsId() {
        return gtfsId;
    }

    public Long getId() {
        return id;
    }
}
