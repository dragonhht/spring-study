package com.github.dragonhht.security.filter;

import com.github.dragonhht.security.exceptions.VerificationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 验证码校验过滤器.
 *
 * @author: dragonhht
 * @Date: 2019-11-17
 */
public class VerificationCodeFilter extends OncePerRequestFilter {

    private static final String LOGIN_URL = "/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 非登录请求不校验
        if (!LOGIN_URL.equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
        } else {
            try {
                verificationCode(request);
                filterChain.doFilter(request, response);
            } catch (VerificationException e) {
                e.printStackTrace();
            }
        }
    }

    private void verificationCode(HttpServletRequest request) throws VerificationException {
        // 获取用户输入的验证码
        String code = request.getParameter("captcha");
        // 获取session中保存的验证码
        HttpSession session = request.getSession();
        String sessionCode = (String) session.getAttribute("captcha");
        if (!StringUtils.isEmpty(sessionCode)) {
            // 清空验证码，无论是否成功
            session.removeAttribute("captcha");
        }
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(sessionCode)
                || !code.equals(sessionCode)) {
            throw new VerificationException();
        }
    }
}
