package com.github.dragonhht.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录.
 *
 * @author: dragonhht
 * @Date: 2019-11-13
 */
@Controller
public class LoginController {

    @GetMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(String userName, String password) {
        System.out.println("userName: " + userName);
        System.out.println("password: " + password);
        return "hello world";
    }

}
