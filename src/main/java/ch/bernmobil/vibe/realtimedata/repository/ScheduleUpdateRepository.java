package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.entity.ScheduleUpdate;
import ch.bernmobil.vibe.realtimedata.entity.sync.AreaMapper;
import org.springframework.data.repository.CrudRepository;

public interface ScheduleUpdateRepository extends CrudRepository<ScheduleUpdate, Long> {
}
