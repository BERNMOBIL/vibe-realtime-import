package ch.bernmobil.vibe.realtimedata.repository.mock.data;


import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScheduleUpdateMockData {
    private static List<ScheduleUpdate> dataSource;

    private static UUID[] ids = {
        UUID.fromString("9f811c73-9764-4cca-9114-12c7d8c22611"),
        UUID.fromString("d63b7b97-e698-4ca9-8018-da344ae84fa2"),
        UUID.fromString("77759720-70e1-4edf-940d-b2901d07c354")
    };

    private static UUID[] schedules = {
        ScheduleMockData.getDataSource().get(0).getId(),
        ScheduleMockData.getDataSource().get(1).getId(),
        ScheduleMockData.getDataSource().get(2).getId(),
    };

    private static Time[] actualArrivals = {
        Time.valueOf(LocalTime.parse("13:14:15")),
        Time.valueOf(LocalTime.parse("14:00:00")),
        Time.valueOf(LocalTime.parse("16:00:00")),
    };

    private static Time[] actualDepartures = {
        Time.valueOf(LocalTime.parse("13:14:35")),
        Time.valueOf(LocalTime.parse("14:02:00")),
        Time.valueOf(LocalTime.parse("16:07:00")),
    };


    public static List<ScheduleUpdate> getDataSource() {
        if(dataSource == null) {
            dataSource = new ArrayList<>();

            for(int i = 0; i < actualArrivals.length; i++) {
                dataSource.add(new ScheduleUpdate(ids[i], actualArrivals[i], actualDepartures[i],
                    schedules[i]));
            }
        }

        return dataSource;
    }


}
