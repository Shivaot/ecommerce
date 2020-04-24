package com.tothenew.ecommerceappAfterStage2Complete.repositories;

import com.tothenew.ecommerceappAfterStage2Complete.entities.users.UserLoginFailCounter;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserLoginFailCounterRepo extends CrudRepository<UserLoginFailCounter,Long> {
    Optional<UserLoginFailCounter> findByEmail(String email);
}
