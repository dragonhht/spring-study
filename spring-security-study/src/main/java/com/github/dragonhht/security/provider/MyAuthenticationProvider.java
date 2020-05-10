package com.github.dragonhht.security.provider;

import com.github.dragonhht.security.details.MyWebAuthenticationDetails;
import com.github.dragonhht.security.exceptions.VerificationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义认证.
 *
 * @author: dragonhht
 * @Date: 2019-11-17
 */
//@Component
public class MyAuthenticationProvider extends DaoAuthenticationProvider {

    public MyAuthenticationProvider(@Qualifier("myUserDetailsService") UserDetailsService userDetailsService,
                                    PasswordEncoder passwordEncoder) {
        this.setUserDetailsService(userDetailsService);
        this.setPasswordEncoder(passwordEncoder);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                        UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        // 校验验证码
        MyWebAuthenticationDetails details = (MyWebAuthenticationDetails) authentication.getDetails();
        if (!details.isCodeIsRight()) {
            throw new VerificationException();
        }
        // 调用父类实现密码验证
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
