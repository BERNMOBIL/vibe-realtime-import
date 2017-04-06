package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsAgency;
import ch.bernmobil.vibe.staticdata.fieldsetmapper.AgencyFieldSetMapper;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgencyImport extends Import<GtfsAgency> {

    @Autowired
    public AgencyImport(SessionFactory sessionFactory) {
        super(sessionFactory, AgencyInfo.fieldNames, AgencyInfo.path, new AgencyFieldSetMapper());
    }

    private static class AgencyInfo {
        static String[] fieldNames = new String[]{"agency_id", "agency_name", "agency_url", "agency_timezone", "agency_lang", "agency_phone", "agency_fare_url"};
        static String path = "gtfs/agency.txt";
    }
}
