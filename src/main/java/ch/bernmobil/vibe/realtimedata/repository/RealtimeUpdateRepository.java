package ch.bernmobil.vibe.realtimedata.repository;

import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Repository for accessing the Realtime-Update-API
 */
@Component
public class RealtimeUpdateRepository {
    private List<FeedEntity> feedEntities;
    private final Logger logger = Logger.getLogger(RealtimeUpdateRepository.class);

    @Value("${bernmobil.realtime-source.url}")
    private String realtimeUrl;

    /**
     * Downloads the GTFS-Realtime Feeds and store it after parsing the downloaded protobuf-binary as {@link FeedMessage}
     * <p>Notice: The {@link com.google.transit.realtime.GtfsRealtime}-Library is used to convert the Feed-Messages</p>
     */
    private void loadFeedEntities() {
        try {
            URL url = new URL(realtimeUrl);
            FeedMessage feedMessage = FeedMessage.parseFrom(url.openStream());
            feedEntities = feedMessage.getEntityList();
        } catch (IOException e) {
            logger.error("Exception while processing Realtime updates", e);
        }
    }

    /**
     * Loads the {@link FeedMessage} using the {@link #loadFeedEntities()}-Method and returns it's result
     * @return An unmodifiableList of the {@link #feedEntities}
     */
    public List<FeedEntity> findAll() {
        loadFeedEntities();
        return Collections.unmodifiableList(feedEntities);
    }
}
