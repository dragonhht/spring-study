package com.github.dragonhht.redis;

import com.github.dragonhht.redis.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataRedisStudyApplicationTests {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOperations;
    @Resource(name = "redisTemplate")
    private HashOperations<String, byte[], byte[]> hashOperations;

    @Test
    public void contextLoads() {

        stringRedisTemplate.opsForValue().set("sprint:data:age", "18");

        String name  = stringRedisTemplate.opsForValue().get("sprint:data:name");
        System.out.println(name);

    }

    @Test
    public void testTemplate() {
        redisTemplate.opsForValue().set("spring:data:user", new User("dragonhht", 20));
        User user = (User) redisTemplate.opsForValue().get("spring:data:user");
        System.out.println(user);
    }

    @Test
    public void testOperations() {
        listOperations.leftPush("spring:data:list", "dragonhht");
        listOperations.leftPush("spring:data:list", "20");

        List<String> list = listOperations.range("spring:data:list", 0, -1);
        list.forEach(System.out::println);
    }

    @Test
    public void testCallback() {
        stringRedisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
                stringRedisConnection.set("spring:data:callback", "callback");
                System.out.println(stringRedisConnection.get("spring:data:callback"));
                return null;
            }
        });
    }

    @Test
    public void testHashMapper() {
        HashMapper<Object, byte[], byte[]> mapper = new ObjectHashMapper();
        Map<byte[], byte[]> user = mapper.toHash(new User("hello", 20));
        hashOperations.putAll("spring:data:hash", user);

        Map<byte[], byte[]> hash = hashOperations.entries("spring:data:hash");
        User u = (User) (mapper).fromHash(hash);
        System.out.println(u);
    }

    /**
     * pub/sub
     */
    @Test
    public void publish() {
        // 通过连接发送消息
        /*redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] msg = "publish send message".getBytes();
                byte[] channel = "spring:data:pub:sub".getBytes();
                redisConnection.publish(msg, channel);
                return null;
            }
        });*/

        // 使用RedisTemplate发送消息
        for (int i = 0; i < 10; i++) {
            stringRedisTemplate.convertAndSend("spring:data:pub:template", "redis send message: " + i);
        }
    }

    @Test
    public void subscribe() throws InterruptedException {
        while (true) {
            TimeUnit.MINUTES.sleep(3);
        }
    }

    /**
     * 事务
     */
    @Test
    public void testTransaction() {

        List<Object> results = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.multi();
                redisOperations.opsForValue().set("spring:data:transaction", "transaction");

                return redisOperations.exec();
            }
        });

        if (results != null) {
            System.out.println("值已添加: " + results.get(0));
        }
    }

    /**
     * 管道
     */
    @Test
    public void testPip() {
        stringRedisTemplate.executePipelined((RedisCallback) connection -> {
           StringRedisConnection redisConnection = (StringRedisConnection) connection;
           for (int i = 0; i < 30; i++) {
               ((StringRedisConnection) connection).sAdd("spring:data:pipeline", "message: " + i);
           }
           return null;
        });
    }
}
