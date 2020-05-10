package spring.cloud.study2.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 使用Ribbon实现客户端负载均衡的消费者.
 *
 */
@EnableEurekaClient
@SpringBootApplication
@EnableCircuitBreaker
public class ConsumerApplication_9999 {
    public static void main( String[] args ) {
        SpringApplication.run(ConsumerApplication_9999.class, args);
    }

    /**
     * 创建RestTemplate,并开启负载均衡.
     */
    @Bean
    @LoadBalanced
    public RestTemplate gerRestTemplate() {
        return new RestTemplate();
    }
}
