package ch.bernmobil.vibe.realtimedata.repository;

import java.util.List;
import org.mockito.Mockito;

public abstract class RepositoryMock<E, R> {
    protected R mock;
    protected final List<E> dataSource;


    public RepositoryMock(Class<R> classType, List<E> dataSource){
        mock = Mockito.mock(classType);
        this.dataSource = dataSource;
        configureMock();
    }


    protected abstract void configureMock();

    public R getMock() {
        return mock;
    }

}
