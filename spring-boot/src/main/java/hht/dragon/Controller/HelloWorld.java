package hht.dragon.Controller;

import hht.dragon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * springBoot学习的第一个程序
 * <p>
 * User : Dragon_hht
 * Date : 17-4-9
 * Time : 上午11:46
 */
@RestController
public class HelloWorld {

    //将配置文件中的userName的值注入
    @Value("${userName1}")
    private String userName;

    //将配置文件中的age的值注入
    @Value("${age}")
    private Integer age;

    //将配置文件中的text的值注入
    @Value("${text}")
    private String text;

    //注入User类的值
    @Autowired
    private User user;

    //如果想要多个路径都能访问，value可以写成集合，RequestMapping用法与springMVC的一样
    @RequestMapping(value = {"/hello", "/hi"})
    public User say() {
        return user;
    }
}
