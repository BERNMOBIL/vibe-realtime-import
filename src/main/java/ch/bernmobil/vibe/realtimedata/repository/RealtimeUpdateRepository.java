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

@Component
public class RealtimeUpdateRepository {
    private List<FeedEntity> feedEntities;
    private final Logger logger = Logger.getLogger(RealtimeUpdateRepository.class);

    @Value("${bernmobil.realtime-source.url}")
    private String realtimeUrl;

    private void loadFeedEntities() {
        try {
            URL url = new URL(realtimeUrl);
            FeedMessage feedMessage = FeedMessage.parseFrom(url.openStream());
            feedEntities = feedMessage.getEntityList();
        } catch (IOException e) {
            logger.error("Exception while processing Realtime updates", e);
        }
    }

    public List<FeedEntity> findAll() {
        loadFeedEntities();
        return Collections.unmodifiableList(feedEntities);
    }
}
