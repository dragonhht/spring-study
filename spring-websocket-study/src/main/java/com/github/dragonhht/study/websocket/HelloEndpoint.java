package com.github.dragonhht.study.websocket;

import com.github.dragonhht.study.model.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Random;

/**
 * .
 *
 * @author: huang
 * @Date: 2019-4-15
 */
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
