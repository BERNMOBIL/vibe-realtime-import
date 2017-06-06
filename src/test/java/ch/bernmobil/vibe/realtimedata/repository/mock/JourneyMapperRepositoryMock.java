package ch.bernmobil.vibe.realtimedata.repository.mock;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import ch.bernmobil.vibe.realtimedata.repository.JourneyMapperRepository;

import ch.bernmobil.vibe.realtimedata.repository.mock.data.JourneyMapperMockData;
import org.mockito.Mockito;


public class JourneyMapperRepositoryMock {
    JourneyMapperRepository mock;

    public JourneyMapperRepositoryMock(){
        mock = Mockito.mock(JourneyMapperRepository.class);
        configureMock();
    }

    protected void configureMock() {
        when(mock.getMappings()).thenReturn(JourneyMapperMockData.getMappingData());
        when(mock.findByGtfsTripId(anyString())).thenCallRealMethod();
    }

    public JourneyMapperRepository getMock() {
        return mock;
    }

}
