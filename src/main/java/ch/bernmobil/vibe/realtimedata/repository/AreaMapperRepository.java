package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.entity.sync.AreaMapper;
import org.springframework.data.repository.CrudRepository;

public interface AreaMapperRepository extends CrudRepository<AreaMapper, Long> {
    AreaMapper findFirstByGtfsId(String gtfsId);
}
