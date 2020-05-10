package com.github.dragonhht.spring.constoller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

/**
 * .
 *
 * @author: huang
 * @Date: 2019-4-12
 */
@RestController
public class IndexController {

    /**
     * 使用Spring MVC的注解实现，返回类型采用响应式类型
     * @return
     */
    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("hello " + new Date());
    }

    /*HandlerFunction<ServerResponse> helloFun =
            serverRequest -> ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(
                    Mono.just("hello " + new Date()), String.class
            );

    RouterFunction<ServerResponse> helloRouter =
            RouterFunctions.route(GET("/sayHello"), helloFun);*/

    @GetMapping(value = "/hello2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> hello2() {
        // 使用interval每秒一个的数据流
        return Flux.interval(Duration.ofSeconds(1)).
                map(msg -> "hello 2: " + new Date());
    }

}
