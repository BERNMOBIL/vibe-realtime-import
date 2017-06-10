package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.TestConfiguration;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;
import ch.bernmobil.vibe.shared.mockdata.ScheduleMockData;
import ch.bernmobil.vibe.realtimedata.mockdata.ScheduleUpdateInformationMockData;
import ch.bernmobil.vibe.shared.entity.Schedule;
import org.jooq.DSLContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.*;

import static java.util.stream.Collectors.toList;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@ActiveProfiles("testConfiguration")
public class ScheduleRepositoryTest {

    private ScheduleRepository scheduleRepository;
    private DSLContext dslContext;

    private boolean isFirstInitialization = true;
    private static List<ScheduleUpdateInformation> mockedScheduleUpdateInformationsWithoutScheduleId;

    //BeforeClass executed before @Autowired
    @Before
    public void loadMockedDataSources() {
        if(isFirstInitialization) {
            isFirstInitialization = false;

            mockedScheduleUpdateInformationsWithoutScheduleId = ScheduleUpdateInformationMockData.getDataSourceWithoutScheduleId();
        }
    }



    @Test
    public void addScheduleIdTest() {
        final UUID[] expectedScheduleIds = {
            UUID.fromString("635977d7-28be-4cbc-833b-f817fbc47225"),
            UUID.fromString("1b50cc76-83be-4aa0-bde9-74fc188a8978"),
            UUID.fromString("86deb4f8-aaa3-4734-a772-1ee38f3e0344")
        };

        List<ScheduleUpdateInformation> scheduleUpdateInformationList = new ArrayList<>(mockedScheduleUpdateInformationsWithoutScheduleId);

        for(int i = 0; i < expectedScheduleIds.length; i++) {
            Assert.assertNull(scheduleUpdateInformationList.get(i).getScheduleId());
        }

        scheduleRepository.addScheduleId(scheduleUpdateInformationList);

        for(int i = 0; i < expectedScheduleIds.length; i++) {
            Assert.assertEquals(expectedScheduleIds[i], scheduleUpdateInformationList.get(i).getScheduleId());
        }
    }



    @Test
    public void loadSchedulesTest() {
        final int expectedNumResults = 3;
        ScheduleRepository scheduleRepository = new ScheduleRepository(dslContext);
        scheduleRepository.load(new Timestamp(0));
        Collection<Schedule> schedules = scheduleRepository.getEntries().values();
        List<Schedule> sortedSchedules = schedules.stream().sorted(Comparator.comparing(Schedule::getId)).collect(toList());
        List<Schedule> expectedSchedules = ScheduleMockData.getDataSource().stream().sorted(Comparator.comparing(Schedule::getId)).collect(toList());

        for(int i = 0; i < expectedNumResults; i++) {
            Schedule expected = expectedSchedules.get(i);
            Schedule actual = sortedSchedules.get(i);
            Assert.assertEquals(expected.getId(), actual.getId());
            Assert.assertEquals(expected.getJourney(), actual.getJourney());
            Assert.assertEquals(expected.getPlannedArrival(), actual.getPlannedArrival());
            Assert.assertEquals(expected.getPlannedDeparture(), actual.getPlannedDeparture());
            Assert.assertEquals(expected.getPlatform(), actual.getPlatform());
            Assert.assertEquals(expected.getStop(), actual.getStop());
        }
        Assert.assertEquals(expectedNumResults, schedules.size());
    }



    @Autowired
    private void setScheduleRepository(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Autowired
    private void setMockedDslContext(DSLContext dslContext) {
        this.dslContext = dslContext;
    }
}
