# Spring WebSocket

-   添加依赖

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

## 广播

-   编写端点

    ```java
    @Controller
    public class HelloEndpoint {
    
        @MessageMapping("/hello")
        @SendTo("/topic/hello")
        public User Hello(@RequestBody User user) {
            user.setId(new Random().nextInt(100));
            user.setName("name_" + user.getName());
            return user;
        }
    
    }
    ```

    -   说明
    
        -   使用`Controller`注解标注类，将类交由Spring容器管理
        
        -   使用`@MessageMapping`注解标注方法，该注解类似与`@GetMapping`、`PostMapping`等注解，用于配置路由
        
        -   使用`@SendTo`注解，该注解可设置放回信息的目的地，即，用户可以通过该注解配置的url获取到该请求返回的数据
        
-   配置

    ```java
    @Configuration
    @EnableWebSocketMessageBroker
    public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            // 设置端点
            registry.addEndpoint("/user-hello")
                    // 跨域设置
                    .setAllowedOrigins("**")
                    // 启用SockJS
                    .withSockJS();
        }
    
        @Override
        public void configureMessageBroker(MessageBrokerRegistry registry) {
            // 目的地前缀
            registry.enableSimpleBroker("/topic");
            // 应用程序前缀
            registry.setApplicationDestinationPrefixes("/ws");
        }
    }
    ```

    -   说明
    
        -   使用注解`@Configuration`表示该类为Spring的配置类
        
        -   使用`@EnableWebSocketMessageBroker`注解，配置WebSocket和基于STOMP代理的消息
        
        -   实现接口`org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker`
        
        -   重载`registerStompEndpoints`方法，该方法主要用于注册STOMP端点，在客户端需首先连接该处配置的端点
        
        -   重载`configureMessageBroker`方法，该处为消息代理的配置。此处配置了目的地的前缀，即所有以该为前缀的信息都将发送到STOMP代理。该示例还配置了应用程序前缀，如该处配置了`/ws`则在访问编写的端点`/hello`时需添加前缀，则最后url为`/ws/hello`
        
-   编写前端客户端

    ```html
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Title</title>
        <script src="/webjars/jquery/jquery.min.js"></script>
        <script src="/webjars/sockjs-client/sockjs.min.js"></script>
        <script src="/webjars/stomp-websocket/stomp.min.js"></script>
    </head>
    <body>
    <script>
    var client = null;
    
    /**
     * 连接
     */
    function connect() {
        var socket = new SockJS('/user-hello')
        client = Stomp.over(socket)
        client.connect({}, function (data) {
            // 订阅到指定的广播
            client.subscribe('/topic/hello', function (data) {
                console.log(data)
            })
        })
    }
    
    /**
     * 发送数据
     * @param id 用户id
     */
    function sendMsg(id) {
        client.send('/ws/hello', {}, JSON.stringify({'name': 'dragonhht'}))
    }
    </script>
    </body>
    </html>
    ```
    
    -   说明
    
        -   该客户端使用了`sockjs`连接服务器
        
        -   首先使用`connect()`连接到后端配置的STOMP端点，并使用`subscribe`方法订阅广播端点
        
        -   使用`send`方法将信息发送到指定的端点
        
## 点对点(非注解`@SendToUser`)

-   编写处理器(包含WebSocket的声明周期)

    ```java
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
    ```

    -   说明
    
        -   该类实现接口`org.springframework.web.socket.WebSocketHandler`
        
        -   接口中的几个方法为WebSocket的生命周期，具体可看注释
        
-   编写WebSocket的拦截器

    ```java
    @Slf4j
    @Component
    public class MyWebSocketInterceptor implements HandshakeInterceptor {
        @Override
        public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
            log.info("拦截器 beforeHandshake方法");
            return true;
        }
    
        @Override
        public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
            log.info("拦截器afterHandshake方法");
        }
    }
    ```
    
    -   说明
    
        -   该类实现接口`org.springframework.web.socket.server.HandshakeInterceptor`
        
-   配置

    ```java
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
    ```
    
    -   说明:
    
        -   使用注解`@Configuration`标注该类为Spring的配置类
        
        -   使用注解`@EnableWebSocket`开启WebSocket
        
        -   实现`registerWebSocketHandlers`方法，配置具体的端点url、处理器和拦截器
        
-   编写客户端

    ```html
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Title</title>
    </head>
    <body>
    <script>
        var ws = new WebSocket("ws://localhost:8080/websocket");
        ws.onopen = function () {
            ws.send("hello");
        };
    
        ws.onmessage = function (evt) {
            console.log(evt.data)
        };
    
        ws.onclose = function (evt) {
            console.log("error");
        };
    
        ws.onerror = function (evt) {
            console.log("error");
        };
    
    </script>
    </body>
    </html>
    ```