package com.tothenew.ecommerceappAfterStage2Complete.config;

import com.tothenew.ecommerceappAfterStage2Complete.entities.users.User;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.UserLoginFailCounter;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.UserLoginFailCounterRepo;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.UserRepo;
import com.tothenew.ecommerceappAfterStage2Complete.utils.SendEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Optional;


@Component
public class AuthenticationEventListener {

    @Autowired
    UserLoginFailCounterRepo userLoginFailCounterRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    SendEmail sendEmail;
    Logger logger = LoggerFactory.getLogger(AuthenticationEventListener.class);

    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
        int counter;
        String userEmail = (String) event.getAuthentication().getPrincipal();
        if ("access-token".contentEquals(userEmail)) {
            logger.trace("invalid access token");
            return;
        }
        logger.trace(userEmail);
        Optional<UserLoginFailCounter> userLoginFailCounter = userLoginFailCounterRepo.findByEmail(userEmail);
        if (!userLoginFailCounter.isPresent()) {
            UserLoginFailCounter userLoginFailCounter1 = new UserLoginFailCounter();
            userLoginFailCounter1.setAttempts(1);
            userLoginFailCounter1.setEmail(userEmail);
            userLoginFailCounterRepo.save(userLoginFailCounter1);
        }
        if (userLoginFailCounter.isPresent()) {
            counter = userLoginFailCounter.get().getAttempts();
            logger.trace(String.valueOf(counter));
            if (counter>=2) {
                User user = userRepo.findByEmail(userEmail);
                user.setLocked(true);
                sendEmail.sendEmail("ACCOUNT LOCKED","YOUR ACCOUNT HAS BEEN LOCKED",userEmail);
                userRepo.save(user);
            }
            UserLoginFailCounter userLoginFailCounter1 = userLoginFailCounter.get();
            userLoginFailCounter1.setAttempts(++counter);
            userLoginFailCounter1.setEmail(userEmail);
            logger.trace(String.valueOf(userLoginFailCounter1));
            userLoginFailCounterRepo.save(userLoginFailCounter1);
        }

    }

    @EventListener
    public void authenticationPass(AuthenticationSuccessEvent event) {
        LinkedHashMap<String,String> userMap = new LinkedHashMap<>();
        try {
           userMap = (LinkedHashMap<String, String>) event.getAuthentication().getDetails();
        } catch (ClassCastException ex) {}
        String userEmail = new String();
        try {
            userEmail = userMap.get("username");
        } catch (NullPointerException e) {
        }
        Optional<UserLoginFailCounter> userLoginFailCounter = userLoginFailCounterRepo.findByEmail(userEmail);
        if (userLoginFailCounter.isPresent()){
            userLoginFailCounterRepo.deleteById(userLoginFailCounter.get().getId());
        }
    }

}
