package ch.bernmobil.vibe.realtimedata;

import ch.bernmobil.vibe.realtimedata.repository.JourneyMapperRepository;
import ch.bernmobil.vibe.realtimedata.repository.JourneyMapperRepositoryMock;
import ch.bernmobil.vibe.realtimedata.repository.RealtimeUpdateRepository;
import ch.bernmobil.vibe.realtimedata.repository.RealtimeUpdateRepositoryMock;
import ch.bernmobil.vibe.realtimedata.repository.ScheduleRepository;
import ch.bernmobil.vibe.realtimedata.repository.ScheduleRepositoryMock;
import ch.bernmobil.vibe.realtimedata.repository.ScheduleUpdateRepository;
import ch.bernmobil.vibe.realtimedata.repository.StopMapperRepository;
import ch.bernmobil.vibe.realtimedata.repository.StopMapperRepositoryMock;
import ch.bernmobil.vibe.shared.UpdateHistoryRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

@Configuration
@Profile("testConfiguration")
public class TestConfiguration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ScheduleRepository scheduleRepository() {
        return new ScheduleRepositoryMock().getMock();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public RealtimeUpdateRepository realtimeUpdateRepository() {
        return new RealtimeUpdateRepositoryMock().getMock();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public JourneyMapperRepository journeyMapperRepository() {
        return new JourneyMapperRepositoryMock().getMock();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public StopMapperRepository stopMapperRepository() {
        return new StopMapperRepositoryMock().getMock();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ScheduleUpdateRepository scheduleUpdateRepository() {
        return Mockito.mock(ScheduleUpdateRepository.class);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public UpdateHistoryRepository updateHistoryRepository() {
        return Mockito.mock(UpdateHistoryRepository.class);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ImportRunner importRunner() {
        return new ImportRunner(
            journeyMapperRepository(),
            stopMapperRepository(),
            scheduleRepository(),
            scheduleUpdateRepository(),
            realtimeUpdateRepository(),
            updateHistoryRepository()
        );
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public DSLContext mockedDslContext() {
        MockDataProvider provider = new MockProvider();
        MockConnection connection = new MockConnection(provider);
        return DSL.using(connection, SQLDialect.POSTGRES);
    }
}
