package ch.bernmobil.vibe.realtimedata.repository.mock.data;

import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScheduleUpdateInformationMockData {
    private static List<ScheduleUpdateInformation> dataSource;
    private static List<ScheduleUpdateInformation> dataSourceWithoutScheduleId;


    private static Time[] actualArrivals = {
        Time.valueOf(LocalTime.parse("13:14:15")),
        new Time(0),
        new Time(0)
    };

    private static Time[] actualDepartures = {
        new Time(0),
        new Time(0),
        new Time(0)
    };

    private static UUID[] journeyIds = {
        JourneyMockData.getDataSource().get(0).getId(),
        JourneyMockData.getDataSource().get(1).getId(),
        JourneyMockData.getDataSource().get(2).getId(),
    };

    private static UUID[] stopIds = {
        StopMockData.getDataSource().get(0).getId(),
        StopMockData.getDataSource().get(1).getId(),
        StopMockData.getDataSource().get(2).getId(),
    };

    private static UUID[] scheduleIds = {
        ScheduleMockData.getDataSource().get(0).getId(),
        ScheduleMockData.getDataSource().get(1).getId(),
        ScheduleMockData.getDataSource().get(2).getId(),
    };


    private static ScheduleUpdateInformation create(int index) {
        return new ScheduleUpdateInformation(actualArrivals[index], actualDepartures[index], journeyIds[index], stopIds[index]);
    }

    public static List<ScheduleUpdateInformation> getDataSource() {
        if(dataSource == null) {
            dataSource = new ArrayList<>();

            for(int i = 0; i < actualArrivals.length; i++) {
                ScheduleUpdateInformation scheduleUpdateInformation = create(i);
                scheduleUpdateInformation.setScheduleId(scheduleIds[i]);
                dataSource.add(scheduleUpdateInformation);
            }
        }

        return dataSource;
    }

    public static List<ScheduleUpdateInformation> getDataSourceWithoutScheduleId() {
        if(dataSourceWithoutScheduleId == null) {
            dataSourceWithoutScheduleId = new ArrayList<>();

            for(int i = 0; i < actualArrivals.length; i++) {
                dataSourceWithoutScheduleId.add(create(i));
            }
        }

        return dataSourceWithoutScheduleId;
    }

}
