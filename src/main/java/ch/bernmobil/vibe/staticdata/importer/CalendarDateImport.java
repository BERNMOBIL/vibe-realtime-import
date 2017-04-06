package ch.bernmobil.vibe.staticdata.importer;


import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsCalendarDate;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.CalendarDateFieldSetMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CalendarDateImport extends Import<GtfsCalendarDate> {

    @Autowired
    public CalendarDateImport(SessionFactory sessionFactory) {
        super(sessionFactory, CalendarDateInfo.fieldNames, CalendarDateInfo.path, new CalendarDateFieldSetMapper());
    }


    private static class CalendarDateInfo {
        static String[] fieldNames = {"service_id", "date", "exception_type"};
        static String path = "gtfs/calendar_dates.txt";
    }
}
