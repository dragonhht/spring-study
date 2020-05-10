package com.github.dragonhht.spring.constoller;

import com.github.dragonhht.spring.model.User;
import com.github.dragonhht.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * .
 *
 * @author: huang
 * @Date: 2019-4-12
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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
