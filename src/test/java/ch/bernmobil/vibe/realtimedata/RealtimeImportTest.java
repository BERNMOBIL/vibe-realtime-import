package ch.bernmobil.vibe.realtimedata;

import static java.util.stream.Collectors.toList;

import ch.bernmobil.vibe.shared.entitiy.ScheduleUpdate;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;
import ch.bernmobil.vibe.realtimedata.repository.JourneyMapperRepository;
import ch.bernmobil.vibe.realtimedata.repository.RealtimeUpdateRepository;
import ch.bernmobil.vibe.realtimedata.repository.ScheduleRepository;
import ch.bernmobil.vibe.realtimedata.repository.StopMapperRepository;
import ch.bernmobil.vibe.realtimedata.repository.mock.data.JourneyMapperMockData;
import ch.bernmobil.vibe.realtimedata.repository.mock.data.ScheduleMockData;
import ch.bernmobil.vibe.realtimedata.repository.mock.data.ScheduleUpdateInformationMockData;
import ch.bernmobil.vibe.realtimedata.repository.mock.data.StopMapperMockData;
import ch.bernmobil.vibe.shared.entitiy.Schedule;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jooq.DSLContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfiguration.class })
@ActiveProfiles("testConfiguration")
public class RealtimeImportTest {

    private RealtimeUpdateRepository realtimeUpdateRepository;
    private ImportRunner importRunner;
    private DSLContext dslContext;

    private boolean isFirstInitialization = true;
    private static List<ScheduleUpdateInformation> mockedScheduleUpdateInformations;

    //BeforeClass executed before @Autowired
    @Before
    public void loadMockedDataSources() {
        if(isFirstInitialization) {
            isFirstInitialization = false;

            mockedScheduleUpdateInformations = ScheduleUpdateInformationMockData.getDataSource();
        }
    }

    @Test
    public void extractUpdateInformations() {
        final int expectedResultSize = 3;
        final UUID[] expectedJourneyUuids = {
            UUID.fromString("d0ce83da-8eb2-47c8-aa85-f98a7cf7bae4"),
            UUID.fromString("13cbd43b-1d55-442a-a511-118cc36285dc"),
            UUID.fromString("55ce4d1a-f3b6-41e5-9914-ccd20e9e0b41")
        };
        final UUID[] expectedStopUuids = {
            UUID.fromString("c8f1191b-9316-4834-a384-262a63fdc7f0"),
            UUID.fromString("115a3a10-7744-4226-adf3-8c35ad6fac0a"),
            UUID.fromString("6dd26aaa-3120-4d3a-b492-6f8d2cf7856e")
        };
        final Time[] expectedArrivalTimes = {
            Time.valueOf(LocalTime.parse("11:15:30")),
            Time.valueOf(LocalTime.parse("12:35:01")),
            Time.valueOf(LocalTime.parse("13:19:03"))
        };
        final Time[] expectedDeparturesTimes = {
            null,
            null,
            null
        };


        List<FeedEntity> feedEntities = new ArrayList<>(realtimeUpdateRepository.findAll());
        List<ScheduleUpdateInformation> scheduleUpdateInformations = importRunner.extractScheduleUpdateInformation(feedEntities);

        Assert.assertEquals(expectedResultSize, scheduleUpdateInformations.size());
        for(int i = 0; i < expectedResultSize; i++) {
            Assert.assertEquals(expectedJourneyUuids[i], scheduleUpdateInformations.get(i).getJourneyId());
            Assert.assertEquals(expectedStopUuids[i], scheduleUpdateInformations.get(i).getStopId());
            Assert.assertEquals(expectedArrivalTimes[i], scheduleUpdateInformations.get(i).getActualArrival());
            Assert.assertEquals(expectedDeparturesTimes[i], scheduleUpdateInformations.get(i).getActualDeparture());
            Assert.assertNull(scheduleUpdateInformations.get(i).getScheduleId());
        }
    }

