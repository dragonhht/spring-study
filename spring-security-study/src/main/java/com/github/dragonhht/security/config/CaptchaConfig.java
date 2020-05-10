package com.github.dragonhht.security.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 图片验证码(kaptcha)配置.
 *
 * @author: dragonhht
 * @Date: 2019-11-17
 */
@Configuration
public class CaptchaConfig {

    @Bean
    public Producer captcha() {
        Properties properties = new Properties();
        // 图片宽度
        properties.setProperty("kaptcha.image.width", "125");
        // 图片高度
        properties.setProperty("kaptcha.image.height", "45");
        // 字符集
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        // 字符长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 字体
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha=new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

}
