package ch.bernmobil.vibe.realtimedata.entity;

import java.sql.Time;

public class ScheduleUpdate {
    private Time actualArrival;
    private Time actualDeparture;
    private long schedule;

    public void setActualArrival(Time actualArrival) {
        this.actualArrival = actualArrival;
    }

    public void setActualDeparture(Time actualDeparture) {
        this.actualDeparture = actualDeparture;
    }

    public void setSchedule(long schedule) { this.schedule = schedule; }

    public Time getActualArrival() {
        return actualArrival;
    }

    public Time getActualDeparture() {
        return actualDeparture;
    }

    public long getSchedule() {
        return schedule;
    }
}
