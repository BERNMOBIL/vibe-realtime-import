package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.TestConfiguration;
import ch.bernmobil.vibe.shared.mockdata.JourneyMapperMockData;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import org.jooq.DSLContext;
import org.junit.Assert;
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
public class JourneyMapperRepositoryTest {
    private JourneyMapperRepository journeyMapperRepository;
    private DSLContext dslContext;

    @Test
    public void getJourneyMappingWithValidGTFSIdTest() {
        final String validGtfsTripId = "101_000827_10_95932_76646_07:26_07:45";
        final String expectedServiceId = "338";
        final UUID expectedUuid = UUID.fromString("d0ce83da-8eb2-47c8-aa85-f98a7cf7bae4");

        Optional<JourneyMapping> validMapping = journeyMapperRepository.findByGtfsTripId(validGtfsTripId);
        JourneyMapping validJourneyMapping = validMapping.get();

        Assert.assertTrue(validMapping.isPresent());
        Assert.assertEquals(validGtfsTripId, validJourneyMapping.getGtfsTripId());
        Assert.assertEquals(expectedServiceId, validJourneyMapping.getGtfsServiceId());
        Assert.assertEquals(expectedUuid, validJourneyMapping.getId());
    }

    @Test
    public void getJourneyMappingWithInvalidGTFSIdTest() {
        final String invalidGtfsTripId = "not_valid_id";

        Optional<JourneyMapping> invalidMapping = journeyMapperRepository.findByGtfsTripId(invalidGtfsTripId);
        Assert.assertFalse(invalidMapping.isPresent());
    }

    @Test
    public void loadJourneyMappingTest() {
        final int expectedNumResults = 3;
        JourneyMapperRepository originalJourneyMapperRepository = new JourneyMapperRepository(dslContext);
        originalJourneyMapperRepository.load(new Timestamp(0));
        Collection<JourneyMapping> journeyMappings = originalJourneyMapperRepository.getEntries().values();
        List<JourneyMapping> sortedJourneyMappings = journeyMappings.stream().sorted(Comparator.comparing(JourneyMapping::getId)).collect(toList());
        List<JourneyMapping> expectedJourneyMappings = JourneyMapperMockData.getDataSource().stream().sorted(Comparator.comparing(JourneyMapping::getId)).collect(toList());

        for(int i = 0; i < expectedNumResults; i++) {
            JourneyMapping expected = expectedJourneyMappings.get(i);
            JourneyMapping actual = sortedJourneyMappings.get(i);
            Assert.assertEquals(expected.getId(), actual.getId());
            Assert.assertEquals(expected.getGtfsTripId(), actual.getGtfsTripId());
            Assert.assertEquals(expected.getGtfsServiceId(), actual.getGtfsServiceId());
        }
        Assert.assertEquals(expectedNumResults, journeyMappings.size());
    }

    @Autowired
    private void setJourneyMapperRepository(JourneyMapperRepository journeyMapperRepository) {
        this.journeyMapperRepository = journeyMapperRepository;
    }

    @Autowired
    private void setMockedDslContext(DSLContext dslContext) {
        this.dslContext = dslContext;
    }
}
