package com.github.dragonhht.security.repository;

import com.github.dragonhht.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * .
 *
 * @author: dragonhht
 * @Date: 2019-11-16
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByUserName(String userName);

}
