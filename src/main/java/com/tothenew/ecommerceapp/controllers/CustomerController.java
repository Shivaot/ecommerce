package com.tothenew.ecommerceapp.controllers;

import com.tothenew.ecommerceapp.dtos.AddressDTO;
import com.tothenew.ecommerceapp.dtos.CustomerProfileDTO;
import com.tothenew.ecommerceapp.services.CustomerProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@RestController
@RequestMapping("/customer/profile")
public class CustomerController {

    @Autowired
    CustomerProfileService customerProfileService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public CustomerProfileDTO viewProfile(HttpServletRequest request) {
        CustomerProfileDTO customerProfileDTO = modelMapper.map(customerProfileService.viewProfile(request),CustomerProfileDTO.class);
        // check image format then set
        customerProfileDTO.setImage("image");
        return customerProfileDTO;
    }

    @PatchMapping("")
    public String updateProfile(@RequestBody CustomerProfileDTO customerProfileDTO,HttpServletRequest request, HttpServletResponse response) {
        String getMessage = customerProfileService.updateCustomer(customerProfileDTO,request);
        if ("Success".contentEquals(getMessage)) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return getMessage;
    }

    @PutMapping("/updatePassword")
    public String updatePassword(@RequestParam String pass,@RequestParam String cPass,HttpServletRequest request,HttpServletResponse response) {
        String getMessage = customerProfileService.updatePassword(pass,cPass,request);
        if ("Success".contentEquals(getMessage)) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return getMessage;
    }

    @GetMapping("/addresses")
    public Set<AddressDTO> viewAddresses(HttpServletRequest request) {
        return customerProfileService.viewAddress(request);
    }

    @PostMapping("/address")
    public String newAddress(@RequestBody AddressDTO addressDTO, HttpServletRequest request, HttpServletResponse response) {
        String getMessage = customerProfileService.newAddress(addressDTO,request);
        if ("Success".contentEquals(getMessage)) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return getMessage;
    }

    @DeleteMapping("/address/{id}")
    public String deleteAddress(@PathVariable Long id,HttpServletResponse response,HttpServletRequest request) {
        String getMessage = customerProfileService.deleteAddress(id,request);
        if ("Success".contentEquals(getMessage)) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return getMessage;
    }

    @PutMapping("/updateAddress/{id}")
    public String updateAddress(@PathVariable Long id,@RequestBody AddressDTO addressDTO,HttpServletResponse response,HttpServletRequest request) {
        String getMessage = customerProfileService.updateAddress(id,addressDTO,request);
        if ("Success".contentEquals(getMessage)) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return getMessage;
    }
}
