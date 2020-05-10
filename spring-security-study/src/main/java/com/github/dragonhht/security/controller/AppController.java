package com.github.dragonhht.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * .
 *
 * @author: dragonhht
 * @Date: 2019-11-16
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @GetMapping("/index")
    public String index() {
        return "app index";
    }

}
