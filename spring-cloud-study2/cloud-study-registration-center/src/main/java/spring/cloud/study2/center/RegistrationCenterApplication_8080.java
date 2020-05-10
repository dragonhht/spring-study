package spring.cloud.study2.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 服务注册中心.
 *
 */
@SpringBootApplication
@EnableEurekaServer     // 启动一个服务注册中心提供给其他应用进行对话
public class RegistrationCenterApplication_8080 {
    public static void main( String[] args ) {
        SpringApplication.run(RegistrationCenterApplication_8080.class, args);
    }
}
