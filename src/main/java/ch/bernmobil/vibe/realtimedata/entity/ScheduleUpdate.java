package ch.bernmobil.vibe.realtimedata.entity;

import java.sql.Time;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ScheduleUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Time actualArrival;
    private Time actualDeparture;

    public Time getActualArrival() {
        return actualArrival;
    }

    public void setActualArrival(Time actualArrival) {
        this.actualArrival = actualArrival;
    }

    public Time getActualDeparture() {
        return actualDeparture;
    }

    public void setActualDeparture(Time actualDeparture) {
        this.actualDeparture = actualDeparture;
    }

    public long getId() {
        return id;
    }
}
