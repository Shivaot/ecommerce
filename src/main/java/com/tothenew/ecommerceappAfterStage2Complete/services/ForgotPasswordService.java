package com.tothenew.ecommerceappAfterStage2Complete.services;

import com.tothenew.ecommerceappAfterStage2Complete.entities.users.ForgotPasswordToken;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.User;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.InvalidEmailException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.InvalidPasswordException;
import com.tothenew.ecommerceappAfterStage2Complete.exceptions.ResourceNotFoundException;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.ForgotPasswordTokenRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.UserRepo;
import com.tothenew.ecommerceappAfterStage2Complete.utils.EmailSender;
import com.tothenew.ecommerceappAfterStage2Complete.utils.EmailValidator;
import com.tothenew.ecommerceappAfterStage2Complete.utils.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
public class ForgotPasswordService {

    @Autowired
    EmailValidator emailValidator;
    @Autowired
    UserRepo userRepo;
    @Autowired
    EmailSender emailSender;
    @Autowired
    ForgotPasswordTokenRepo forgotPasswordTokenRepo;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean sendToken(String email) {
        if (!emailValidator.isValid(email)) {
            throw new InvalidEmailException("email is invalid");
        }
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("no email found");
        }
        if (!user.isActive()) {
            throw new ResourceNotFoundException("user is not active");
        }

        String token = UUID.randomUUID().toString();

        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setToken(token);
        forgotPasswordToken.setExpiryDate(new Date());
        forgotPasswordToken.setUserEmail(email);

        forgotPasswordTokenRepo.save(forgotPasswordToken);

        emailSender.sendEmail("FORGOT PASSWORD", token, email);

        return true;
    }

    @Transactional
    public boolean resetPassword(String token, String pass, String cpass) {
        if (!pass.equals(cpass)) {
            throw new ResourceNotFoundException("password and confirm password not match");
        }
        if (!PasswordValidator.isValidPassword(pass)) {
            throw new InvalidPasswordException("invalid password syntax");
        }
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepo.findByToken(token);
        if (forgotPasswordToken == null) {
            throw new ResourceNotFoundException("invalid token");
        }
        Date date = new Date();
        long diff = date.getTime() - forgotPasswordToken.getExpiryDate().getTime();
        long diffHours = diff / (60 * 60 * 1000);
        if (diffHours > 24) {
            forgotPasswordTokenRepo.deleteByToken(token);
            throw new ResourceNotFoundException("token has expired");
        }
        User user = userRepo.findByEmail(forgotPasswordToken.getUserEmail());
        user.setPassword(passwordEncoder.encode(pass));
        userRepo.save(user);
        forgotPasswordTokenRepo.deleteByToken(token);
        emailSender.sendEmail("PASSWORD CHANGED", "YOUR PASSWORD HAS BEEN CHANGED", forgotPasswordToken.getUserEmail());
        return true;
    }
}
