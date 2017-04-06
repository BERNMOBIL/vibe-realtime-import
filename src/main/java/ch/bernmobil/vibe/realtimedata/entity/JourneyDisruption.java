package ch.bernmobil.vibe.realtimedata.entity;

import ch.bernmobil.vibe.staticdata.entity.Journey;
import java.sql.Timestamp;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "trip_update")
public class JourneyDisruption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private Timestamp start;
    private Timestamp plannedEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getPlannedEnd() {
        return plannedEnd;
    }

    public void setPlannedEnd(Timestamp plannedEnd) {
        this.plannedEnd = plannedEnd;
    }

}
