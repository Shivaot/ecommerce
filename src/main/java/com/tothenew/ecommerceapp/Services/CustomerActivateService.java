package com.tothenew.ecommerceapp.Services;

import com.tothenew.ecommerceapp.Entities.Users.CustomerActivate;
import com.tothenew.ecommerceapp.Entities.Users.User;
import com.tothenew.ecommerceapp.Repositories.CustomerActivateRepo;
import com.tothenew.ecommerceapp.Repositories.UserRepo;
import com.tothenew.ecommerceapp.Utils.SendEmail;
import com.tothenew.ecommerceapp.Utils.ValidEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
public class CustomerActivateService {

    @Autowired
    ValidEmail validEmail;

    @Autowired
    CustomerActivateRepo customerActivateRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    SendEmail sendEmail;

    @Transactional
    public String activateCustomer(String email,String token) {
        if (!validEmail.checkEmailValid(email)) {
            return "invalid email";
        }
        CustomerActivate customerActivate = customerActivateRepo.findByUserEmail(email);
        try {
            if (customerActivate.getUserEmail().equals(null)) {
            }
        } catch (NullPointerException ex) {
            return "no email found";
        }
        if (!customerActivate.getToken().equals(token)) {
            return "invalid token";
        }
        Date date = new Date();
        long diff = date.getTime() - customerActivate.getExpiryDate().getTime();
        long diffHours = diff / (60 * 60 * 1000);
        // token expire case
        if (diffHours > 24) {
            customerActivateRepo.deleteByUserEmail(email);

            String newToken = UUID.randomUUID().toString();

            CustomerActivate localCustomerActivate = new CustomerActivate();
            localCustomerActivate.setToken(newToken);
            localCustomerActivate.setUserEmail(email);
            localCustomerActivate.setExpiryDate(new Date());

            customerActivateRepo.save(localCustomerActivate);

            sendEmail.sendEmail("RE-ACCOUNT ACTIVATE TOKEN",newToken,email);

            return "Token has expired";
        }
        if (customerActivate.getToken().equals(token)) {
            User user = userRepo.findByEmail(email);
            user.setActive(true);
            userRepo.save(user);
            sendEmail.sendEmail("ACCOUNT ACTIVATED","Your account has been activated",email);
            customerActivateRepo.deleteByUserEmail(email);
            return "Success";
        }

        return "Success";
    }

    @Transactional
    public String resendLink(String email) {
        if (!validEmail.checkEmailValid(email)) {
            return "email is invalid";
        }
        User user = userRepo.findByEmail(email);
        try {
            if (user.getEmail().equals(null)) {
            }
        } catch (NullPointerException ex) {
            return "no email found";
        }
        if (user.isActive()) {
            return "Account already active";
        }
        if (!user.isActive()) {
            customerActivateRepo.deleteByUserEmail(email);

            String newToken = UUID.randomUUID().toString();

            CustomerActivate localCustomerActivate = new CustomerActivate();
            localCustomerActivate.setToken(newToken);
            localCustomerActivate.setUserEmail(email);
            localCustomerActivate.setExpiryDate(new Date());

            customerActivateRepo.save(localCustomerActivate);

            sendEmail.sendEmail("RE-ACCOUNT ACTIVATE TOKEN",newToken,email);

            return "Success";
        }

        return "Success";
    }
}
