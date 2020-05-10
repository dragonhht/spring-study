package spring.cloud.study2.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 服务提供者.
 *
 * @author: huang
 * Date: 18-2-12
 */
@SpringBootApplication
@EnableEurekaClient
public class ServiceApplication_8081 {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication_8081.class, args);
    }

}
