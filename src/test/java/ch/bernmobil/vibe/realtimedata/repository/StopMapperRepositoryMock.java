package ch.bernmobil.vibe.realtimedata.repository;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import ch.bernmobil.vibe.realtimedata.repository.StopMapperRepository;
import ch.bernmobil.vibe.realtimedata.repository.mock.data.StopMapperMockData;
import ch.bernmobil.vibe.shared.mapping.StopMapping;
import java.util.Map;
import org.mockito.Mockito;


public class StopMapperRepositoryMock extends RepositoryMock<StopMapping, StopMapperRepository> {
    public StopMapperRepositoryMock() {
        super(StopMapperRepository.class, StopMapperMockData.getDataSource());
    }

    @Override
    protected void configureMock() {
        when(mock.getMappings()).thenReturn(StopMapperMockData.getMappingData());
        when(mock.findByGtfsId(anyString())).thenCallRealMethod();
    }
}
