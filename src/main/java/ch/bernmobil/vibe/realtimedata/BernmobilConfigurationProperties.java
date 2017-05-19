package ch.bernmobil.vibe.realtimedata;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URL;

public class BernmobilConfigurationProperties {

    @ConfigurationProperties(prefix = "bernmobil.jobrepository")
    public class JobRepository {

        /**
         * Value of the datasource of the repository where logs of Spring Batch will be stored
         */
        private String datasource;

        public String getDatasource() {
            return datasource;
        }

        public void setDatasource(String datasource) {
            this.datasource = datasource;
        }
    }


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
         * Defines how many different import versions are stored in the database
         */
        private int size;
        /**
         * Defines how long the realtime service in minutes
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
}
