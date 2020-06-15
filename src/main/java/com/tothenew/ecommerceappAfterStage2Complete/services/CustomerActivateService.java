package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.entities.users.CustomerActivate;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.User;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.FieldAlreadyExistException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.InvalidEmailException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.ResourceNotFoundException;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.CustomerActivateRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.UserRepo;
import com.tothenew.ecommerceappAfterStage2Complete.utils.EmailSender;
import com.tothenew.ecommerceappAfterStage2Complete.utils.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
public class CustomerActivateService {

    @Autowired
    EmailValidator emailValidator;
    @Autowired
    CustomerActivateRepo customerActivateRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    EmailSender emailSender;

    @Transactional
    public Boolean activateCustomer(String token) {
        CustomerActivate customerActivate = customerActivateRepo.findByToken(token);
        if (customerActivate == null) {
            throw new ResourceNotFoundException("invalid token");
        }
        Date date = new Date();
        long diff = date.getTime() - customerActivate.getExpiryDate().getTime();
        long diffHours = diff / (60 * 60 * 1000);
        // token expire case
        if (diffHours > 24) {
            customerActivateRepo.deleteByUserEmail(customerActivate.getUserEmail());

            String newToken = UUID.randomUUID().toString();

            CustomerActivate localCustomerActivate = new CustomerActivate();
            localCustomerActivate.setToken(newToken);
            localCustomerActivate.setUserEmail(customerActivate.getUserEmail());
            localCustomerActivate.setExpiryDate(new Date());

            customerActivateRepo.save(localCustomerActivate);

            emailSender.sendEmail("RE-ACCOUNT ACTIVATE TOKEN", "http://localhost:8080/customer/activate/"+newToken, customerActivate.getUserEmail());

            throw new ResourceNotFoundException("token has expired");
        }
        if (customerActivate.getToken().equals(token)) {
            User user = userRepo.findByEmail(customerActivate.getUserEmail());
            user.setActive(true);
            userRepo.save(user);
            emailSender.sendEmail("ACCOUNT ACTIVATED", "Your account has been activated", customerActivate.getUserEmail());
            customerActivateRepo.deleteByUserEmail(customerActivate.getUserEmail());
            return true;
        }
        return true;
    }

    @Transactional
    public Boolean resendLink(String email) {
        if (!emailValidator.isValid(email)) {
            throw new InvalidEmailException("email is invalid");
        }
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("email not found");
        }
        if (user.isActive()) {
            throw new FieldAlreadyExistException("user is already is activated");
        }
        if (!user.isActive()) {
            customerActivateRepo.deleteByUserEmail(email);

            String newToken = UUID.randomUUID().toString();

            CustomerActivate localCustomerActivate = new CustomerActivate();
            localCustomerActivate.setToken(newToken);
            localCustomerActivate.setUserEmail(email);
            localCustomerActivate.setExpiryDate(new Date());

            customerActivateRepo.save(localCustomerActivate);

            emailSender.sendEmail("RE-ACCOUNT ACTIVATE TOKEN", "http://localhost:3000/customer/activate/"+newToken, email);

            return true;
        }
        return true;
    }
}
