package ch.bernmobil.vibe.realtimedata.repository;

import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RealtimeUpdateRepository {
    private FeedMessage feedMessage;
    private List<FeedEntity> feedEntities;
    private final Logger logger = Logger.getLogger(RealtimeUpdateRepository.class);

    @Value("${bernmobil.realtime-source.url}")
    private String realtimeUrl;

    private void loadRealtimeUpdates() {
        try {
            URL url = new URL(realtimeUrl);
            feedMessage = FeedMessage.parseFrom(url.openStream());
            feedEntities = feedMessage.getEntityList();
        } catch (IOException e) {
            logger.error("Exception while processing realtime updates", e);
        }
    }

    public List<FeedEntity> getFeedEntities() {
        if(feedEntities == null) {
            loadRealtimeUpdates();
        }
        return feedEntities;
    }
}
