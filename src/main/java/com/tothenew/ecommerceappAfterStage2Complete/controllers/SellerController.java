package com.tothenew.ecommerceappAfterStage2Complete.controllers;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.CategoryDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.SellerAddressDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.SellerProfileDTO;
import com.tothenew.ecommerceappAfterStage2Complete.services.CategoryService;
import com.tothenew.ecommerceappAfterStage2Complete.services.SellerProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/seller/profile")
public class SellerController {

    @Autowired
    SellerProfileService sellerProfileService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public SellerProfileDTO viewProfile(HttpServletRequest request) throws IOException {
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

    @GetMapping("/categories")
    public List<CategoryDTO> viewLeafCategories() {
        return categoryService.viewLeafCategories();
    }
}
