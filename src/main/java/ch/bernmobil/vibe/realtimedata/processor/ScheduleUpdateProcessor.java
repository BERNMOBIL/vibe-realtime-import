package ch.bernmobil.vibe.realtimedata.processor;

import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;
import org.springframework.batch.item.ItemProcessor;

public class ScheduleUpdateProcessor implements ItemProcessor<ScheduleUpdate, ScheduleUpdate> {
    @Override
    public ScheduleUpdate process(ScheduleUpdate item) throws Exception {
        return item;
    }
}