# Spring-data-redis

## 连接工厂

###   `Lettuce`连接工厂

> Lettuce基于Netty的连接实例（StatefulRedisConnection），可以在多个线程间并发访问，且线程安全，满足多线程环境下的并发访问，同时它是可伸缩的设计，一个连接实例不够的情况也可以按需增加连接实例。

#### Java编码方式

```
@Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisConfiguration redisConfiguration = new RedisStandaloneConfiguration("my.dragon.com", 6379);
        return new LettuceConnectionFactory(redisConfiguration);
    }
```

#### 配置文件方式

```yaml
spring:
  redis:
    host: my.dragon.com
    port: 6379
    timeout: 5000
    database: 0
    # lettuce相关配置
    lettuce:
      pool:
        max-active: 8
        max-wait: -1
        max-idle: 8
        min-idle: 0
```

-   `Jedis`连接工厂

> Jedis在实现上是直连redis server，多线程环境下非线程安全，除非使用连接池，为每个Jedis实例增加物理连接。

#### Java编码方式

```
// 使用该方法需引入Jedis依赖
@Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration("my.dragon.com", 6379);
        return new JedisConnectionFactory(redisConfiguration);
    }
``` 

## 自定义`RedisTemplate`

-   可将可序列化对象转成json后处理

```
@Bean
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
```

-   使用

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataRedisStudyApplicationTests {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Test
    public void testTemplate() {
        redisTemplate.opsForValue().set("spring:data:user", new User("dragonhht", 20));
        User user = (User) redisTemplate.opsForValue().get("spring:data:user");
        System.out.println(user);
    }

}
```

## 直接使用Redis相关类型的操作类

-   此处以列表为例

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataRedisStudyApplicationTests {

    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOperations;

    @Test
    public void testOperations() {
        listOperations.leftPush("spring:data:list", "dragonhht");
        listOperations.leftPush("spring:data:list", "20");

        List<String> list = listOperations.range("spring:data:list", 0, -1);
        list.forEach(System.out::println);
    }

}
```

## 通过`RedisTemplate`或`StringRedisTemplate`直接获取连接操作

```
@Test
    public void testCallback() {
        stringRedisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                // 当使用stringRedisTemplate时，可以转换为StringRedisConnection
                StringRedisConnection stringRedisConnection = (StringRedisConnection) redisConnection;
                stringRedisConnection.set("spring:data:callback", "callback");
                System.out.println(stringRedisConnection.get("spring:data:callback"));
                return null;
            }
        });
    }
```

## 哈希映射器

-   多种实现方式

    -   `BeanUtilsHashMapper`
    
    -   `ObjectHashMapper`
    
    -   `Jackson2HashMapper`

-   示例

```
@Test
    public void testHashMapper() {
        HashMapper<Object, byte[], byte[]> mapper = new ObjectHashMapper();
        Map<byte[], byte[]> user = mapper.toHash(new User("hello", 20));
        hashOperations.putAll("spring:data:hash", user);

        Map<byte[], byte[]> hash = hashOperations.entries("spring:data:hash");
        User u = (User) (mapper).fromHash(hash);
        System.out.println(u);
    }
```

## 发布/订阅模式

-   发布者

    -   通过使用`convertAndSend`方法发布信息

    ```
    stringRedisTemplate.convertAndSend("spring:data:pub:template", "message");
    ```
    
    -   直接通过连接发布
    
    ```
    redisTemplate.execute(new RedisCallback<Object>() {
        @Override
        public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
            byte[] msg = "publish send message".getBytes();
            byte[] channel = "spring:data:pub:sub".getBytes();
            redisConnection.publish(msg, channel);
            return null;
        }
    });
    ```
    
-   订阅者

1.  创建监听器
    
    ```java
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
    ```
    
2.  配置监听
    
    ```java
    @Configuration
    public class RedisConfig {
    
        @Autowired
        private RedisConnectionFactory redisConnectionFactory;
        @Autowired
        private Messagelistener messagelistener;
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
    }
    ```

## 事务

-   redis的事务提供了`multi`、`exec`和`discard`等命令

-   在Spring Data Redis中可使用`RedisTemplate`操作事务,但`RedisTemplate`不保证使用相同的连接执行所有事务操作

-   Spring Data Redis提供了SessionCallback获取在需要执行多个操作时的`connection`

```
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
```

-   使用`@Transactional`注解

    -   要使用`@Transactional`注解启动事务，需使用`setEnableTransactionSupport(true)`开始事务支持
    
    ```java
    @Configuration
    public class RedisConfig {
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
    }
    ```

## 管道

-   当需要连续多次发送该操作命令时，使用管可提高性能

```
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
```
