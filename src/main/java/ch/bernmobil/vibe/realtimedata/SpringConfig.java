package ch.bernmobil.vibe.realtimedata;

import java.net.MalformedURLException;
import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:/application.properties")
public class SpringConfig {

    @Value("clean-previous-realtime-updates.sql")
    private Resource cleanPreviousUpdatesScript;

    @Value("org/springframework/batch/core/schema-drop-sqlite.sql")
    private Resource dropBatchTables;

    @Value("org/springframework/batch/core/schema-sqlite.sql")
    private Resource createBatchSchema;

    @Autowired
    private Environment environment;

    @Primary
    @Bean
    public DataSource batchDataSource() {
        return createDataSource(
            environment.getProperty("bernmobil.static.batch.driver"),
            environment.getProperty("bernmobil.static.batch.datasource")
        );
    }

    @Bean("StaticDataSource")
    public DataSource staticDataSource() {
        return createDataSource(
            environment.getProperty("bernmobil.static.datasource.driver"),
            environment.getProperty("bernmobil.static.datasource.url"),
            environment.getProperty("bernmobil.static.datasource.username"),
            environment.getProperty("bernmobil.static.datasource.password")
        );
    }

    @Bean("MapperDataSource")
    public DataSource mapperDataSource() {
        return createDataSource(
            environment.getProperty("bernmobil.mappings.datasource.driver"),
            environment.getProperty("bernmobil.mappings.datasource.url"),
            environment.getProperty("bernmobil.mappings.datasource.username"),
            environment.getProperty("bernmobil.mappings.datasource.password")
        );
    }

    @Bean("PostgresInitializer")
    public DataSourceInitializer postgresInitializer(@Qualifier("StaticDataSource") DataSource dataSource) {
        return dataSourceInitializer(dataSource, cleanPreviousUpdatesScript);
    }

    @Bean("JobRepositoryInitializer")
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) throws MalformedURLException {
        return dataSourceInitializer(dataSource, dropBatchTables, createBatchSchema);
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

    private DataSourceInitializer dataSourceInitializer(DataSource dataSource, Resource... sqlScripts) {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();

        for(Resource resource : sqlScripts){
            databasePopulator.addScript(resource);
        }

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator);

        return initializer;
    }
}
