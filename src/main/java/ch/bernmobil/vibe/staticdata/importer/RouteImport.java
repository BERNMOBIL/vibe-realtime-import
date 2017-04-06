package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsRoute;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.RouteFieldSetMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RouteImport extends Import<GtfsRoute> {

    @Autowired
    public RouteImport(SessionFactory sessionFactory) {
        super(sessionFactory, RouteInfo.fieldNames, RouteInfo.path, new RouteFieldSetMapper());
    }


    private static class RouteInfo {
        static String[] fieldNames = {"route_id", "agency_id", "route_short_name", "route_long_name", "route_desc", "route_type", "route_url", "route_color", "route_text_color"};
        static String path = "gtfs/routes.txt";
    }
}
