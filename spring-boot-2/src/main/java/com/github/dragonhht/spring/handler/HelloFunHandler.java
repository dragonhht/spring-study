package com.github.dragonhht.spring.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

/**
 * 业务主要处理逻辑.
 *
 * @author: huang
 * @Date: 2019-4-12
 */
@Component
public class HelloFunHandler {

    /**
     *  hello主要处理逻辑
     * @return
     */
    public Mono<ServerResponse> sayHello(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(Mono.just("hello: " + new Date()), String.class);
    }

    public Mono<ServerResponse> sayHelloTwo(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(Mono.just("hello 2: " + new Date()), String.class);
    }

    /**
     * 服务器每秒像客户端推送数据.
     * @param request
     * @return
     */
    public Mono<ServerResponse> initiativeHello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
                .body(
                        // 使用interval每秒一个的数据流
                        Flux.interval(Duration.ofSeconds(1)).
                        map(msg -> "hello 3: " + new Date()), String.class);
    }

}
