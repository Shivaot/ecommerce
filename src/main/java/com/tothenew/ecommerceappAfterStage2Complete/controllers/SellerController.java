package com.tothenew.ecommerceappAfterStage2Complete.controllers;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.CategoryDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.ResponseDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.SellerAddressDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.SellerProfileDTO;
import com.tothenew.ecommerceappAfterStage2Complete.services.CategoryService;
import com.tothenew.ecommerceappAfterStage2Complete.services.SellerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seller/profile")
public class SellerController {

    @Autowired
    SellerProfileService sellerProfileService;
    @Autowired
    CategoryService categoryService;

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
    public ResponseEntity<ResponseDTO> updatePassword(@RequestBody Map<String,String> body, HttpServletRequest request) {
        if (sellerProfileService.updatePassword(body.get("pass"),body.get("cPass"),request)) {
            return new ResponseEntity<>(new ResponseDTO("Success",new Date()), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ResponseDTO("bad request",new Date()), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/updateAddress/{id}")
    public ResponseEntity<ResponseDTO> updateAddress(@PathVariable Long id, @RequestBody SellerAddressDTO sellerAddressDTO,HttpServletRequest request) {
        if (sellerProfileService.updateAddress(id,sellerAddressDTO,request)) {
            return new ResponseEntity<>(new ResponseDTO("Success",new Date()), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ResponseDTO("bad request",new Date()), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/categories")
    public List<CategoryDTO> viewLeafCategories() {
        return categoryService.viewLeafCategories();
    }
}
