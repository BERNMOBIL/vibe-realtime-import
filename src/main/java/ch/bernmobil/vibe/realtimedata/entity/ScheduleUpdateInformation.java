package ch.bernmobil.vibe.realtimedata.entity;

import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;
import java.util.UUID;

public class ScheduleUpdateInformation {
    private StopTimeUpdate stopTimeUpdate;
    private UUID journeyId;
    private UUID stopId;
    private UUID scheduleId;

    public ScheduleUpdateInformation(StopTimeUpdate stopTimeUpdate, UUID journeyId, UUID stopId) {
        this.stopTimeUpdate = stopTimeUpdate;
        this.journeyId = journeyId;
        this.stopId = stopId;
    }

    public StopTimeUpdate getStopTimeUpdate() {
        return stopTimeUpdate;
    }

    public void setStopTimeUpdate(StopTimeUpdate stopTimeUpdate) {
        this.stopTimeUpdate = stopTimeUpdate;
    }

    public UUID getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(UUID journeyId) {
        this.journeyId = journeyId;
    }

    public UUID getStopId() {
        return stopId;
    }

    public void setStopId(UUID stopId) {
        this.stopId = stopId;
    }

    public UUID getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(UUID scheduleId) {
        this.scheduleId = scheduleId;
    }
}
