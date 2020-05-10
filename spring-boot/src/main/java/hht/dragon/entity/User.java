package hht.dragon.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * user
 * <p>
 * User : Dragon_hht
 * Date : 17-4-9
 * Time : 下午2:35
 */
//ConfigurationProperties获取前缀为user的配置,将前缀为user的值映射过来
@Component
@ConfigurationProperties(prefix = "user")
public class User {
    private String name1;
    private Integer age;

    public String getName1() {
        return name1;
    }

    public void setName1(String name) {
        this.name1 = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name1 + '\'' +
                ", age=" + age +
                '}';
    }
}
