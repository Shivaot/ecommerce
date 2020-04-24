package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.entities.users.*;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.FieldAlreadyExistException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.FormatInvalidException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.InvalidEmailException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.InvalidPasswordException;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.CustomerActivateRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.SellerRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.UserRepo;
import com.tothenew.ecommerceappAfterStage2Complete.utils.SendEmail;
import com.tothenew.ecommerceappAfterStage2Complete.utils.EmailValidator;
import com.tothenew.ecommerceappAfterStage2Complete.utils.ValidGst;
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
    ValidGst validGst;
    @Autowired
    CustomerActivateRepo customerActivateRepo;
    @Autowired
    SendEmail sendEmail;
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

        sendEmail.sendEmail("ACCOUNT ACTIVATE TOKEN", "http://localhost:8080/customer/activate/" + token, customer.getEmail());

        return true;
    }

    public String registerSeller(Seller seller) {
        boolean isValidGst = validGst.checkGstValid(seller.getGst());
        if (!isValidGst) {
            return "gst is not valid";
        }
        boolean isValidEmail = emailValidator.isValid(seller.getEmail());
        if (!isValidEmail) {
            return "email is not valid";
        }
        User localSeller = userRepo.findByEmail(seller.getEmail());
        try {
            if (localSeller.getEmail().equals(seller.getEmail())) {
                return "Email already exists";
            }
        } catch (NullPointerException ex) {
//            ex.printStackTrace();
        }
        Seller anotherLocalSeller = sellerRepo.findByCompanyName(seller.getCompanyName());
        try {
            if (anotherLocalSeller.getCompanyName().equalsIgnoreCase(seller.getCompanyName())) {
                return "company name should be unique";
            }
        } catch (NullPointerException ex) {
//            ex.printStackTrace();
        }
        List<Seller> anotherLocalSeller1 = sellerRepo.findByGst(seller.getGst());
        boolean flag = false;
        for (Seller seller1 : anotherLocalSeller1) {
            if (seller1.getGst().equals(seller.getGst())) {
                flag = true;
                break;
            }
        }
        try {
            if (flag == true) {
                return "gst should be unique";
            }
        } catch (NullPointerException ex) {
//            ex.printStackTrace();
        }
        boolean isValidPassword = PasswordValidator.isValidPassword(seller.getPassword());
        if (!isValidPassword) {
            return "password is invalid";
        }
        seller.setPassword(passwordEncoder.encode(seller.getPassword()));
        if (seller.getCompanyContact().length() != 10) {
            return "invalid contact";
        }
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

        return "Success";
    }


}
