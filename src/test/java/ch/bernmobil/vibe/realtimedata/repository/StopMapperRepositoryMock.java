package ch.bernmobil.vibe.realtimedata.repository;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import ch.bernmobil.vibe.shared.mapping.StopMapping;
import ch.bernmobil.vibe.shared.mockdata.StopMapperMockData;


public class StopMapperRepositoryMock extends RepositoryMock<StopMapping, StopMapperRepository> {
    public StopMapperRepositoryMock() {
        super(StopMapperRepository.class, StopMapperMockData.getDataSource());
    }

    @Override
    protected void configureMock() {
        when(mock.getEntries()).thenReturn(StopMapperMockData.getMappingData());
        when(mock.findByGtfsId(anyString())).thenCallRealMethod();
    }
}
