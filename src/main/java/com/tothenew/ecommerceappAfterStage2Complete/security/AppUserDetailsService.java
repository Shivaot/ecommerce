package com.tothenew.ecommerceappAfterStage2Complete.security;

import com.tothenew.ecommerceappAfterStage2Complete.utils.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserDao userDao;
    @Autowired
    EmailValidator emailValidator;
    Logger logger = LoggerFactory.getLogger(AppUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        boolean isValid = emailValidator.isValid(email);
        if (!isValid) {
            throw new RuntimeException("Email is invalid");
        }
        logger.trace("Trying to authenticate user ::" + email);
        UserDetails userDetails = userDao.loadUserByUserEmail(email);
        return userDetails;
    }
}