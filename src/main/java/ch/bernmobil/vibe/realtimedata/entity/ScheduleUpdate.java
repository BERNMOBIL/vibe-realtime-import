package ch.bernmobil.vibe.realtimedata.entity;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

public class ScheduleUpdate {
    private Time actualArrival;
    private Time actualDeparture;
    private long schedule;

    public ScheduleUpdate(Time actualArrival, Time actualDeparture, long schedule) {
        this.actualArrival = actualArrival;
        this.actualDeparture = actualDeparture;
        this.schedule = schedule;
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
