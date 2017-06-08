/*
 * This file is generated by jOOQ.
*/
package ch.bernmobil.vibe.mocks.static_;


import ch.bernmobil.vibe.mocks.static_.tables.Area;
import ch.bernmobil.vibe.mocks.static_.tables.CalendarDate;
import ch.bernmobil.vibe.mocks.static_.tables.CalendarException;
import ch.bernmobil.vibe.mocks.static_.tables.Journey;
import ch.bernmobil.vibe.mocks.static_.tables.JourneyDisruption;
import ch.bernmobil.vibe.mocks.static_.tables.Route;
import ch.bernmobil.vibe.mocks.static_.tables.Schedule;
import ch.bernmobil.vibe.mocks.static_.tables.ScheduleUpdate;
import ch.bernmobil.vibe.mocks.static_.tables.Stop;
import ch.bernmobil.vibe.mocks.static_.tables.UpdateHistory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


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
public class Public extends SchemaImpl {

    private static final long serialVersionUID = -362218015;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.area</code>.
     */
    public final Area AREA = ch.bernmobil.vibe.mocks.static_.tables.Area.AREA;

    /**
     * The table <code>public.calendar_date</code>.
     */
    public final CalendarDate CALENDAR_DATE = ch.bernmobil.vibe.mocks.static_.tables.CalendarDate.CALENDAR_DATE;

    /**
     * The table <code>public.calendar_exception</code>.
     */
    public final CalendarException CALENDAR_EXCEPTION = ch.bernmobil.vibe.mocks.static_.tables.CalendarException.CALENDAR_EXCEPTION;

    /**
     * The table <code>public.journey</code>.
     */
    public final Journey JOURNEY = ch.bernmobil.vibe.mocks.static_.tables.Journey.JOURNEY;

    /**
     * The table <code>public.journey_disruption</code>.
     */
    public final JourneyDisruption JOURNEY_DISRUPTION = ch.bernmobil.vibe.mocks.static_.tables.JourneyDisruption.JOURNEY_DISRUPTION;

    /**
     * The table <code>public.route</code>.
     */
    public final Route ROUTE = ch.bernmobil.vibe.mocks.static_.tables.Route.ROUTE;

    /**
     * The table <code>public.schedule</code>.
     */
    public final Schedule SCHEDULE = ch.bernmobil.vibe.mocks.static_.tables.Schedule.SCHEDULE;

    /**
     * The table <code>public.schedule_update</code>.
     */
    public final ScheduleUpdate SCHEDULE_UPDATE = ch.bernmobil.vibe.mocks.static_.tables.ScheduleUpdate.SCHEDULE_UPDATE;

    /**
     * The table <code>public.stop</code>.
     */
    public final Stop STOP = ch.bernmobil.vibe.mocks.static_.tables.Stop.STOP;

    /**
     * The table <code>public.update_history</code>.
     */
    public final UpdateHistory UPDATE_HISTORY = ch.bernmobil.vibe.mocks.static_.tables.UpdateHistory.UPDATE_HISTORY;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            Area.AREA,
            CalendarDate.CALENDAR_DATE,
            CalendarException.CALENDAR_EXCEPTION,
            Journey.JOURNEY,
            JourneyDisruption.JOURNEY_DISRUPTION,
            Route.ROUTE,
            Schedule.SCHEDULE,
            ScheduleUpdate.SCHEDULE_UPDATE,
            Stop.STOP,
            UpdateHistory.UPDATE_HISTORY);
    }
}
