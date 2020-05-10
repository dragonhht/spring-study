package com.github.dragonhht.security.details;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 继承WebAuthenticationDetails可获取Request中的其他信息.
 *
 * @author: dragonhht
 * @Date: 2019-11-17
 */
public class MyWebAuthenticationDetails extends WebAuthenticationDetails {

    private boolean codeIsRight;

    public boolean isCodeIsRight() {
        return codeIsRight;
    }

    public MyWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        // 获取用户输入的验证码
        String code = request.getParameter("captcha");
        // 获取session中保存的验证码
        HttpSession session = request.getSession();
        String sessionCode = (String) session.getAttribute("captcha");
        if (!StringUtils.isEmpty(sessionCode)) {
            // 清空验证码，无论是否成功
            session.removeAttribute("captcha");
        }
        if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(sessionCode)
                && code.equals(sessionCode)) {
            this.codeIsRight = true;
        }
    }
}
