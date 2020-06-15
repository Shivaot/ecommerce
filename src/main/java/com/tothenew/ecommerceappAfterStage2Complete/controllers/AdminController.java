package com.tothenew.ecommerceappAfterStage2Complete.controllers;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.AdminCustomerDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.AdminSellerDTO;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.ResponseDTO;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.CustomerRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.SellerRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.UserRepo;
import com.tothenew.ecommerceappAfterStage2Complete.services.AdminService;
import com.tothenew.ecommerceappAfterStage2Complete.utils.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class AdminController {

    @Autowired
    UserRepo userRepo;
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    EmailSender emailSender;
    @Autowired
    AdminService adminService;

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    @PatchMapping("admin/customer/activate/{id}")
    public ResponseEntity<ResponseDTO> activateCustomer(@PathVariable Long id) {
        adminService.activateUser(id);
        return new ResponseEntity<>(new ResponseDTO("Success",new Date()), HttpStatus.OK);
    }

    @PatchMapping("admin/customer/deactivate/{id}")
    public  ResponseEntity<ResponseDTO> deactivateCustomer(@PathVariable Long id) {
       adminService.deactivateUser(id);
       return new ResponseEntity<>(new ResponseDTO("Success",new Date()), HttpStatus.OK);
    }

    @PatchMapping("admin/seller/activate/{id}")
    public ResponseEntity<ResponseDTO> activateSeller(@PathVariable Long id) {
        adminService.activateUser(id);
        return new ResponseEntity<>(new ResponseDTO("Success",new Date()), HttpStatus.OK);
    }

    @PatchMapping("admin/seller/deactivate/{id}")
    public ResponseEntity<ResponseDTO> deactivateSeller(@PathVariable Long id) {
        adminService.deactivateUser(id);
        return new ResponseEntity<>(new ResponseDTO("Success",new Date()), HttpStatus.OK);
    }

    @GetMapping("/customers")
    public List<AdminCustomerDTO> getCustomers(@RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "30") String size, @RequestParam(defaultValue = "id") String SortBy, @RequestParam(required = false) Optional<String> email) {
        return adminService.getCustomers(page,size,SortBy,email);
    }

    @GetMapping("/sellers")
    public List<AdminSellerDTO> getSellers(@RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "30") String size, @RequestParam(defaultValue = "id") String SortBy, @RequestParam(required = false) Optional<String> email) {
        return adminService.getSellers(page,size,SortBy,email);
    }
}
