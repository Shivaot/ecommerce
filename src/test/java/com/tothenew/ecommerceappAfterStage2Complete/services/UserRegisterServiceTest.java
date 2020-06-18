package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Address;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Customer;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Seller;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRegisterServiceTest {

    @Autowired
    private UserRegisterService userRegisterService;

    @Test
    void shouldThrowInvalidEmailExceptionWhenEmailInvalid() {
        Customer customer = new Customer();
        customer.setFirstName("Eden");
        customer.setLastName("Hazard");
        customer.setMiddleName("");
        customer.setEmail("emailNotInRightFormat");
        customer.setPassword("Test@test123");
        customer.setContact("9910026203");
        Executable executable = () -> userRegisterService.registerCustomer(customer);
        assertThrows(InvalidEmailException.class,executable);
    }

    @Test
    void shouldThrowFieldAlreadyExistExceptionWhenCustomerAlreadyRegistered() {
        Customer customer = new Customer();
        customer.setFirstName("Eden");
        customer.setLastName("Hazard");
        customer.setMiddleName("");
        customer.setEmail("riya@kohli.in");
        customer.setPassword("Test@test123");
        customer.setContact("9910026203");
        Executable executable = () -> userRegisterService.registerCustomer(customer);
        assertThrows(FieldAlreadyExistException.class,executable);
    }

    @Test
    void shouldThrowInvalidPasswordExceptionWhenInvalidPasswordFormat() {
        Customer customer = new Customer();
        customer.setFirstName("Eden");
        customer.setLastName("Hazard");
        customer.setMiddleName("");
        customer.setEmail("eden@hazard.in");
        customer.setPassword("Test@test");
        customer.setContact("9910026203");
        Executable executable = () -> userRegisterService.registerCustomer(customer);
        assertThrows(InvalidPasswordException.class,executable);
    }

    @Test
    void shouldThrowFormatInvalidExceptionWhenContactInvalid() {
        Customer customer = new Customer();
        customer.setFirstName("Eden");
        customer.setLastName("Hazard");
        customer.setMiddleName("");
        customer.setEmail("eden@hazard.in");
        customer.setPassword("Test@test123");
        customer.setContact("991002620");
        Executable executable = () -> userRegisterService.registerCustomer(customer);
        assertThrows(FormatInvalidException.class,executable);
    }

    @Test
    void shouldRegisterCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Eden");
        customer.setLastName("Hazard");
        customer.setMiddleName("");
        customer.setEmail("eden@hazard.in");
        customer.setPassword("Test@test123");
        customer.setContact("9910026203");
        boolean result = userRegisterService.registerCustomer(customer);
        assertTrue(result);
    }

    @Test
    void shouldThrowInvalidGstException() {
        Seller seller = new Seller();
        seller.setFirstName("David");
        seller.setMiddleName("");
        seller.setLastName("Luiz");
        seller.setEmail("david@luiz.in");
        seller.setPassword("Test@test123");
        seller.setCompanyContact("9910026203");
        seller.setCompanyName("Chelsea");
        seller.setGst("123");
        Set<Address> addresses = new HashSet<>();
        Address address = new Address();
        address.setAddress("some address");
        address.setCity("gzb");
        address.setCountry("India");
        address.setLabel("Home");
        address.setState("UP");
        address.setZipCode(201001);
        addresses.add(address);
        seller.setAddresses(addresses);
        Executable executable = () -> userRegisterService.registerSeller(seller);
        assertThrows(InvalidGstException.class,executable);
    }

    @Test
    void shouldRegisterSeller() {
        Seller seller = new Seller();
        seller.setFirstName("David");
        seller.setMiddleName("");
        seller.setLastName("Luiz");
        seller.setEmail("david@luiz.in");
        seller.setPassword("Test@test123");
        seller.setCompanyContact("9910026203");
        seller.setCompanyName("Chelsea");
        seller.setGst("36WRVPS3698F1Z9");
        Set<Address> addresses = new HashSet<>();
        Address address = new Address();
        address.setAddress("some address");
        address.setCity("gzb");
        address.setCountry("India");
        address.setLabel("Home");
        address.setState("UP");
        address.setZipCode(201001);
        addresses.add(address);
        seller.setAddresses(addresses);
        boolean result = userRegisterService.registerSeller(seller);
        assertTrue(result);
    }

}