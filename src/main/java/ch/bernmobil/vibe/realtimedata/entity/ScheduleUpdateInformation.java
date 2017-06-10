package ch.bernmobil.vibe.realtimedata.entity;

import ch.bernmobil.vibe.shared.entity.ScheduleUpdate;
import java.sql.Time;
import java.util.UUID;

public class ScheduleUpdateInformation {

    private Time actualArrival;
    private Time actualDeparture;
    private UUID journeyId;
    private UUID stopId;
    private UUID scheduleId;

    public ScheduleUpdateInformation(Time actualArrival, Time actualDeparture, UUID journeyId, UUID stopId) {
        this.actualArrival = actualArrival;
        this.actualDeparture = actualDeparture;
        this.journeyId = journeyId;
        this.stopId = stopId;
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

    /**
     * Converts a {@link ScheduleUpdateInformation} object to a {@link ScheduleUpdate} object
     * <p>Notice: The converted {@link ScheduleUpdate} is ready to save</p>
     * @return Conversion from {@link ScheduleUpdateInformation} to {@link ScheduleUpdate}
     */
    public ScheduleUpdate convert() {
        return new ScheduleUpdate(getActualArrival(), getActualDeparture(), getScheduleId());
    }
}
