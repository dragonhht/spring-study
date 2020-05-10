package com.github.dragonhht.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.collections.DefaultRedisList;
import org.springframework.data.redis.support.collections.RedisList;

import java.io.Serializable;

/**
 * Redis配置(使用Java类配置).
 *
 * @author: huang
 * @Date: 2019-6-13
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private Messagelistener messagelistener;

    /**
     * 创建Lettuce生产工厂.
     *
     * Lettuce和Jedis的都是连接Redis Server的客户端程序。
     * Jedis在实现上是直连redis server，多线程环境下非线程安全，除非使用连接池，为每个Jedis实例增加物理连接。
     * Lettuce基于Netty的连接实例（StatefulRedisConnection），可以在多个线程间并发访问，且线程安全，
     * 满足多线程环境下的并发访问，同时它是可伸缩的设计，一个连接实例不够的情况也可以按需增加连接实例。
     */
    /*@Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisConfiguration redisConfiguration = new RedisStandaloneConfiguration("my.dragon.com", 6379);
        return new LettuceConnectionFactory(redisConfiguration);
    }*/

    /*@Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration("my.dragon.com", 6379);
        return new JedisConnectionFactory(redisConfiguration);
    }*/

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        // 事务支持
        template.setEnableTransactionSupport(true);
        return template;
    }

    /**
     * 定义监听器
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        // 定义监听渠道
        Topic topic = new ChannelTopic("spring:data:pub:template");

        // 添加消息监听
        container.addMessageListener(messagelistener, topic);

        return container;
    }

    @Bean
    public RedisCacheManager redisCacheManager(LettuceConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.create(redisConnectionFactory);
    }


}
