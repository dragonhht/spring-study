package com.github.dragonhht.security.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 获取图片验证码.
 *
 * @author: dragonhht
 * @Date: 2019-11-17
 */
@Controller
public class CaptchaController {

    @Autowired
    private Producer captchaProducer;

    @GetMapping("/captcha.jpg")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        // 创建验证码文本
        String text = captchaProducer.createText();
        // 将验证码放入session
        request.getSession().setAttribute("captcha", text);
        // 创建验证码图片
        BufferedImage image = captchaProducer.createImage(text);
        // 将验证码图片通过流输出
        ServletOutputStream responseOutputStream =
                response.getOutputStream();
        ImageIO.write(image, "jpg", responseOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
