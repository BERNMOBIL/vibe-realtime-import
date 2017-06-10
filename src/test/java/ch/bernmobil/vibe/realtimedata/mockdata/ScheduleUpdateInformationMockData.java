package ch.bernmobil.vibe.realtimedata.mockdata;

import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;
import ch.bernmobil.vibe.shared.mockdata.JourneyMockData;
import ch.bernmobil.vibe.shared.mockdata.ScheduleMockData;
import ch.bernmobil.vibe.shared.mockdata.StopMockData;

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
        Time.valueOf(LocalTime.parse("14:00:00")),
        Time.valueOf(LocalTime.parse("16:00:00")),
    };

    private static Time[] actualDepartures = {
        Time.valueOf(LocalTime.parse("13:14:35")),
        Time.valueOf(LocalTime.parse("14:02:00")),
        Time.valueOf(LocalTime.parse("16:07:00")),
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

    private ScheduleUpdateInformationMockData() {}

    private static ScheduleUpdateInformation create(int index) {
        return new ScheduleUpdateInformation(actualArrivals[index], actualDepartures[index],
            journeyIds[index], stopIds[index]);
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
