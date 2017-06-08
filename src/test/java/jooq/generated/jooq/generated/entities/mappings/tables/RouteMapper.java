/*
 * This file is generated by jOOQ.
*/
package jooq.generated.entities.mappings.tables;


import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

import jooq.generated.entities.mappings.Keys;
import jooq.generated.entities.mappings.Public;
import jooq.generated.entities.mappings.tables.records.RouteMapperRecord;

import org.jooq.Field;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


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
public class RouteMapper extends TableImpl<RouteMapperRecord> {

    private static final long serialVersionUID = 928947751;

    /**
     * The reference instance of <code>public.route_mapper</code>
     */
    public static final RouteMapper ROUTE_MAPPER = new RouteMapper();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RouteMapperRecord> getRecordType() {
        return RouteMapperRecord.class;
    }

    /**
     * The column <code>public.route_mapper.gtfs_id</code>.
     */
    public final TableField<RouteMapperRecord, String> GTFS_ID = createField("gtfs_id", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.route_mapper.id</code>.
     */
    public final TableField<RouteMapperRecord, UUID> ID = createField("id", org.jooq.impl.SQLDataType.UUID, this, "");

    /**
     * The column <code>public.route_mapper.update</code>.
     */
    public final TableField<RouteMapperRecord, Timestamp> UPDATE = createField("update", org.jooq.impl.SQLDataType.TIMESTAMP, this, "");

    /**
     * Create a <code>public.route_mapper</code> table reference
     */
    public RouteMapper() {
        this("route_mapper", null);
    }

    /**
     * Create an aliased <code>public.route_mapper</code> table reference
     */
    public RouteMapper(String alias) {
        this(alias, ROUTE_MAPPER);
    }

    private RouteMapper(String alias, Table<RouteMapperRecord> aliased) {
        this(alias, aliased, null);
    }

    private RouteMapper(String alias, Table<RouteMapperRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<RouteMapperRecord>> getKeys() {
        return Arrays.<UniqueKey<RouteMapperRecord>>asList(Keys.ROUTE_MAPPER_GTFS_ID_UPDATE_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RouteMapper as(String alias) {
        return new RouteMapper(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public RouteMapper rename(String name) {
        return new RouteMapper(name, null);
    }
}
