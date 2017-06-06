package ch.bernmobil.vibe.realtimedata.repository;

import ch.bernmobil.vibe.realtimedata.ImportRunner;
import ch.bernmobil.vibe.realtimedata.repository.mock.JourneyMapperRepositoryMock;
import ch.bernmobil.vibe.realtimedata.repository.mock.RealtimeUpdateRepositoryMock;
import ch.bernmobil.vibe.realtimedata.repository.mock.ScheduleRepositoryMock;
import ch.bernmobil.vibe.realtimedata.repository.mock.StopMapperRepositoryMock;
import ch.bernmobil.vibe.shared.UpdateHistoryRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

@Configuration
@Profile("testConfiguration")
public class TestConfiguration {

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ScheduleRepository scheduleRepository() {
        return new ScheduleRepositoryMock().getMock();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public RealtimeUpdateRepository realtimeUpdateRepository() {
        return new RealtimeUpdateRepositoryMock().getMock();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public JourneyMapperRepository journeyMapperRepository() {
        return new JourneyMapperRepositoryMock().getMock();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public StopMapperRepository stopMapperRepository() {
        return new StopMapperRepositoryMock().getMock();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ScheduleUpdateRepository scheduleUpdateRepository() {
        return Mockito.mock(ScheduleUpdateRepository.class);
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ImportRunner importRunner() {
        return new ImportRunner(
            journeyMapperRepository(),
            stopMapperRepository(),
            scheduleRepository(),
            scheduleUpdateRepository(),
            realtimeUpdateRepository(),
            Mockito.mock(UpdateHistoryRepository.class)
        );
    }



/*
    @Primary
    @Bean(name = "StaticDataSource")
    public DataSource staticDataSource() {
        DataSource dataSource = createDataSource("driver","url","username","password");
        return dataSource;
    }


    private DataSource createDataSource(String driver, String url, String username, String password) {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.driverClassName(driver);
        builder.url(url);
        builder.username(username);
        builder.password(password);
        return builder.build();
    }*/
}
