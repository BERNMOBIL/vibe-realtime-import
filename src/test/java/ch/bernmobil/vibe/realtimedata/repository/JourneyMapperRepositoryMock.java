package ch.bernmobil.vibe.realtimedata.repository;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import ch.bernmobil.vibe.realtimedata.repository.JourneyMapperRepository;

import ch.bernmobil.vibe.realtimedata.repository.mock.data.JourneyMapperMockData;
import ch.bernmobil.vibe.shared.mapping.JourneyMapping;
import java.util.Map;
import org.mockito.Mockito;


public class JourneyMapperRepositoryMock extends RepositoryMock<JourneyMapping, JourneyMapperRepository>{
    public JourneyMapperRepositoryMock(){
        super(JourneyMapperRepository.class, JourneyMapperMockData.getDataSource());
    }

    @Override
    protected void configureMock() {
        when(mock.getMappings()).thenReturn(JourneyMapperMockData.getMappingData());
        when(mock.findByGtfsTripId(anyString())).thenCallRealMethod();
    }
}
