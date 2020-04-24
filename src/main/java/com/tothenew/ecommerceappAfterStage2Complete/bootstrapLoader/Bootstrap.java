package com.tothenew.ecommerceappAfterStage2Complete.bootstrapLoader;

import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Admin;
import com.tothenew.ecommerceappAfterStage2Complete.entities.users.Role;
import com.tothenew.ecommerceappAfterStage2Complete.repositories.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class Bootstrap implements ApplicationRunner {

    @Autowired
    UserRepo userRepository;
    Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if(userRepository.count()<1){

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            Admin shiva = new Admin();
            shiva.setFirstName("Shiva");
            shiva.setLastName("Tiwari");
            shiva.setEmail("shiva@admin.com");
            shiva.setCreatedBy("Shiva");
            shiva.setDateCreated(new Date());
            shiva.setLastUpdated(new Date());
            shiva.setUpdatedBy("Shiva");
            shiva.setActive(true);
            shiva.setDeleted(false);
            shiva.setPassword(passwordEncoder.encode("pass"));

            Role role = new Role();
            role.setAuthority("ROLE_ADMIN");
            Role role1 = new Role();
            role1.setAuthority("ROLE_CUSTOMER");
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(role);
            roleSet.add(role1);
            shiva.setRoles(roleSet);

            userRepository.save(shiva);
            logger.trace("Total users saved::" +userRepository.count());
        }
    }
}
