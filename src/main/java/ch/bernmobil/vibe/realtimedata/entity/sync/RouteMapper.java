package ch.bernmobil.vibe.realtimedata.entity.sync;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RouteMapper {

    @Id
    private String gtfsId;
    private Long id;

    public String getGtfsId() {
        return gtfsId;
    }

    public Long getId() {
        return id;
    }
}
