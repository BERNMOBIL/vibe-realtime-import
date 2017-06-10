package ch.bernmobil.vibe.realtimedata.repository;

import static org.mockito.Mockito.when;

import ch.bernmobil.vibe.shared.mockdata.JourneyMapperMockData;
import com.google.protobuf.TextFormat;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.mockito.Mockito;


public class RealtimeUpdateRepositoryMock {
    RealtimeUpdateRepository mock;
    Logger logger = Logger.getLogger(RealtimeUpdateRepositoryMock.class);

    public RealtimeUpdateRepositoryMock(){
        mock = Mockito.mock(RealtimeUpdateRepository.class);
        configureMock();
    }

    protected void configureMock() {
        when(mock.findAll()).thenReturn(readRealtimeFeedFromFile());
    }

    private List<FeedEntity> readRealtimeFeedFromFile() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("mockedFullTripUpdate.pb").getFile());
            InputStream inputStream = new FileInputStream(file.getAbsolutePath());
            InputStreamReader reader = new InputStreamReader(inputStream);
            FeedMessage.Builder builder = FeedMessage.newBuilder();
            TextFormat.merge(reader, builder);
            return builder.build().getEntityList();
        }
        catch(Exception e) {
            logger.error(e.getStackTrace());
            return new ArrayList<>();
        }
    }

    public RealtimeUpdateRepository getMock() {
        return mock;
    }


}
