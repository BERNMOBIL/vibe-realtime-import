package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsStop;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.StopFieldSetMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class StopImport extends Import<GtfsStop> {

    @Autowired
    public StopImport(SessionFactory sessionFactory) {
        super(sessionFactory, StopInfo.fieldNames, StopInfo.path, new StopFieldSetMapper());
    }

    private static class StopInfo {
        static String[] fieldNames = {"stop_id", "stop_code", "stop_name", "stop_desc", "stop_lat", "stop_lon", "zone_id", "stop_url", "location_type", "parent_station"};
        static String path = "gtfs/stops.txt";
    }
}
