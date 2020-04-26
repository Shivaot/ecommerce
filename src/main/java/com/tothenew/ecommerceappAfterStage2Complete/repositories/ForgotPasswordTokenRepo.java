package com.tothenew.ecommerceappAfterStage2Complete.repositories;

import com.tothenew.ecommerceappAfterStage2Complete.entities.users.ForgotPasswordToken;
import org.springframework.data.repository.CrudRepository;

public interface ForgotPasswordTokenRepo extends CrudRepository<ForgotPasswordToken,Long> {
    ForgotPasswordToken findByToken(String token);
    void deleteByToken(String token);
}
