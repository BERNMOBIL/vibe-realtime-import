package ch.bernmobil.vibe.realtimedata.entity;

import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;

public class ScheduleUpdateInformation {
    private StopTimeUpdate stopTimeUpdate;
    private int journeyId;
    private int stopId;
    private int scheduleId;

    public ScheduleUpdateInformation(StopTimeUpdate stopTimeUpdate, int journeyId, int stopId) {
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

    public int getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(int journeyId) {
        this.journeyId = journeyId;
    }

    public int getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }
}
