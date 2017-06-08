/*
 * This file is generated by jOOQ.
*/
package jooq.generated.entities.static_.tables.pojos;


import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.2"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Schedule implements Serializable {

    private static final long serialVersionUID = -1133652424;

    private final UUID      id;
    private final String    platform;
    private final Time      plannedArrival;
    private final Time      plannedDeparture;
    private final UUID      stop;
    private final UUID      journey;
    private final Timestamp update;

    public Schedule(Schedule value) {
        this.id = value.id;
        this.platform = value.platform;
        this.plannedArrival = value.plannedArrival;
        this.plannedDeparture = value.plannedDeparture;
        this.stop = value.stop;
        this.journey = value.journey;
        this.update = value.update;
    }

    public Schedule(
        UUID      id,
        String    platform,
        Time      plannedArrival,
        Time      plannedDeparture,
        UUID      stop,
        UUID      journey,
        Timestamp update
    ) {
        this.id = id;
        this.platform = platform;
        this.plannedArrival = plannedArrival;
        this.plannedDeparture = plannedDeparture;
        this.stop = stop;
        this.journey = journey;
        this.update = update;
    }

    public UUID getId() {
        return this.id;
    }

    public String getPlatform() {
        return this.platform;
    }

    public Time getPlannedArrival() {
        return this.plannedArrival;
    }

    public Time getPlannedDeparture() {
        return this.plannedDeparture;
    }

    public UUID getStop() {
        return this.stop;
    }

    public UUID getJourney() {
        return this.journey;
    }

    public Timestamp getUpdate() {
        return this.update;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Schedule (");

        sb.append(id);
        sb.append(", ").append(platform);
        sb.append(", ").append(plannedArrival);
        sb.append(", ").append(plannedDeparture);
        sb.append(", ").append(stop);
        sb.append(", ").append(journey);
        sb.append(", ").append(update);

        sb.append(")");
        return sb.toString();
    }
}
