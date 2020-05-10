package com.github.dragonhht.study.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

/**
 * WebSocket处理类(声明周期).
 *
 * @author: huang
 * @Date: 2019-4-15
 */
@Slf4j
@Component
public class WSHandler implements WebSocketHandler {

    /**
     * 建立连接.
     * @param webSocketSession
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        log.info("WebSocket客户端接入: " + webSocketSession.getId());
    }

    /**
     * 接收并处理消息.
     * @param webSocketSession
     * @param webSocketMessage
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        log.info(webSocketSession.getId() + " 接收到信息: " + ((TextMessage)webSocketMessage).toString());
        // 将消息发送给指定用户，此处返送给自己
        webSocketSession.sendMessage(webSocketMessage);
    }

    /**
     * 出错时调用.
     * @param webSocketSession
     * @param throwable
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        log.error(webSocketSession.getId() + " 调用出错: " + throwable.getMessage());
    }

    /**
     * 关闭连接.
     * @param webSocketSession
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        log.info(webSocketSession.getId() + " 已关闭连接: " + closeStatus.toString());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
