/*
 * This file is generated by jOOQ.
*/
package ch.bernmobil.vibe.mocks.static_.tables.pojos;


import java.io.Serializable;
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
public class Stop implements Serializable {

    private static final long serialVersionUID = -515035924;

    private final UUID      id;
    private final String    name;
    private final UUID      area;
    private final Timestamp update;

    public Stop(Stop value) {
        this.id = value.id;
        this.name = value.name;
        this.area = value.area;
        this.update = value.update;
    }

    public Stop(
        UUID      id,
        String    name,
        UUID      area,
        Timestamp update
    ) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.update = update;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public UUID getArea() {
        return this.area;
    }

    public Timestamp getUpdate() {
        return this.update;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Stop (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(area);
        sb.append(", ").append(update);

        sb.append(")");
        return sb.toString();
    }
}