    @Test
    public void convertUpdateInformationsToScheduleUpdateTest() {
        //Sort has to be done, because the method convert disorder the elements.
        final List<ScheduleUpdate> expectedScheduleUpdates = new ArrayList<ScheduleUpdate>(){{
            add(new ScheduleUpdate(Time.valueOf(LocalTime.parse("14:00:00")),
                Time.valueOf(LocalTime.parse("14:02:00")),
                UUID.fromString("1b50cc76-83be-4aa0-bde9-74fc188a8978")));
            add(new ScheduleUpdate(Time.valueOf(LocalTime.parse("13:14:15")),
                Time.valueOf(LocalTime.parse("13:14:35")),
                UUID.fromString("635977d7-28be-4cbc-833b-f817fbc47225")));
            add(new ScheduleUpdate(Time.valueOf(LocalTime.parse("16:00:00")),
                Time.valueOf(LocalTime.parse("16:07:00")),
                UUID.fromString("86deb4f8-aaa3-4734-a772-1ee38f3e0344")));
        }}.stream().sorted(Comparator.comparing(ScheduleUpdate::getSchedule)).collect(toList());

        List<ScheduleUpdateInformation> informations = new ArrayList<>(mockedScheduleUpdateInformations);
        List<ScheduleUpdate> sortedScheduleUpdates = importRunner.convert(informations).stream()
            .sorted(Comparator.comparing(ScheduleUpdate::getSchedule)).collect(toList());

        Assert.assertEquals(informations.size(), sortedScheduleUpdates.size());
        for(int i = 0; i < expectedScheduleUpdates.size(); i++) {
            ScheduleUpdate expected = expectedScheduleUpdates.get(i);
            ScheduleUpdate actual = sortedScheduleUpdates.get(i);
            Assert.assertEquals(expected.getActualArrival(), actual.getActualArrival());
            Assert.assertEquals(expected.getActualDeparture(), actual.getActualDeparture());
            Assert.assertEquals(expected.getSchedule(), actual.getSchedule());
        }
    }

    @Test
    public void convertToUpdateInformationTest() {
        final StopTimeUpdate stopTimeUpdate = realtimeUpdateRepository.findAll().get(0).getTripUpdate().getStopTimeUpdate(0);
        final String gtfsTripId = "101_000827_10_95932_76646_07:26_07:45";
        final ScheduleUpdateInformation expected = new ScheduleUpdateInformation(
            Time.valueOf(LocalTime.parse("11:15:30")),
            null, UUID.fromString("d0ce83da-8eb2-47c8-aa85-f98a7cf7bae4"),
            UUID.fromString("c8f1191b-9316-4834-a384-262a63fdc7f0"));

        ScheduleUpdateInformation actual = importRunner.convertToScheduleUpdateInformation(stopTimeUpdate, gtfsTripId);
        Assert.assertEquals(expected.getActualArrival(), actual.getActualArrival());
        Assert.assertEquals(expected.getActualDeparture(), actual.getActualDeparture());
        Assert.assertEquals(expected.getJourneyId(), actual.getJourneyId());
        Assert.assertEquals(expected.getStopId(), actual.getStopId());
        Assert.assertNull(actual.getScheduleId());

    }

    @Test
    public void parseTimeTest() {
        final long inputTimestamp = 1496754508;
        final String timezoneEurope = "Europe/Paris";
        final String timezoneAustralia = "Australia/Sydney";
        final Time expectedTimeEurope = Time.valueOf(LocalTime.parse("15:08:28"));
        final Time expectedTimeAustralia = Time.valueOf(LocalTime.parse("23:08:28"));

        Time parsedTimeEurope = ImportRunner.parseUpdateTime(inputTimestamp, timezoneEurope);
        Time parsedTimeAustralia = ImportRunner.parseUpdateTime(inputTimestamp, timezoneAustralia);

        Assert.assertEquals(expectedTimeEurope, parsedTimeEurope);
        Assert.assertEquals(expectedTimeAustralia, parsedTimeAustralia);
    }

    @Autowired
    private void setRealtimeUpdateRepository(RealtimeUpdateRepository realtimeUpdateRepository) {
        this.realtimeUpdateRepository = realtimeUpdateRepository;
    }

    @Autowired
    private void setMockedDslContext(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Autowired
    private void setImportRunner(ImportRunner importRunner) {
        this.importRunner = importRunner;
        ReflectionTestUtils.setField(importRunner, "timezone", "Europe/Paris", String.class);
    }
}
