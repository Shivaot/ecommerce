package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.exceptions.FieldAlreadyExistException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.InvalidEmailException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerActivateServiceTest {

    @Autowired
    private CustomerActivateService customerActivateService;

    @Test
    void shouldThrowResourceNotFoundExceptionIfInvalidToken() {
        String token = UUID.randomUUID().toString();
        Executable executable = () -> customerActivateService.activateCustomer(token);
        assertThrows(ResourceNotFoundException.class,executable);
    }

    @Test
    void shouldThrowInvalidEmailException() {
        String email = "emailNotInRightFormat";
        Executable executable = () -> customerActivateService.resendLink(email);
        assertThrows(InvalidEmailException.class,executable);
    }

    @Test
    void shouldThrowFieldAlreadyExistExceptionWhenUserIsAlreadyActive() {
        String email = "riya@kohli.in";
        Executable executable = () -> customerActivateService.resendLink(email);
        assertThrows(FieldAlreadyExistException.class,executable);
    }

    // will not always pass
    @Test
    void shouldActivateUser() {
        String email = "Ram@sharma.in";
        Boolean result = customerActivateService.resendLink(email);
        assertTrue(result);
    }
}