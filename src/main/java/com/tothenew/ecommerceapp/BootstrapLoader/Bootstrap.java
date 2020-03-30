package com.tothenew.ecommerceapp.BootstrapLoader;

import com.tothenew.ecommerceapp.Entities.Users.Admin;
import com.tothenew.ecommerceapp.Entities.Users.CustomerActivate;
import com.tothenew.ecommerceapp.Entities.Users.Role;
import com.tothenew.ecommerceapp.Entities.Users.User;
import com.tothenew.ecommerceapp.Repositories.CustomerActivateRepo;
import com.tothenew.ecommerceapp.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class Bootstrap implements ApplicationRunner {

    @Autowired
    UserRepo userRepository;

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
            System.out.println("Total users saved::"+userRepository.count());
        }
    }
}
