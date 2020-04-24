package com.tothenew.ecommerceappAfterStage2Complete.controllers;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.ResponseDTO;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Customer;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Seller;
import com.tothenew.ecommerceappAfterStage2Complete.services.UserRegisterService;
import com.tothenew.ecommerceappAfterStage2Complete.utils.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;

@RestController
public class RegisterController {

    @Autowired
    UserRegisterService userRegisterService;
    @Autowired
    SendEmail sendEmail;

    @PostMapping(value = "customer/registration",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseDTO> registerCustomer(@Valid @RequestBody Customer customer) {
        if (userRegisterService.registerCustomer(customer)) {
            return new ResponseEntity<>(new ResponseDTO("Success",new Date()), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ResponseDTO("Bad Request",new Date()),HttpStatus.BAD_REQUEST);
    }

    @PostMapping("register/seller")
    String registerSeller(@Valid @RequestBody Seller seller, HttpServletResponse httpServletResponse) {
        String getMessage = userRegisterService.registerSeller(seller);
        if (getMessage.contentEquals("Success")) {
            sendEmail.sendEmail("ACCOUNT CREATED", "Your account has been created waiting for approval", seller.getEmail());
            httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return getMessage;
    }
}
