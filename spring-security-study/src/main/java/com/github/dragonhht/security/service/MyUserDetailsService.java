package com.github.dragonhht.security.service;

import com.github.dragonhht.security.entity.User;
import com.github.dragonhht.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 *
 * @author: dragonhht
 * @Date: 2019-11-16
 */
@Service("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 设置用户角色
        user.setAuthorities(getUserAuthorities(user.getRoles()));
        return user;
    }

    /**
     * 解析数据库中保存的角色
     * @param roles
     * @return
     */
    private List<GrantedAuthority> getUserAuthorities(String roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        String[] roleArr = roles.split(";");
        for (String role : roleArr) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
