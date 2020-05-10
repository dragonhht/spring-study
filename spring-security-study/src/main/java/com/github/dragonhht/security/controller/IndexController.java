package com.github.dragonhht.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * .
 *
 * @author: dragonhht
 * @Date: 2019-11-13
 */
@RestController
public class IndexController {

    @GetMapping("/")
    public String hello() {
        return "hello world";
    }

}
