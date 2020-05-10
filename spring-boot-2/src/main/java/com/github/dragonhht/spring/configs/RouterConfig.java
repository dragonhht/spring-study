package com.github.dragonhht.spring.configs;

import com.github.dragonhht.spring.handler.HelloFunHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

/**
 * .
 *
 * @author: huang
 * @Date: 2019-4-12
 */
@Configuration
public class RouterConfig {

    @Autowired
    private HelloFunHandler helloFunHandler;

    @Bean
    public RouterFunction<ServerResponse> helloRouter() {
        return RouterFunctions.route(GET("/sayHello"), serverRequest -> helloFunHandler.sayHello(serverRequest))
                .andRoute(GET("/sayHello2"), helloFunHandler::sayHelloTwo)
                .andRoute(GET("/goodHello"), helloFunHandler::initiativeHello);
    }

}
