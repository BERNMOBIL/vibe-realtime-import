package ch.bernmobil.vibe.realtimedata.entity;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class ScheduleUpdate {
    private Time actualArrival;
    private Time actualDeparture;
    private UUID schedule;

    public ScheduleUpdate(Time actualArrival, Time actualDeparture, UUID schedule) {
        this.actualArrival = actualArrival;
        this.actualDeparture = actualDeparture;
        this.schedule = schedule;
    }

    public void setSchedule(UUID schedule) { this.schedule = schedule; }

    public Time getActualArrival() {
        return actualArrival;
    }

    public Time getActualDeparture() {
        return actualDeparture;
    }

    public UUID getSchedule() {
        return schedule;
    }

    public static ScheduleUpdate convert(ScheduleUpdateInformation updateInformation) {
        return new ScheduleUpdate(
            updateInformation.getActualArrival(),
            updateInformation.getActualDeparture(),
            updateInformation.getScheduleId()
        );
    }
}
