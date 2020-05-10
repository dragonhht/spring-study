package com.github.dragonhht.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * .
 *
 * @author: huang
 * @Date: 2019-6-15
 */
@Component
public class Messagelistener extends MessageListenerAdapter {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = new String(message.getBody());
        String channel = new String(pattern);
        System.out.println("处理消息: " + channel + " :----: " + msg);
    }
}
