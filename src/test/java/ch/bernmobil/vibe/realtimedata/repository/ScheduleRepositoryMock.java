package ch.bernmobil.vibe.realtimedata.repository;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

import ch.bernmobil.vibe.realtimedata.repository.mock.data.ScheduleMockData;
import ch.bernmobil.vibe.shared.entitiy.Schedule;
import java.util.Map;


public class ScheduleRepositoryMock extends RepositoryMock<Schedule, ScheduleRepository>{
    public ScheduleRepositoryMock(){
        super(ScheduleRepository.class, ScheduleMockData.getDataSource());
    }

    @Override
    protected void configureMock() {
        when(mock.getMappings()).thenReturn(ScheduleMockData.getMappingData());
        doCallRealMethod().when(mock).addScheduleId(anyList());
    }


}
