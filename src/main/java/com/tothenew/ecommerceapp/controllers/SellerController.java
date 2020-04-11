package com.tothenew.ecommerceapp.controllers;

import com.tothenew.ecommerceapp.dtos.AddressDTO;
import com.tothenew.ecommerceapp.dtos.CustomerProfileDTO;
import com.tothenew.ecommerceapp.dtos.SellerAddressDTO;
import com.tothenew.ecommerceapp.dtos.SellerProfileDTO;
import com.tothenew.ecommerceapp.services.SellerProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/seller/profile")
public class SellerController {

    @Autowired
    SellerProfileService sellerProfileService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public SellerProfileDTO viewProfile(HttpServletRequest request) {
        return sellerProfileService.viewProfile(request);
    }

    @PatchMapping("")
    public String updateProfile(@RequestBody SellerProfileDTO sellerProfileDTO, HttpServletRequest request, HttpServletResponse response) {
        String getMessage = sellerProfileService.updateSeller(sellerProfileDTO,request);
        if ("Success".contentEquals(getMessage)) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return getMessage;
    }

    @PutMapping("/updatePassword")
    public String updatePassword(@RequestParam String pass,@RequestParam String cPass,HttpServletRequest request,HttpServletResponse response) {
        String getMessage = sellerProfileService.updatePassword(pass,cPass,request);
        if ("Success".contentEquals(getMessage)) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return getMessage;
    }

    @PutMapping("/updateAddress/{id}")
    public String updateAddress(@PathVariable Long id, @RequestBody SellerAddressDTO sellerAddressDTO, HttpServletResponse response, HttpServletRequest request) {
        String getMessage = sellerProfileService.updateAddress(id,sellerAddressDTO,request);
        if ("Success".contentEquals(getMessage)) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return getMessage;
    }
}
