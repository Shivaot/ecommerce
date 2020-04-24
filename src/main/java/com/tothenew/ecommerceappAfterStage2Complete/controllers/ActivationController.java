package com.tothenew.ecommerceappAfterStage2Complete.controllers;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.ResponseDTO;
import com.tothenew.ecommerceappAfterStage2Complete.services.CustomerActivateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
public class ActivationController {

    @Autowired
    CustomerActivateService customerActivateService;

    
    @PutMapping("customer/activate/{token}")
    public ResponseEntity<ResponseDTO> activateCustomer(@PathVariable String token) {
        if (customerActivateService.activateCustomer(token)) {
            return new ResponseEntity<>(new ResponseDTO("Success",new Date()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseDTO("Bad Request",new Date()),HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/customer/re-sendActivation",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> resendLink(@RequestBody Map<String,String> email, HttpServletResponse httpServletResponse) {
        if (customerActivateService.resendLink(email.get("email"))) {
            return new ResponseEntity<>(new ResponseDTO("Success",new Date()),HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseDTO("Bad Request",new Date()),HttpStatus.BAD_REQUEST);
    }
}
