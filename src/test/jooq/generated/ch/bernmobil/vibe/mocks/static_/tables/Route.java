/*
 * This file is generated by jOOQ.
*/
package ch.bernmobil.vibe.mocks.static_.tables;


import ch.bernmobil.vibe.mocks.static_.Keys;
import ch.bernmobil.vibe.mocks.static_.Public;
import ch.bernmobil.vibe.mocks.static_.tables.records.RouteRecord;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

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
public class Route extends TableImpl<RouteRecord> {

    private static final long serialVersionUID = -1580676643;

    /**
     * The reference instance of <code>public.route</code>
     */
    public static final Route ROUTE = new Route();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RouteRecord> getRecordType() {
        return RouteRecord.class;
    }

    /**
     * The column <code>public.route.id</code>.
     */
    public final TableField<RouteRecord, UUID> ID = createField("id", org.jooq.impl.SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.route.type</code>.
     */
    public final TableField<RouteRecord, BigDecimal> TYPE = createField("type", org.jooq.impl.SQLDataType.NUMERIC, this, "");

    /**
     * The column <code>public.route.line</code>.
     */
    public final TableField<RouteRecord, String> LINE = createField("line", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.route.update</code>.
     */
    public final TableField<RouteRecord, Timestamp> UPDATE = createField("update", org.jooq.impl.SQLDataType.TIMESTAMP, this, "");

    /**
     * Create a <code>public.route</code> table reference
     */
    public Route() {
        this("route", null);
    }

    /**
     * Create an aliased <code>public.route</code> table reference
     */
    public Route(String alias) {
        this(alias, ROUTE);
    }

    private Route(String alias, Table<RouteRecord> aliased) {
        this(alias, aliased, null);
    }

    private Route(String alias, Table<RouteRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<RouteRecord> getPrimaryKey() {
        return Keys.ROUTE_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<RouteRecord>> getKeys() {
        return Arrays.<UniqueKey<RouteRecord>>asList(Keys.ROUTE_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Route as(String alias) {
        return new Route(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Route rename(String name) {
        return new Route(name, null);
    }
}
