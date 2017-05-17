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

    //TODO: beautify
    public static ScheduleUpdate convert(ScheduleUpdateInformation updateInformation) {
        return new ScheduleUpdate(
            parseUpdateTime(updateInformation.getStopTimeUpdate().getArrival().getTime()),
            parseUpdateTime(updateInformation.getStopTimeUpdate().getDeparture().getTime()),
            updateInformation.getScheduleId()
        );
    }

    private static Time parseUpdateTime(Long timestamp) {
        return timestamp == 0 ? null : Time.valueOf(
            LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()).toLocalTime());
    }
}
