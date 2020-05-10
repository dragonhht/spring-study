package com.github.dragonhht.study.config;

import com.github.dragonhht.study.handler.WSHandler;
import com.github.dragonhht.study.interceptor.MyWebSocketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * .
 *
 * @author: huang
 * @Date: 2019-4-15
 */
@Configuration
@EnableWebSocket
public class HandlerConfig implements WebSocketConfigurer{

    @Autowired
    private WSHandler wsHandler;
    @Autowired
    private MyWebSocketInterceptor myWebSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(wsHandler, "/websocket")
                .addInterceptors(myWebSocketInterceptor);
    }
}
