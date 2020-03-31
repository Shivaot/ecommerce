package com.tothenew.ecommerceapp.Repositories;

import com.tothenew.ecommerceapp.Entities.Users.CustomerActivate;
import com.tothenew.ecommerceapp.Entities.Users.ForgotPasswordToken;
import org.springframework.data.repository.CrudRepository;

public interface CustomerActivateRepo extends CrudRepository<CustomerActivate,Long> {
    CustomerActivate findByUserEmail(String email);
    void deleteByUserEmail(String email);
}
