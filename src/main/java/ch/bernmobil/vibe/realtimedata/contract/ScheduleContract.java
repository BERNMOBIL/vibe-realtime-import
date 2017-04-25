package ch.bernmobil.vibe.realtimedata.contract;

public final class ScheduleContract {
    public ScheduleContract(){}
    public final static String TABLE_NAME = "schedule";
    public final static String ID = "id";
    public final static String PLATFORM = "platform";
    public final static String PLANNED_ARRIVAL = "planned_arrival";
    public final static String PLANNED_DEPARTURE = "planned_departure";
    public final static String STOP = "stop";
    public final static String JOURNEY = "journey";
    public final static String UPDATE = "update";

    public final static String[] COLUMNS = { ID, PLATFORM, PLANNED_ARRIVAL, PLANNED_DEPARTURE, STOP, JOURNEY, UPDATE };
}
