package ch.bernmobil.vibe.realtimedata;

import javax.sql.DataSource;
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
@PropertySource("classpath:/application.properties")
public class SpringConfig {

    @Value("classpath:/clean-previous-realtime-updates.sql")
    private Resource cleanPreviousUpdatesScript;

    @Autowired
    private Environment environment;

    @Bean("StaticDataSource")
    public DataSource staticDataSource() {
        return createDataSource(
            "org.postgresql.Driver",
            environment.getProperty("bernmobil.static.datasource.url"),
            environment.getProperty("bernmobil.static.datasource.username"),
            environment.getProperty("bernmobil.static.datasource.password")
        );
    }

    @Primary
    @Bean("MapperDataSource")
    public DataSource mapperDataSource() {
        return createDataSource(
            "org.postgresql.Driver",
            environment.getProperty("bernmobil.mappings.datasource.url"),
            environment.getProperty("bernmobil.mappings.datasource.username"),
            environment.getProperty("bernmobil.mappings.datasource.password")
        );
    }

    @Bean("PostgresInitializer")
    public DataSourceInitializer postgresInitializer(@Qualifier("StaticDataSource") DataSource dataSource) {
        return dataSourceInitializer(dataSource, cleanPreviousUpdatesScript);
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
