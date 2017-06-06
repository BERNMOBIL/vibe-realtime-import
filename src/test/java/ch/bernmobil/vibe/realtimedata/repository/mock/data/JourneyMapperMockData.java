package ch.bernmobil.vibe.realtimedata.repository.mock.data;

import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JourneyMapperMockData {
    private static List<JourneyMapping> dataSource;

    private static UUID[] ids = {
        JourneyMockData.getDataSource().get(0).getId(),
        JourneyMockData.getDataSource().get(1).getId(),
        JourneyMockData.getDataSource().get(2).getId(),
    };

    private static String[] gtfsTripIds = {
        "101_000827_10_95932_76646_07:26_07:45",
        "180_000827_10_95932_90052_18:16_18:53",
        "230_000827_6_7110_7062_05:20_05:30",
    };

    private static String[] gtfsServiceIds = {
        "338",
        "22",
        "338",
    };

    private static Timestamp[] updates = {
        new Timestamp(0),
        new Timestamp(0),
        new Timestamp(0),
    };

    private static JourneyMapping create(int index) {
        return new JourneyMapping(gtfsTripIds[index], gtfsServiceIds[index], ids[index]);
    }


    public static List<JourneyMapping> getDataSource() {
        if(dataSource == null) {
            dataSource = new ArrayList<>();

            for(int i = 0; i < ids.length; i++) {
                dataSource.add(create(i));
            }
        }

        return dataSource;
    }

    public static Map<String, JourneyMapping> getMappingData() {
        Map<String, JourneyMapping> mappings = new HashMap<>();
        for(JourneyMapping journeyMapping : getDataSource()) {
            mappings.put(journeyMapping.getGtfsTripId(), journeyMapping);
        }
        return mappings;
    }

}
