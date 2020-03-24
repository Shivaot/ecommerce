package com.tothenew.ecommerceapp.Services;

import com.tothenew.ecommerceapp.Entities.Users.ForgotPasswordToken;
import com.tothenew.ecommerceapp.Entities.Users.User;
import com.tothenew.ecommerceapp.Repositories.ForgotPasswordTokenRepo;
import com.tothenew.ecommerceapp.Repositories.UserRepo;
import com.tothenew.ecommerceapp.Utils.SendEmail;
import com.tothenew.ecommerceapp.Utils.ValidEmail;
import com.tothenew.ecommerceapp.Utils.ValidPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ForgotPasswordService {

    @Autowired
    ValidEmail validEmail;

    @Autowired
    UserRepo userRepo;

    @Autowired
    SendEmail sendEmail;

    @Autowired
    ForgotPasswordTokenRepo forgotPasswordTokenRepo;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String sendToken(String email) {
        boolean isValidEmail = validEmail.checkEmailValid(email);
        if (!isValidEmail) {
            return "email is invalid";
        }
        User user = userRepo.findByEmail(email);
        try {
            if (user.getEmail().equals(null)) {
            }
        } catch (NullPointerException ex) {
            return "no email found";
        }

        String token = UUID.randomUUID().toString();

        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setToken(token);
        forgotPasswordToken.setExpiryDate( new Date());
        forgotPasswordToken.setUserEmail(email);

        forgotPasswordTokenRepo.save(forgotPasswordToken);

        sendEmail.sendEmail("FORGOT PASSWORD",token,email);

        return "Success";
    }

    @Transactional
    public String resetPassword(String email,String token,String pass,String cpass) {
        if (!pass.equals(cpass)){
            return "password and confirm password not match";
        }
        if (!ValidPassword.isValidPassword(pass)) {
            return "in valid password syntax";
        }
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepo.findByUserEmail(email);
        try {
            if (forgotPasswordToken.getUserEmail().equals(null)) {
            }
        } catch (NullPointerException ex) {
            return "no email found";
        }
        Date currentDate = new Date();
        int milliseconds = (int) (forgotPasswordToken.getExpiryDate().getTime() - currentDate.getTime());
        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
        System.out.println(days);
        if (days > 1) {
            forgotPasswordTokenRepo.deleteByUserEmail(email);
            return "Token has expired";
        }
        if (!forgotPasswordToken.getToken().equals(token)) {
            return "invalid token";
        }
        if (forgotPasswordToken.getToken().equals(token)) {
            User user = userRepo.findByEmail(email);
            user.setPassword(passwordEncoder.encode(pass));
            userRepo.save(user);
            forgotPasswordTokenRepo.deleteByUserEmail(email);
            sendEmail.sendEmail("PASSWORD CHANGED","YOUR PASSWORD HAS BEEN CHANGED",email);
            return "Success";
        }
        return "Success";
    }
}
