package ch.bernmobil.vibe.realtimedata.repository.mock.data;

import ch.bernmobil.vibe.shared.entitiy.Journey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JourneyMockData {
    private static List<Journey> dataSource;

    private static UUID[] ids = {
        UUID.fromString("d0ce83da-8eb2-47c8-aa85-f98a7cf7bae4"),
        UUID.fromString("13cbd43b-1d55-442a-a511-118cc36285dc"),
        UUID.fromString("55ce4d1a-f3b6-41e5-9914-ccd20e9e0b41")
    };

    private static String[] headsigns = {
        "Solothurn, Hauptbahnhof",
        "Adelboden, Post",
        "Spiez, Bahnhof"
    };

    private static UUID[] routes = {
        UUID.fromString("75839273-2c5d-400e-a250-bd510a5d7de1"),
        UUID.fromString("9ede091b-5edd-46d5-8a1a-b2f96f0b43d6"),
        UUID.fromString("9ede091b-5edd-46d5-8a1a-b2f96f0b43d6"),
    };

    private static UUID[] terminal_stations = {
        UUID.fromString("a705c8d2-7089-458f-b68d-469ba6ac90d6"),
        UUID.fromString("2456efe9-4dd2-4929-9add-a108cd6a66ff"),
        UUID.fromString("cc8a03cc-1648-425e-9e4d-48540964bd91")
    };


    public static List<Journey> getDataSource() {
        if(dataSource == null) {
            dataSource = new ArrayList<>();
            for(int i = 0; i < ids.length; i++) {
                dataSource.add(new Journey(ids[i], headsigns[i], routes[i], terminal_stations[i]));
            }
        }

        return dataSource;
    }

}
