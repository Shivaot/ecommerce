package com.tothenew.ecommerceappAfterStage2Complete.controllers;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.tothenew.ecommerceappAfterStage2Complete.dtos.AdminCustomerDTO;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Customer;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Seller;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.User;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.CustomerRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.SellerRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.UserRepo;
import com.tothenew.ecommerceappAfterStage2Complete.services.AdminService;
import com.tothenew.ecommerceappAfterStage2Complete.utils.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    @PatchMapping("admin/activate/customer/{id}")
    public String activateCustomer(@PathVariable java.lang.Long id, HttpServletResponse httpServletResponse) {
        Optional<User> user = userRepo.findById(id);
        if (!user.isPresent()) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "no user found with given id";
        }
        if (!user.get().isActive()) {
            user.get().setActive(true);
            userRepo.save(user.get());
            // trigger mail
            emailSender.sendEmail("ACTIVATED", "HEY CUSTOMER YOUR ACCOUNT HAS BEEN ACTIVATED", user.get().getEmail());
            return "Success";
        }
        userRepo.save(user.get());
        logger.trace("already activated");
        return "Success";
    }

    @PatchMapping("admin/deactivate/customer/{id}")
    public String deactivateCustomer(@PathVariable java.lang.Long id, HttpServletResponse httpServletResponse) {
        Optional<User> user = userRepo.findById(id);
        if (!user.isPresent()) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "no user found with given id";
        }
        if (user.get().isActive()) {
            user.get().setActive(false);
            userRepo.save(user.get());
            // trigger mail
            emailSender.sendEmail("DEACTIVATED", "HEY CUSTOMER YOUR ACCOUNT HAS BEEN DEACTIVATED", user.get().getEmail());
            return "Success";
        }
        userRepo.save(user.get());
        logger.trace("already deactivated");
        return "Success";
    }

    @PatchMapping("admin/activate/seller/{id}")
    public String activateSeller(@PathVariable java.lang.Long id, HttpServletResponse httpServletResponse) {
        Optional<User> user = userRepo.findById(id);
        if (!user.isPresent()) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "no user found with given id";
        }
        if (!user.get().isActive()) {
            user.get().setActive(true);
            userRepo.save(user.get());
            // trigger mail
            emailSender.sendEmail("ACTIVATED", "HEY SELLER YOUR ACCOUNT HAS BEEN ACTIVATED", user.get().getEmail());
            return "Success";
        }
        userRepo.save(user.get());
        logger.trace("already activated");
        return "Success";
    }

    @PatchMapping("admin/deactivate/seller/{id}")
    public String deactivateSeller(@PathVariable java.lang.Long id, HttpServletResponse httpServletResponse) {
        Optional<User> user = userRepo.findById(id);
        if (!user.isPresent()) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "no user found with given id";
        }
        if (user.get().isActive()) {
            user.get().setActive(false);
            userRepo.save(user.get());
            // trigger mail
            emailSender.sendEmail("DEACTIVATED", "HEY SELLER YOUR ACCOUNT HAS BEEN DEACTIVATED", user.get().getEmail());
            return "Success";
        }
        userRepo.save(user.get());
        logger.trace("already deactivated");
        return "Success";
    }

    @GetMapping("/customers")
    public List<AdminCustomerDTO> getCustomers(@RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "10") String size, @RequestParam(defaultValue = "id") String SortBy, @RequestParam(required = false) Optional<String> email) {
        return adminService.getCustomers(page,size,SortBy,email);
    }


    @GetMapping("/admin/sellers")
    public MappingJacksonValue getSellers(@RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "10") String size, @RequestParam(defaultValue = "id") String SortBy) {
        List<Seller> sellerList = sellerRepo.findAll(PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by(SortBy)));
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "firstName", "middleName", "lastName", "email", "active", "companyName", "addresses", "companyContact");
        FilterProvider filters = new SimpleFilterProvider().addFilter("ignoreAddressInCustomer", filter);
        MappingJacksonValue mapping = new MappingJacksonValue(sellerList);
        mapping.setFilters(filters);

        return mapping;
    }
}
