package ch.bernmobil.vibe.realtimedata.repository;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import ch.bernmobil.vibe.realtimedata.repository.StopMapperRepository;
import ch.bernmobil.vibe.realtimedata.repository.mock.data.StopMapperMockData;
import org.mockito.Mockito;


public class StopMapperRepositoryMock {
    StopMapperRepository mock;

    public StopMapperRepositoryMock(){
        mock = Mockito.mock(StopMapperRepository.class);
        configureMock();
    }

    protected void configureMock() {
        when(mock.getMappings()).thenReturn(StopMapperMockData.getMappingData());
        when(mock.findByGtfsId(anyString())).thenCallRealMethod();
    }

    public StopMapperRepository getMock() {
        return mock;
    }

}
