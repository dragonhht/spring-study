package com.github.dragonhht.security.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * 用户实体.
 *
 * @author: dragonhht
 * @Date: 2019-11-16
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "my_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Integer userId;
    private String userName;
    private String password;
    private boolean enable;
    private String roles;
    @Transient
    private List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }

    @Override
    public int hashCode() {
        return this.userId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && this.userName.equals(((User) obj).getUsername());
    }
}
