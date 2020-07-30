package com.kangyonggan.demo;

import org.springframework.data.repository.CrudRepository;

/**
 * @author kyg
 */
public interface UserRepository extends CrudRepository<User, Long> {

}
