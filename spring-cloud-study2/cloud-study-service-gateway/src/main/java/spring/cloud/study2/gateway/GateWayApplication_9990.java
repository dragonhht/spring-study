package spring.cloud.study2.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import spring.cloud.study2.gateway.filter.AccessFilter;

/**
 * 服务网关.
 *
 */
@EnableZuulProxy
@SpringCloudApplication
public class GateWayApplication_9990 {
    public static void main( String[] args ) {
        SpringApplication.run(GateWayApplication_9990.class, args);
    }

    @Bean
    public AccessFilter getAccessFilter() {
        return new AccessFilter();
    }
}
