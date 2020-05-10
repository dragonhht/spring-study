# Spring Boot 2

## WebFlux的函数式开发模式

-   Spring MVC模式的开发示例(WebFlux的函数式开发模式由此示例修改)

```
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

}
```

-   使用WebFlux提供的函数式接口中的`HandlerFunction`和`RouterFunction`,来修改以上示例

    -   主要逻辑

    ```
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
    
    }
    ```
    
    -   配置路由
    
    ```java
    @Configuration
    public class RouterConfig {
    
        @Autowired
        private HelloFunHandler helloFunHandler;
    
        @Bean
        public RouterFunction<ServerResponse> helloRouter() {
            return RouterFunctions.route(GET("/sayHello"), serverRequest -> helloFunHandler.sayHello(serverRequest))
                    .andRoute(GET("/sayHello2"), helloFunHandler::sayHelloTwo); // 这种方式更简洁
        }
    
    }
    ```
    
    -   说明：
        
        > 以上的实现主要使用了`HandlerFunction`和`RouterFunction`接口,下面具体来看下这两个接口
        
        -   `HandlerFunction`接口: 该接口相当于之前在`Controller`中具体的处理方法(如第一个示例中的`hello`方法)，该接口有方法`Mono<T> handle(ServerRequest var1);`
        
        -   `RouterFunction`接口: 该接口为路由，相当于第一个示例中的`@GetMapping("/hello")`, 该接口有方法`Mono<HandlerFunction<T>> route(ServerRequest var1);`
        
## 服务器主动推送数据

### 使用函数式方式

-   主要业务

```
/**
     * 服务器每秒像客户端推送数据.
     * @param request
     * @return
     */
    public Mono<ServerResponse> initiativeHello(ServerRequest request) {
        // TEXT_EVENT_STREAM会让客户端保持该连接
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
                .body(
                        // 使用interval每秒一个的数据流
                        Flux.interval(Duration.ofSeconds(1)).
                        map(msg -> "hello 3: " + new Date()), String.class);
    }
```

-   添加路由

```
@Bean
    public RouterFunction<ServerResponse> helloRouter() {
        return RouterFunctions.route(GET("/sayHello"), serverRequest -> helloFunHandler.sayHello(serverRequest))
                .andRoute(GET("/sayHello2"), helloFunHandler::sayHelloTwo)
                // 主动推送数据路由
                .andRoute(GET("/goodHello"), helloFunHandler::initiativeHello);
    }
```

### 使用注解方式

```
    @GetMapping(value = "/hello2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> hello2() {
        // 使用interval每秒一个的数据流
        return Flux.interval(Duration.ofSeconds(1)).
                map(msg -> "hello 2: " + new Date());
    }
```

## 响应式Spring Data

> 支持响应式数据访问的数据库有： `MongoDB`、`Redis`、`Apache Cassandra`和`CouchDB`  
> 使用时需添加相关的响应式依赖

-   示例

    -   示例使用Mongodb，所以添加相关依赖
    
    ```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb-reactive</artifactId>
    </dependency>
    ```
    
    -   DAO层(继承`ReactiveCrudRepository`接口，此处继承他的子接口`ReactiveMongoRepository`)
    
    ```java
    @Repository
    public interface UserRepository extends ReactiveMongoRepository<User, String> {
    }
    ```
    
    -   Service层：Service层只是简单的调用DAO层的接口，[代码](./src/main/java/com/github/dragonhht/spring/service/simple.UserService.java)
    
    -   Controller层:
    
    ```java
    @RestController
    @RequestMapping("/user")
    public class UserController {
    
        @Autowired
        private simple.UserService userService;
    
        @PostMapping("")
        public Mono<User> save(User user) {
            return userService.save(user);
        }
    
        @GetMapping("/{id}")
        public Mono<User> findById(@PathVariable("id") String id) {
            return userService.findUserById(id);
        }
    
        @GetMapping(value = "", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
        public Flux<User> findAll() {
            // 数据每秒一个
            return userService.findAllUser().delayElements(Duration.ofSeconds(1));
        }
    
        @DeleteMapping("/{id}")
        public Mono<Void> delById(@PathVariable("id") String id) {
            return userService.delUserById(id);
        }
    
    }
    ```

    > 以上`UserController`中的`findAll()`方法使用将所有的User信息按每秒一个输出
    