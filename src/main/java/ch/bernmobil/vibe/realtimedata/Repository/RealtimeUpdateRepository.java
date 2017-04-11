package ch.bernmobil.vibe.realtimedata.Repository;

import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import java.net.URL;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class RealtimeUpdateRepository {
    private FeedMessage feedMessage;
    private List<FeedEntity> feedEntities;
    private final Logger logger = Logger.getLogger(RealtimeUpdateRepository.class);

    private void loadRealtimeUpdates() {
        try {
            URL url = new URL("https://wp-test.bernmobil.ch/gtfs/827/realtime?apikey=b4059f45-9b52-4511-y68f-0fdfd0fa11c1");
            feedMessage = FeedMessage.parseFrom(url.openStream());
            feedEntities = feedMessage.getEntityList();
        } catch (Exception e) {
            logger.error("Realtime Update API-URL is wrong");
            e.printStackTrace();
        }
    }

    public List<FeedEntity> getFeedEntities() {
        if(feedEntities == null) {
            loadRealtimeUpdates();
        }
        return feedEntities;
    }
}
