package com.tothenew.ecommerceappAfterStage2Complete.repositories;

import com.tothenew.ecommerceappAfterStage2Complete.entities.users.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User,Long> {
    User findByEmail(String email);
    @Query(value = "select * from user where email=:email.",nativeQuery = true)
    Optional<User> getUserByEmail(@Param("email") String email);
    Optional<User> findById(Long id);
}
