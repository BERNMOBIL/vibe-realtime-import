package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.entity.sync.AreaMapper;
import ch.bernmobil.vibe.realtimedata.entity.sync.JourneyMapper;
import org.springframework.data.repository.CrudRepository;

public interface JourneyMapperRepository extends CrudRepository<JourneyMapper, Long> {
    JourneyMapper findFirstByGtfsTripId(String gtfsTripId);
}
