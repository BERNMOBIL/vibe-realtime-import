package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.TestConfiguration;
import ch.bernmobil.vibe.shared.mockdata.StopMapperMockData;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
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
public class StopMapperRepositoryTest {

    private StopMapperRepository stopMapperRepository;
    private DSLContext dslContext;

    @Test
    public void getStopMappingWithValidGTFSIdTest() {
        final String validGtfsId = "76313_0";
        final UUID expectedUuid = UUID.fromString("115a3a10-7744-4226-adf3-8c35ad6fac0a");

        Optional<StopMapping> validMapping = stopMapperRepository.findByGtfsId(validGtfsId);
        StopMapping validStopMapping = validMapping.get();

        Assert.assertTrue(validMapping.isPresent());
        Assert.assertEquals(validGtfsId, validStopMapping.getGtfsId());
        Assert.assertEquals(expectedUuid, validStopMapping.getId());
    }

    @Test
    public void getStopMappingWithInvalidGTFSIdTest() {
        final String invalidGtfsId = "not_valid_id";

        Optional<StopMapping> invalidMapping = stopMapperRepository.findByGtfsId(invalidGtfsId);
        Assert.assertFalse(invalidMapping.isPresent());
    }

    @Test
    public void loadStopMappingTest() {
        final int expectedNumResults = 3;
        StopMapperRepository originalStopMapperRepository = new StopMapperRepository(dslContext);
        originalStopMapperRepository.load(new Timestamp(0));
        Collection<StopMapping> stopMappings = originalStopMapperRepository.getEntries().values();
        List<StopMapping> sortedStopMappings = stopMappings.stream().sorted(Comparator.comparing(StopMapping::getId)).collect(toList());
        List<StopMapping> expectedStopMappings = StopMapperMockData.getDataSource().stream().sorted(Comparator.comparing(StopMapping::getId)).collect(toList());

        for (int i = 0; i < expectedNumResults; i++) {
            StopMapping expected = expectedStopMappings.get(i);
            StopMapping actual = sortedStopMappings.get(i);
            Assert.assertEquals(expected.getId(), actual.getId());
            Assert.assertEquals(expected.getGtfsId(), actual.getGtfsId());
        }

        Assert.assertEquals(expectedNumResults, stopMappings.size());
    }


    @Autowired
    private void setStopMapperRepository(StopMapperRepository stopMapperRepository) {
        this.stopMapperRepository = stopMapperRepository;
    }

    @Autowired
    private void setMockedDslContext(DSLContext dslContext) {
        this.dslContext = dslContext;
    }
}
