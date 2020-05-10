package spring.cloud.study2.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Config Server.
 *
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication_9998 {
    public static void main( String[] args ) {
        SpringApplication.run(ConfigServerApplication_9998.class, args);
    }
}
