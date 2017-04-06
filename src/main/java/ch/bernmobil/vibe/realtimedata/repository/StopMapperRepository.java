package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.entity.sync.JourneyMapper;
import ch.bernmobil.vibe.realtimedata.entity.sync.StopMapper;
import org.springframework.data.repository.CrudRepository;

public interface StopMapperRepository extends CrudRepository<StopMapper, Long> {
    StopMapper findFirstByGtfsId(String gtfsId);
}
