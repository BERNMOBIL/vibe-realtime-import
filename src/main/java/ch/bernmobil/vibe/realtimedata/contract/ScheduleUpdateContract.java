package ch.bernmobil.vibe.realtimedata.contract;

public final class ScheduleUpdateContract {
    public final static String TABLE_NAME = "schedule_update";
    public final static String ID = "id";
    public final static String SCHEDULE = "schedule";
    public final static String ACTUAL_ARRIVAL = "actual_arrival";
    public final static String ACTUAL_DEPARTURE = "actual_departure";

    public final static String[] COLUMNS = { ID, SCHEDULE, ACTUAL_ARRIVAL, ACTUAL_DEPARTURE };
}
