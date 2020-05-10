package com.github.dragonhht.spring.service;

import com.github.dragonhht.spring.model.User;
import com.github.dragonhht.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * .
 *
 * @author: huang
 * @Date: 2019-4-12
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Mono<User> findUserById(String id) {
        return userRepository.findById(id);
    }

    public Mono<Void> delUserById(String id) {
        return userRepository.deleteById(id);
    }

    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    public Flux<User> findAllUser() {
        return userRepository.findAll();
    }

}
