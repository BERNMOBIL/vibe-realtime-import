package ch.bernmobil.vibe.realtimedata.entity;

public class Schedule {
    private int id;
    private int journeyId;
    private int stopId;

    public Schedule(int id, int journeyId, int stopId) {
        this.id = id;
        this.journeyId = journeyId;
        this.stopId = stopId;
    }

    public long getJourneyId() {
        return journeyId;
    }
    public long getStopId() {
        return stopId;
    }
    public int getId() { return id; }
}
