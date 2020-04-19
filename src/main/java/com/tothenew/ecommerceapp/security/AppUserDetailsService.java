package com.tothenew.ecommerceapp.security;

import com.tothenew.ecommerceapp.utils.ValidEmail;
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
    ValidEmail validEmail;
    Logger logger = LoggerFactory.getLogger(AppUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        boolean isValid = validEmail.checkEmailValid(email);
        if (!isValid) {
            throw new RuntimeException("Email is invalid");
        }
        logger.trace("Trying to authenticate user ::" + email);
        UserDetails userDetails = userDao.loadUserByUserEmail(email);
        return userDetails;
    }
}