/*
 * This file is generated by jOOQ.
*/
package jooq.generated.entities.mappings.tables.records;


import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.Generated;

import jooq.generated.entities.mappings.tables.RouteMapper;

import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.TableRecordImpl;


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
public class RouteMapperRecord extends TableRecordImpl<RouteMapperRecord> implements Record3<String, UUID, Timestamp> {

    private static final long serialVersionUID = -1792239319;

    /**
     * Setter for <code>public.route_mapper.gtfs_id</code>.
     */
    public RouteMapperRecord setGtfsId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.route_mapper.gtfs_id</code>.
     */
    public String getGtfsId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.route_mapper.id</code>.
     */
    public RouteMapperRecord setId(UUID value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.route_mapper.id</code>.
     */
    public UUID getId() {
        return (UUID) get(1);
    }

    /**
     * Setter for <code>public.route_mapper.update</code>.
     */
    public RouteMapperRecord setUpdate(Timestamp value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.route_mapper.update</code>.
     */
    public Timestamp getUpdate() {
        return (Timestamp) get(2);
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<String, UUID, Timestamp> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<String, UUID, Timestamp> valuesRow() {
        return (Row3) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return RouteMapper.ROUTE_MAPPER.GTFS_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field2() {
        return RouteMapper.ROUTE_MAPPER.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field3() {
        return RouteMapper.ROUTE_MAPPER.UPDATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getGtfsId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value2() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value3() {
        return getUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RouteMapperRecord value1(String value) {
        setGtfsId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RouteMapperRecord value2(UUID value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RouteMapperRecord value3(Timestamp value) {
        setUpdate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RouteMapperRecord values(String value1, UUID value2, Timestamp value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RouteMapperRecord
     */
    public RouteMapperRecord() {
        super(RouteMapper.ROUTE_MAPPER);
    }

    /**
     * Create a detached, initialised RouteMapperRecord
     */
    public RouteMapperRecord(String gtfsId, UUID id, Timestamp update) {
        super(RouteMapper.ROUTE_MAPPER);

        set(0, gtfsId);
        set(1, id);
        set(2, update);
    }
}
