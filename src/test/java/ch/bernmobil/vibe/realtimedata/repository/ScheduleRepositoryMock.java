package ch.bernmobil.vibe.realtimedata.repository;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;


import ch.bernmobil.vibe.shared.entity.Schedule;
import ch.bernmobil.vibe.shared.mockdata.ScheduleMockData;


public class ScheduleRepositoryMock extends RepositoryMock<Schedule, ScheduleRepository>{
    public ScheduleRepositoryMock(){
        super(ScheduleRepository.class, ScheduleMockData.getDataSource());
    }

    @Override
    protected void configureMock() {
        when(mock.getEntries()).thenReturn(ScheduleMockData.getMappingData());
        doCallRealMethod().when(mock).addScheduleId(anyList());
        doCallRealMethod().when(mock).load(any());
    }


}
