package com.tothenew.ecommerceappAfterStage2Complete.security;

import com.tothenew.ecommerceappAfterStage2Complete.entities.users.User;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {

    @Autowired
    UserRepo userRepository;
    Logger logger = LoggerFactory.getLogger(UserDao.class);

    AppUser loadUserByUserEmail(String email)  {
        User user = userRepository.findByEmail(email);
        logger.trace(user.toString());
        if (email != null) {
            List<GrantAuthorityImpl> authorities = new ArrayList<>();
            logger.trace(user.getRoles().toString());
            user.getRoles().forEach(role -> {
                    GrantAuthorityImpl grantAuthority = new GrantAuthorityImpl(role.getAuthority());
                    authorities.add(grantAuthority);
                });
            return new AppUser(user.getFirstName(), user.getPassword(), authorities,user.isActive(),!user.isLocked(),!user.isPasswordExpired());
        } else {
            throw new RuntimeException();
        }
    }
}
