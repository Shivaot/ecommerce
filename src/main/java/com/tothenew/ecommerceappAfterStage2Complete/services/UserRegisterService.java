package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.entities.users.*;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.*;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.CustomerActivateRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.SellerRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.UserRepo;
import com.tothenew.ecommerceappAfterStage2Complete.utils.EmailSender;
import com.tothenew.ecommerceappAfterStage2Complete.utils.EmailValidator;
import com.tothenew.ecommerceappAfterStage2Complete.utils.GstValidator;
import com.tothenew.ecommerceappAfterStage2Complete.utils.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserRegisterService {

    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    GstValidator gstValidator;
    @Autowired
    CustomerActivateRepo customerActivateRepo;
    @Autowired
    EmailSender emailSender;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    UserRepo userRepo;
    @Autowired
    EmailValidator emailValidator;

    public Boolean registerCustomer(Customer customer) {
        if (!emailValidator.isValid(customer.getEmail())) {
            throw new InvalidEmailException("email is invalid");
        }
        if (userRepo.findByEmail(customer.getEmail()) != null) {
            throw new FieldAlreadyExistException("email already exist");
        }
        if (!PasswordValidator.isValidPassword(customer.getPassword())) {
            throw new InvalidPasswordException("password in invalid");
        }
        if (customer.getContact().length() != 10) {
            throw new FormatInvalidException("contact is invalid");
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Role role = new Role();
        role.setAuthority("ROLE_CUSTOMER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        customer.setRoles(roleSet);
        customer.setCreatedBy(customer.getFirstName());
        customer.setDateCreated(new Date());
        customer.setLocked(false);
        customer.setPasswordExpired(false);

        userRepo.save(customer);

        String token = UUID.randomUUID().toString();

        CustomerActivate customerActivate = new CustomerActivate();
        customerActivate.setToken(token);
        customerActivate.setUserEmail(customer.getEmail());
        customerActivate.setExpiryDate(new Date());

        customerActivateRepo.save(customerActivate);

        emailSender.sendEmail("ACCOUNT ACTIVATE TOKEN", "http://localhost:8080/customer/activate/" + token, customer.getEmail());

        return true;
    }

    public boolean registerSeller(Seller seller) {
        if (!gstValidator.isValid(seller.getGst())) {
            throw new InvalidGstException("gst is invalid");
        }
        if (!emailValidator.isValid(seller.getEmail())) {
            throw new InvalidEmailException("email is invalid");
        }
        if (userRepo.findByEmail(seller.getEmail()) != null) {
            throw new FieldAlreadyExistException("email already exist");
        }
        if (sellerRepo.findByCompanyName(seller.getCompanyName()) != null) {
            throw new FieldAlreadyExistException("company name should be unique");
        }
        if (sellerRepo.getByGst(seller.getGst()) != null) {
            throw new FieldAlreadyExistException("gst should be unique");
        }
        if (!PasswordValidator.isValidPassword(seller.getPassword())) {
            throw new InvalidPasswordException("password in invalid");
        }
        if (seller.getCompanyContact().length() != 10) {
            throw new FormatInvalidException("contact is invalid");
        }
        seller.setPassword(passwordEncoder.encode(seller.getPassword()));
        Role role = new Role();
        role.setAuthority("ROLE_SELLER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        seller.setRoles(roleSet);
        seller.setCreatedBy(seller.getFirstName());
        seller.setDateCreated(new Date());
        seller.setLocked(false);
        seller.setPasswordExpired(false);

        Set<Address> addresses = seller.getAddresses();
        addresses.forEach(address -> {
            Address addressSave = address;
            addressSave.setUser(seller);
        });

        userRepo.save(seller);

        return true;
    }


}
