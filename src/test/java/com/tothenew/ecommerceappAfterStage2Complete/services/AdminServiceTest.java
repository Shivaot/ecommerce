package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.AdminCustomerDTO;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Customer;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.FieldAlreadyExistException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.ResourceNotFoundException;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.CustomerRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServiceTest {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserRepo userRepo;

    @Test
    void shouldThrowResourceNotFoundExceptionWhenEmailNotFound() {
        // given
        Optional<String> email = Optional.of("xyz@mail.com");
        // when
        Executable executable = () -> adminService.getCustomers("0","10","id",email);
        // then
        assertThrows(ResourceNotFoundException.class,executable);
    }

    @Test
    void shouldReturnSingleCustomerWhenEmailFound() {
        // given
        Optional<String> email = Optional.of("riya@kohli.in");
        // when
        List<AdminCustomerDTO> customerDTO = adminService.getCustomers("0","10","id",email);
        // then
        assertEquals(customerDTO.size(),1);
    }

    @Test
    void shouldReturnListOfCustomers() {
        // given
        Optional<String> email = Optional.empty();
        // when
        List<AdminCustomerDTO> customerDTO = adminService.getCustomers("0","10","id",email);
        // then
        assertNotEquals(customerDTO.size(),1);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenIdNotFound() {
        // given
        Long id = 12222L;
        // when
        Executable executable = () -> adminService.activateUser(id);
        // then
        assertThrows(ResourceNotFoundException.class,executable);
    }

    @Test
    void shouldThrowFieldAlreadyExistException() {
        // given
        Long id = 4L;
        // when
        Executable executable = () -> adminService.activateUser(id);
        // then
        assertThrows(FieldAlreadyExistException.class,executable);
    }

    //  will not always pass
    @Test
    void shouldDeactivateUser() {
        // given
        Long id = 38L;
        // when
        adminService.deactivateUser(id);
        // then
        assertFalse(userRepo.findById(id).get().isActive());
    }



}