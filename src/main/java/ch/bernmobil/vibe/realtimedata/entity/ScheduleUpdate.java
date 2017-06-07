package ch.bernmobil.vibe.realtimedata.entity;

import java.sql.Time;
import java.util.UUID;

public class ScheduleUpdate {

    private UUID id;
    private Time actualArrival;
    private Time actualDeparture;
    private UUID schedule;

    public ScheduleUpdate(Time actualArrival, Time actualDeparture, UUID schedule) {
        this.actualArrival = actualArrival;
        this.actualDeparture = actualDeparture;
        this.schedule = schedule;
    }

    public ScheduleUpdate(UUID id, Time actualArrival, Time actualDeparture, UUID schedule) {
        this(actualArrival, actualDeparture, schedule);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public UUID getSchedule() {
        return schedule;
    }

    public void setSchedule(UUID schedule) {
        this.schedule = schedule;
    }


    public static ScheduleUpdate convert(ScheduleUpdateInformation updateInformation) {
        return new ScheduleUpdate(
            updateInformation.getActualArrival(),
            updateInformation.getActualDeparture(),
            updateInformation.getScheduleId()
        );
    }
}
