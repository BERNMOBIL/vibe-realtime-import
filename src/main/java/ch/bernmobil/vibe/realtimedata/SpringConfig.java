package ch.bernmobil.vibe.realtimedata;

import javax.sql.DataSource;

import ch.bernmobil.vibe.shared.UpdateHistoryRepository;
import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.UpdateManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.time.Duration;

@Configuration
public class SpringConfig {

    @Value("classpath:/clean-previous-realtime-updates.sql")
    private Resource cleanPreviousUpdatesScript;

    private Environment environment;

    @Bean(name = "StaticDataSource")
    public DataSource staticDataSource() {
        return createDataSource(
            "org.postgresql.Driver",
            environment.getProperty("spring.datasource.url"),
            environment.getProperty("spring.datasource.username"),
            environment.getProperty("spring.datasource.password")
        );
    }

    @Primary
    @Bean(name = "MapperDataSource")
    public DataSource mapperDataSource() {
        return createDataSource(
            "org.postgresql.Driver",
            environment.getProperty("bernmobil.mappingrepository.datasource.url"),
            environment.getProperty("bernmobil.mappingrepository.datasource.username"),
            environment.getProperty("bernmobil.mappingrepository.datasource.password")
        );
    }

    @Bean
    public UpdateManager updateManager(@Qualifier("MapperRepository") UpdateManagerRepository mapperRepository,
                                       @Qualifier("StaticRepository") UpdateManagerRepository staticRepository,
                                       UpdateHistoryRepository updateHistoryRepository) {
        int historySize = environment.getProperty("bernmobil.history.size", Integer.class);
        Duration timeout = Duration.ofMinutes(environment.getProperty("bernmobil.history.timeout-duration", Long.class));
        return new UpdateManager(mapperRepository, staticRepository, updateHistoryRepository, historySize, timeout);
    }

    @Bean(name = "MapperRepository")
    public UpdateManagerRepository mapperRepository(@Qualifier("MapperDataSource") DataSource mapperDataSource) {
        return new UpdateManagerRepository(new JdbcTemplate(mapperDataSource));
    }

    @Bean(name = "StaticRepository")
    public UpdateManagerRepository staticRepository(@Qualifier("StaticDataSource") DataSource staticDataSource) {
        return new UpdateManagerRepository(new JdbcTemplate(staticDataSource));
    }

    @Bean
    public UpdateHistoryRepository updateHistoryRepository(@Qualifier("StaticDataSource")DataSource dataSource) {
        return new UpdateHistoryRepository(dataSource);
    }

    private DataSource createDataSource(String driverClassName, String url) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        return dataSource;
    }

    private DataSource createDataSource(String driverClassName, String url, String username, String password) {
        DriverManagerDataSource dataSource = (DriverManagerDataSource) createDataSource(driverClassName, url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
