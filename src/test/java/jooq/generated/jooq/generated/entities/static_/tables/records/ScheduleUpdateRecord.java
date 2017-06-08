/*
 * This file is generated by jOOQ.
*/
package jooq.generated.entities.static_.tables.records;


import java.sql.Time;
import java.util.UUID;

import javax.annotation.Generated;

import jooq.generated.entities.static_.tables.ScheduleUpdate;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


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
public class ScheduleUpdateRecord extends UpdatableRecordImpl<ScheduleUpdateRecord> implements Record4<UUID, UUID, Time, Time> {

    private static final long serialVersionUID = 250079931;

    /**
     * Setter for <code>public.schedule_update.id</code>.
     */
    public ScheduleUpdateRecord setId(UUID value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.schedule_update.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.schedule_update.schedule</code>.
     */
    public ScheduleUpdateRecord setSchedule(UUID value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.schedule_update.schedule</code>.
     */
    public UUID getSchedule() {
        return (UUID) get(1);
    }

    /**
     * Setter for <code>public.schedule_update.actual_arrival</code>.
     */
    public ScheduleUpdateRecord setActualArrival(Time value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.schedule_update.actual_arrival</code>.
     */
    public Time getActualArrival() {
        return (Time) get(2);
    }

    /**
     * Setter for <code>public.schedule_update.actual_departure</code>.
     */
    public ScheduleUpdateRecord setActualDeparture(Time value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.schedule_update.actual_departure</code>.
     */
    public Time getActualDeparture() {
        return (Time) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<UUID, UUID, Time, Time> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<UUID, UUID, Time, Time> valuesRow() {
        return (Row4) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field1() {
        return ScheduleUpdate.SCHEDULE_UPDATE.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field2() {
        return ScheduleUpdate.SCHEDULE_UPDATE.SCHEDULE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Time> field3() {
        return ScheduleUpdate.SCHEDULE_UPDATE.ACTUAL_ARRIVAL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Time> field4() {
        return ScheduleUpdate.SCHEDULE_UPDATE.ACTUAL_DEPARTURE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value2() {
        return getSchedule();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time value3() {
        return getActualArrival();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time value4() {
        return getActualDeparture();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleUpdateRecord value1(UUID value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleUpdateRecord value2(UUID value) {
        setSchedule(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleUpdateRecord value3(Time value) {
        setActualArrival(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleUpdateRecord value4(Time value) {
        setActualDeparture(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleUpdateRecord values(UUID value1, UUID value2, Time value3, Time value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ScheduleUpdateRecord
     */
    public ScheduleUpdateRecord() {
        super(ScheduleUpdate.SCHEDULE_UPDATE);
    }

    /**
     * Create a detached, initialised ScheduleUpdateRecord
     */
    public ScheduleUpdateRecord(UUID id, UUID schedule, Time actualArrival, Time actualDeparture) {
        super(ScheduleUpdate.SCHEDULE_UPDATE);

        set(0, id);
        set(1, schedule);
        set(2, actualArrival);
        set(3, actualDeparture);
    }
}
