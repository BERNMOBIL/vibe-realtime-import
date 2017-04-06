package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStopTime;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.StopTimeFieldSetMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class StopTimeImport extends Import<GtfsStopTime> {

    @Autowired
    public StopTimeImport(SessionFactory sessionFactory) {
        super(sessionFactory, StopTimeInfo.fieldNames, StopTimeInfo.path, new StopTimeFieldSetMapper());
    }


    private static class StopTimeInfo {
        static String[] fieldNames = {"trip_id" , "arrival_time", "departure_time", "stop_id", "stop_sequence", "stop_headsign", "pickup_type", "drop_off_type", "shape_dist_traveled"};
        static String path = "gtfs/stop_times.txt";
    }
}
