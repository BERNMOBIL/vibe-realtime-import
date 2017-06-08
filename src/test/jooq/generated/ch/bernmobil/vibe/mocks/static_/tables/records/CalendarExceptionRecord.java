/*
 * This file is generated by jOOQ.
*/
package ch.bernmobil.vibe.mocks.static_.tables.records;


import ch.bernmobil.vibe.mocks.static_.tables.CalendarException;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
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
public class CalendarExceptionRecord extends UpdatableRecordImpl<CalendarExceptionRecord> implements Record5<UUID, Date, BigDecimal, UUID, Timestamp> {

    private static final long serialVersionUID = 843221913;

    /**
     * Setter for <code>public.calendar_exception.id</code>.
     */
    public CalendarExceptionRecord setId(UUID value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.calendar_exception.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.calendar_exception.date</code>.
     */
    public CalendarExceptionRecord setDate(Date value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.calendar_exception.date</code>.
     */
    public Date getDate() {
        return (Date) get(1);
    }

    /**
     * Setter for <code>public.calendar_exception.type</code>.
     */
    public CalendarExceptionRecord setType(BigDecimal value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.calendar_exception.type</code>.
     */
    public BigDecimal getType() {
        return (BigDecimal) get(2);
    }

    /**
     * Setter for <code>public.calendar_exception.calendar_date</code>.
     */
    public CalendarExceptionRecord setCalendarDate(UUID value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.calendar_exception.calendar_date</code>.
     */
    public UUID getCalendarDate() {
        return (UUID) get(3);
    }

    /**
     * Setter for <code>public.calendar_exception.update</code>.
     */
    public CalendarExceptionRecord setUpdate(Timestamp value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.calendar_exception.update</code>.
     */
    public Timestamp getUpdate() {
        return (Timestamp) get(4);
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
    // Record5 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<UUID, Date, BigDecimal, UUID, Timestamp> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<UUID, Date, BigDecimal, UUID, Timestamp> valuesRow() {
        return (Row5) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field1() {
        return CalendarException.CALENDAR_EXCEPTION.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field2() {
        return CalendarException.CALENDAR_EXCEPTION.DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field3() {
        return CalendarException.CALENDAR_EXCEPTION.TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field4() {
        return CalendarException.CALENDAR_EXCEPTION.CALENDAR_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field5() {
        return CalendarException.CALENDAR_EXCEPTION.UPDATE;
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
    public Date value2() {
        return getDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value3() {
        return getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value4() {
        return getCalendarDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value5() {
        return getUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarExceptionRecord value1(UUID value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarExceptionRecord value2(Date value) {
        setDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarExceptionRecord value3(BigDecimal value) {
        setType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarExceptionRecord value4(UUID value) {
        setCalendarDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarExceptionRecord value5(Timestamp value) {
        setUpdate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarExceptionRecord values(UUID value1, Date value2, BigDecimal value3, UUID value4, Timestamp value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached CalendarExceptionRecord
     */
    public CalendarExceptionRecord() {
        super(CalendarException.CALENDAR_EXCEPTION);
    }

    /**
     * Create a detached, initialised CalendarExceptionRecord
     */
    public CalendarExceptionRecord(UUID id, Date date, BigDecimal type, UUID calendarDate, Timestamp update) {
        super(CalendarException.CALENDAR_EXCEPTION);

        set(0, id);
        set(1, date);
        set(2, type);
        set(3, calendarDate);
        set(4, update);
    }
}
