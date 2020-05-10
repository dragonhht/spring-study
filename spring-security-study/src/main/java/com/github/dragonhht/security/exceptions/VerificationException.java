package com.github.dragonhht.security.exceptions;


import org.springframework.security.core.AuthenticationException;

/**
 * 自定义验证码校验失败异常.
 *
 * @author: dragonhht
 * @Date: 2019-11-17
 */
public class VerificationException extends AuthenticationException {

    public VerificationException() {
        super("图片验证码校验失败");
    }
}
