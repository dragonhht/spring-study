package com.github.dragonhht.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * .
 *
 * @author: dragonhht
 * @Date: 2019-11-24
 */
@RestController
public class IndexController {

    @GetMapping("/hello")
    public String hello(Principal principal) {
        return "hello, " + principal.getName();
    }

}
