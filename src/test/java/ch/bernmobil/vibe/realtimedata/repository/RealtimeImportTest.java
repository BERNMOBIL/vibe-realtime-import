package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.ImportRunner;
import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdateInformation;
import ch.bernmobil.vibe.realtimedata.repository.mock.data.ScheduleUpdateInformationMockData;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    private ScheduleRepository scheduleRepository;
    private RealtimeUpdateRepository realtimeUpdateRepository;
    private JourneyMapperRepository journeyMapperRepository;
    private StopMapperRepository stopMapperRepository;
    private ImportRunner importRunner;

    private boolean isFirstInitialization = true;
    private static List<ScheduleUpdateInformation> mockedScheduleUpdateInformations;
    private static List<ScheduleUpdateInformation> mockedScheduleUpdateInformationsWithoutScheduleId;

    //BeforeClass executed before @Autowired
    @Before
    public void loadMockedDataSources() {
        if(isFirstInitialization) {
            isFirstInitialization = false;

            mockedScheduleUpdateInformations = ScheduleUpdateInformationMockData.getDataSource();
            mockedScheduleUpdateInformationsWithoutScheduleId = ScheduleUpdateInformationMockData.getDataSourceWithoutScheduleId();
        }
    }

    @Test
    public void getJourneyMappingWithValidGTFSIdTest() {
        final String VALID_GTFS_TRIP_ID = "101_000827_10_95932_76646_07:26_07:45";
        final String EXPECTED_SERVICE_ID = "338";
        final UUID EXPECTED_UUID = UUID.fromString("d0ce83da-8eb2-47c8-aa85-f98a7cf7bae4");

        Optional<JourneyMapping> validMapping = journeyMapperRepository.findByGtfsTripId(VALID_GTFS_TRIP_ID);
        JourneyMapping validJourneyMapping = validMapping.get();

        Assert.assertTrue(validMapping.isPresent());
        Assert.assertEquals(VALID_GTFS_TRIP_ID, validJourneyMapping.getGtfsTripId());
        Assert.assertEquals(EXPECTED_SERVICE_ID, validJourneyMapping.getGtfsServiceId());
        Assert.assertEquals(EXPECTED_UUID, validJourneyMapping.getId());
    }

    @Test
    public void getJourneyMappingWithInvalidGTFSIdTest() {
        final String INVALID_GTFS_TRIP_ID = "not_valid_id";

        Optional<JourneyMapping> invalidMapping = journeyMapperRepository.findByGtfsTripId(INVALID_GTFS_TRIP_ID);
        Assert.assertFalse(invalidMapping.isPresent());
    }

    @Test
    public void getStopMappingWithValidGTFSIdTest() {
        final String VALID_GTFS_ID = "76313_0";
        final UUID EXPECTED_UUID = UUID.fromString("115a3a10-7744-4226-adf3-8c35ad6fac0a");

        Optional<StopMapping> validMapping = stopMapperRepository.findByGtfsId(VALID_GTFS_ID);
        StopMapping validStopMapping = validMapping.get();

        Assert.assertTrue(validMapping.isPresent());
        Assert.assertEquals(VALID_GTFS_ID, validStopMapping.getGtfsId());
        Assert.assertEquals(EXPECTED_UUID, validStopMapping.getId());
    }

    @Test
    public void getStopMappingWithInvalidGTFSIdTest() {
        final String INVALID_GTFS_ID = "not_valid_id";

        Optional<StopMapping> invalidMapping = stopMapperRepository.findByGtfsId(INVALID_GTFS_ID);
        Assert.assertFalse(invalidMapping.isPresent());
    }

    @Test
    public void extractUpdateInformations() {
        final int EXPECTED_RESULT_SIZE = 3;
        final UUID[] EXPECTED_JOURNEY_UUIDS = {
            UUID.fromString("d0ce83da-8eb2-47c8-aa85-f98a7cf7bae4"),
            UUID.fromString("13cbd43b-1d55-442a-a511-118cc36285dc"),
            UUID.fromString("55ce4d1a-f3b6-41e5-9914-ccd20e9e0b41")
        };
        final UUID[] EXPECTED_STOP_UUIDS = {
            UUID.fromString("c8f1191b-9316-4834-a384-262a63fdc7f0"),
            UUID.fromString("115a3a10-7744-4226-adf3-8c35ad6fac0a"),
            UUID.fromString("6dd26aaa-3120-4d3a-b492-6f8d2cf7856e")
        };
        final Time[] EXPECTED_ARRIVAL_TIMES = {
            Time.valueOf(LocalTime.parse("11:15:30")),
            Time.valueOf(LocalTime.parse("12:35:01")),
            Time.valueOf(LocalTime.parse("13:19:03"))
        };
        final Time[] EXPECTED_DEPARTURES_TIMES = {
            null,
            null,
            null
        };

        ReflectionTestUtils.setField(importRunner, "timezone", "Europe/Paris", String.class);
        List<FeedEntity> feedEntities = new ArrayList<>(realtimeUpdateRepository.findAll());
        List<ScheduleUpdateInformation> scheduleUpdateInformations = importRunner.extractScheduleUpdateInformation(feedEntities);

        Assert.assertEquals(EXPECTED_RESULT_SIZE, scheduleUpdateInformations.size());
        for(int i = 0; i < EXPECTED_RESULT_SIZE; i++) {
            Assert.assertEquals(EXPECTED_JOURNEY_UUIDS[i], scheduleUpdateInformations.get(i).getJourneyId());
            Assert.assertEquals(EXPECTED_STOP_UUIDS[i], scheduleUpdateInformations.get(i).getStopId());
            Assert.assertEquals(EXPECTED_ARRIVAL_TIMES[i], scheduleUpdateInformations.get(i).getActualArrival());
            Assert.assertEquals(EXPECTED_DEPARTURES_TIMES[i], scheduleUpdateInformations.get(i).getActualDeparture());
            Assert.assertNull(scheduleUpdateInformations.get(i).getScheduleId());
        }
    }

    @Test
    public void addScheduleIdTest() {
        final UUID[] EXPECTED_SCHEDULE_IDS = {
            UUID.fromString("635977d7-28be-4cbc-833b-f817fbc47225"),
            UUID.fromString("1b50cc76-83be-4aa0-bde9-74fc188a8978"),
            UUID.fromString("86deb4f8-aaa3-4734-a772-1ee38f3e0344")
        };

        List<ScheduleUpdateInformation> scheduleUpdateInformationList = new ArrayList<>(mockedScheduleUpdateInformationsWithoutScheduleId);

        for(int i = 0; i < EXPECTED_SCHEDULE_IDS.length; i++) {
            Assert.assertNull(scheduleUpdateInformationList.get(i).getScheduleId());
        }

        scheduleRepository.addScheduleId(scheduleUpdateInformationList);

        for(int i = 0; i < EXPECTED_SCHEDULE_IDS.length; i++) {
            Assert.assertEquals(EXPECTED_SCHEDULE_IDS[i], scheduleUpdateInformationList.get(i).getScheduleId());
        }
    }


    @Autowired
    private void setScheduleRepository(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Autowired
    private void setRealtimeUpdateRepository(RealtimeUpdateRepository realtimeUpdateRepository) {
        this.realtimeUpdateRepository = realtimeUpdateRepository;
    }

    @Autowired
    private void setJourneyMapperRepository(JourneyMapperRepository journeyMapperRepository) {
        this.journeyMapperRepository = journeyMapperRepository;
    }

    @Autowired
    private void setStopMapperRepository(StopMapperRepository stopMapperRepository) {
        this.stopMapperRepository = stopMapperRepository;
    }

    @Autowired
    private void setImportRunner(ImportRunner importRunner) {
        this.importRunner = importRunner;
    }
}
