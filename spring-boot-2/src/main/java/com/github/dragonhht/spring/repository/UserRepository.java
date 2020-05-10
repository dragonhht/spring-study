package com.github.dragonhht.spring.repository;

import com.github.dragonhht.spring.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 继承ReactiveCrudReposity下的子接口ReactiveMongoRepository.
 *
 * @author: huang
 * @Date: 2019-4-12
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
