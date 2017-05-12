package ch.bernmobil.vibe.realtimedata.entity;

import java.util.UUID;

public class Schedule {
    private UUID id;
    private UUID journeyId;
    private UUID stopId;

    public Schedule(UUID id, UUID journeyId, UUID stopId) {
        this.id = id;
        this.journeyId = journeyId;
        this.stopId = stopId;
    }

    public UUID getJourneyId() {
        return journeyId;
    }
    public UUID getStopId() {
        return stopId;
    }
    public UUID getId() { return id; }
}
