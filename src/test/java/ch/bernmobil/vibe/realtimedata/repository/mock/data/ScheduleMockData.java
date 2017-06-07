package ch.bernmobil.vibe.realtimedata.repository.mock.data;

import ch.bernmobil.vibe.shared.entitiy.Schedule;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScheduleMockData {
    private static List<Schedule> dataSource;
    private static Map<String, Schedule> mappings;

    private static UUID[] ids = {
        UUID.fromString("635977d7-28be-4cbc-833b-f817fbc47225"),
        UUID.fromString("1b50cc76-83be-4aa0-bde9-74fc188a8978"),
        UUID.fromString("86deb4f8-aaa3-4734-a772-1ee38f3e0344")
    };

    private static String[] platforms = {
        "Plattform 1",
        "Plattform 2",
        "Plattform 3",
    };

    private static Time[] planned_arrivals = {
        Time.valueOf(LocalTime.parse("01:38:00")),
        Time.valueOf(LocalTime.parse("05:08:00")),
        Time.valueOf(LocalTime.parse("01:20:00")),
    };

    private static Time[] planned_departures = {
        Time.valueOf(LocalTime.parse("01:38:00")),
        Time.valueOf(LocalTime.parse("05:08:00")),
        Time.valueOf(LocalTime.parse("01:20:00")),
    };

    private static UUID[] stops = {
        StopMockData.getDataSource().get(0).getId(),
        StopMockData.getDataSource().get(1).getId(),
        StopMockData.getDataSource().get(2).getId()
    };
    private static UUID[] journeys = {
        JourneyMockData.getDataSource().get(0).getId(),
        JourneyMockData.getDataSource().get(1).getId(),
        JourneyMockData.getDataSource().get(2).getId(),
    };

    public static List<Schedule> getDataSource() {
        if(dataSource == null) {
            dataSource = new ArrayList<>();
            for(int i = 0; i < ids.length; i++) {
                dataSource.add(
                    new Schedule(ids[i], platforms[i], planned_arrivals[i], planned_departures[i],
                        stops[i], journeys[i]));
            }
        }

        return dataSource;
    }

    public static Map<String, Schedule> getMappingData() {
        if(mappings == null) {
            mappings = new HashMap<>();
            for(Schedule schedule : getDataSource()) {
                String key = String.format("%s:%s", schedule.getJourney(), schedule.getStop());
                mappings.put(key, schedule);
            }
        }
        return mappings;
    }

}
