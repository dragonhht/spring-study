package com.github.dragonhht.batch.repository;

import com.github.dragonhht.batch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * .
 *
 * @author: huang
 * @Date: 2019-4-12
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
