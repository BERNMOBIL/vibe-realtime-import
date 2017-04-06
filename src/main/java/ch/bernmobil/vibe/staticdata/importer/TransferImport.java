package ch.bernmobil.vibe.staticdata.importer;

import ch.bernmobil.vibe.staticdata.gtfsmodel.GtfsTransfer;
import org.hibernate.SessionFactory;

public class TransferImport extends Import<GtfsTransfer>{

    public TransferImport(SessionFactory sessionFactory) {
        super(sessionFactory, TransferInfo.fieldNames, TransferInfo.path);
    }

    private static class TransferInfo {
        static String[] fieldNames = {"from_stop_id", "to_stop_id", "transfer_type", "min_transfer_time"};
        static String path = "gtfs/transfers.txt";
    }
}
