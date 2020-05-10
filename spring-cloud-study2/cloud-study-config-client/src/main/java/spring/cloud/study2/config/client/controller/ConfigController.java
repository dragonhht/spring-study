package spring.cloud.study2.config.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description.
 *
 * @author: huang
 * Date: 18-2-13
 */
@RefreshScope
@RestController
public class ConfigController {

    @Value("${test}")
    private String test;

    @RequestMapping("/test")
    public String getConfig() {
        return test;
    }



}
