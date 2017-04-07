package ch.bernmobil.vibe.realtimedata.entity;

import java.sql.Timestamp;

public class JourneyDisruption {
    private String message;
    private Timestamp start;
    private Timestamp plannedEnd;

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
