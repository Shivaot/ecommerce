package com.tothenew.ecommerceapp.Repositories;

import com.tothenew.ecommerceapp.Entities.Users.UserLoginFailCounter;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserLoginFailCounterRepo extends CrudRepository<UserLoginFailCounter,Long> {
    Optional<UserLoginFailCounter> findByEmail(String email);
}
