package ch.bernmobil.vibe.realtimedata.repository.mock;

import java.util.List;
import org.mockito.Mockito;

public abstract class RepositoryMock<EntityType, RepositoryType> {
    protected RepositoryType mock;
    protected final List<EntityType> dataSource;


    public RepositoryMock(Class<RepositoryType> classType, List<EntityType> dataSource){
        mock = Mockito.mock(classType);
        this.dataSource = dataSource;
        configureMock();
    }

    protected abstract void configureMock();

    public RepositoryType getMock() {
        return mock;
    }

}
