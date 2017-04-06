package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsTrip;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.TripFieldSetMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TripImport extends Import<GtfsTrip> {

    @Autowired
    public TripImport(SessionFactory sessionFactory) {
        super(sessionFactory, TripInfo.fieldNames, TripInfo.path, new TripFieldSetMapper());
    }


    private static class TripInfo {
        static String[] fieldNames = {"route_id", "service_id", "trip_id", "trip_headsign", "trip_short_name", "direction_id", "block_id", "shape_id"};
        static String path = "gtfs/trips.txt";
    }
}
