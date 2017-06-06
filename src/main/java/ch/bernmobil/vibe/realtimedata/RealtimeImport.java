package ch.bernmobil.vibe.realtimedata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class RealtimeImport {

    /**
     * SpringApplication runner and Java entry-point in this project
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(RealtimeImport.class, args);
    }
}
