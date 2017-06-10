package ch.bernmobil.vibe.realtimedata;

import ch.bernmobil.vibe.shared.UpdateHistoryRepository;
import ch.bernmobil.vibe.shared.UpdateManager;
import ch.bernmobil.vibe.shared.UpdateManagerRepository;
import ch.bernmobil.vibe.shared.UpdateTimestampManager;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
public class SpringConfig {

    @Value("classpath:/clean-previous-realtime-updates.sql")
    private Resource cleanPreviousUpdatesScript;

    private Environment environment;


    @Bean
    public UpdateManager updateManager(@Qualifier("MapperRepository") UpdateManagerRepository mapperRepository,
                                       @Qualifier("StaticRepository") UpdateManagerRepository staticRepository,
                                       UpdateTimestampManager updateTimestampManager,
                                       UpdateHistoryRepository updateHistoryRepository) {
        int historySize = environment.getProperty("bernmobil.history.size", Integer.class);
        Duration timeout = Duration.ofMinutes(environment.getProperty("bernmobil.history.timeout-duration", Long.class));
        return new UpdateManager(mapperRepository, staticRepository, updateHistoryRepository, historySize, timeout, updateTimestampManager);
    }

    @Bean(name = "MapperRepository")
    public UpdateManagerRepository mapperRepository(@Qualifier("MapperDslContext") DSLContext dslContext) {
        return new UpdateManagerRepository(dslContext);
    }

    @Bean(name = "StaticRepository")
    public UpdateManagerRepository staticRepository(@Qualifier("StaticDslContext")DSLContext dslContext) {
        return new UpdateManagerRepository(dslContext);
    }

    @Bean
    public UpdateHistoryRepository updateHistoryRepository(@Qualifier("StaticDslContext") DSLContext dslContext) {
        return new UpdateHistoryRepository(dslContext);
    }

    @Bean
    public UpdateTimestampManager updateTimestampManager(){
        return new UpdateTimestampManager();
    }

    @Bean(name = "StaticDslContext")
    public DSLContext staticDslContext() {
        DataSource dataSource = createDataSource(
            environment.getProperty("spring.datasource.driver-class-name"),
            environment.getProperty("spring.datasource.url"),
            environment.getProperty("spring.datasource.username"),
            environment.getProperty("spring.datasource.password")
        );
        return getDslContext(dataSource);
    }

    @Bean(name = "MapperDslContext")
    public DSLContext mapperDslContext() {
        DataSource dataSource = createDataSource(
            environment.getProperty("bernmobil.mappingrepository.datasource.driver-class-name"),
            environment.getProperty("bernmobil.mappingrepository.datasource.url"),
            environment.getProperty("bernmobil.mappingrepository.datasource.username"),
            environment.getProperty("bernmobil.mappingrepository.datasource.password")
        );
        return getDslContext(dataSource);
    }

    private DSLContext getDslContext(DataSource dataSource) {
        String dialectString = environment.getProperty("spring.jooq.sql-dialect").toUpperCase();
        SQLDialect dialect = SQLDialect.valueOf(dialectString);
        return DSL.using(dataSource, dialect);
    }

    private DataSource createDataSource(String driver, String url, String username, String password) {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.driverClassName(driver);
        builder.url(url);
        builder.username(username);
        builder.password(password);
        return builder.build();
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
