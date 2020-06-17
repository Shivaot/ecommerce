package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.exceptions.InvalidEmailException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ForgotPasswordServiceTest {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Test
    void shouldThrowInvalidEmailException() {
        String email = "emailNotInRightFormat";
        Executable executable = () -> forgotPasswordService.sendToken(email);
        assertThrows(InvalidEmailException.class,executable);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenNoEmailFound() {
        String email = "email@notindb.com";
        Executable executable = () -> forgotPasswordService.sendToken(email);
        assertThrows(ResourceNotFoundException  .class,executable);
    }
}