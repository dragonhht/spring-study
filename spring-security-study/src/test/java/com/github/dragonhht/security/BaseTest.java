package com.github.dragonhht.security;

import com.github.dragonhht.security.entity.User;
import com.github.dragonhht.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * .
 *
 * @author: dragonhht
 * @Date: 2019-11-16
 */
@SpringBootTest(classes = SpringSecurityStudyApplication.class)
public class BaseTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testDao() {
        User user = new User();
        user.setEnable(true);
        user.setPassword(new BCryptPasswordEncoder().encode("123"));
        user.setRoles("ROLE_ADMIN");
        user.setUserName("testAdmin");
        userRepository.save(user);
    }

}
