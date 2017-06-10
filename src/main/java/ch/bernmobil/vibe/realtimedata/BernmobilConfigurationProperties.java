package ch.bernmobil.vibe.realtimedata;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URL;

/**
 * Class to define configuration properties and help the Spring Configuration Processor to determine the type and its
 * converters for a custom property.
 *
 * @author Oliviero Chiodo
 * @author Matteo Patisso
 */
public class BernmobilConfigurationProperties {

    @ConfigurationProperties(prefix = "bernmobil.mappingrepository.datasource")
    public class MappingRepository {

        /**
         * Url of the datasource where the mapping data should be stored
         */
        private String url;
        /**
         * Username for the mapping datasource
         */
        private String username;
        /**
         * Password for the mapping datasource
         */
        private String password;
        /**
         * Classname of the database driver used for the mapping repository
         */
        private String driverClassName;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }
    }

    @ConfigurationProperties(prefix = "bernmobil.realtime-source")
    public class RealtimeSource {
        /**
         * The API where realtime data are fetched
         */
        private URL url;

        public URL getUrl() {
            return url;
        }

        public void setUrl(URL url) {
            this.url = url;
        }
    }

    @ConfigurationProperties(prefix = "bernmobil.history")
    public class History {
        /**
         * Defines how many different import versions of static data are stored in the database
         */
        private int size;
        /**
         * Defines the amount of time, from when a running instance is considered as failed.
         */
        private long timeoutDuration;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public long getTimeoutDuration() {
            return timeoutDuration;
        }

        public void setTimeoutDuration(long timeoutDuration) {
            this.timeoutDuration = timeoutDuration;
        }
    }

    @ConfigurationProperties(prefix = "bernmobil.locale")
    public class Locale {

        /**
         * The timezone in which the departures are taking place. Refer to {@link java.time.ZoneId}
         * for information about valid values
         */
        private String timezone;

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }
    }

    @ConfigurationProperties(prefix = "bernmobil.import")
    public class Import {
        /**
         * Integer value in milliseconds for the delay between the end of a realtime import and the beginning of the
         * next one.
         */
        private int schedule;

        public int getSchedule() {
            return schedule;
        }

        public void setSchedule(int schedule) {
            this.schedule = schedule;
        }
    }
}
