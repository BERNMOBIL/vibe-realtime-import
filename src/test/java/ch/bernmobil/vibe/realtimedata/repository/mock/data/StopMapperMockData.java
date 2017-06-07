package ch.bernmobil.vibe.realtimedata.repository.mock.data;

import ch.bernmobil.vibe.shared.mapping.StopMapping;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StopMapperMockData {
    private static List<StopMapping> dataSource;
    private static Map<String, StopMapping> mappings;

    private static UUID[] ids = {
        StopMockData.getDataSource().get(0).getId(),
        StopMockData.getDataSource().get(1).getId(),
        StopMockData.getDataSource().get(2).getId(),
    };

    private static String[] gtfsIds = {
        "77969_0",
        "76313_0",
        "88423_0"
    };

    private static Timestamp[] updates = {
        new Timestamp(0),
        new Timestamp(0),
        new Timestamp(0),
    };


    public static List<StopMapping> getDataSource() {
        if(dataSource == null) {
            dataSource = new ArrayList<>();
            for(int i = 0; i < ids.length; i++) {
                dataSource.add(new StopMapping(gtfsIds[i], ids[i]));
            }
        }

        return dataSource;
    }

    public static Map<String, StopMapping> getMappingData() {
        if(mappings == null) {
            mappings = new HashMap<>();
            for(StopMapping stopMapping : getDataSource()) {
                mappings.put(stopMapping.getGtfsId(), stopMapping);
            }
        }
        return mappings;
    }

}
