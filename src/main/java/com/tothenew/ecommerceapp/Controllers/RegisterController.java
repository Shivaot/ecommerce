package com.tothenew.ecommerceapp.Controllers;

import com.tothenew.ecommerceapp.Entities.Users.Customer;
import com.tothenew.ecommerceapp.Entities.Users.Seller;
import com.tothenew.ecommerceapp.Services.UserRegisterService;
import com.tothenew.ecommerceapp.Utils.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class RegisterController {

    @Autowired
    UserRegisterService userRegisterService;

    @Autowired
    SendEmail sendEmail;

    @PostMapping("register/customer")
    String registerCustomer(@RequestBody Customer customer, HttpServletResponse httpServletResponse) {

        String getMessage = userRegisterService.registerCustomer(customer);
        System.out.println(getMessage + "for customer");
        if (getMessage.equals("Success")) {
            httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
        }
        else  {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return getMessage;
    }

    @PostMapping("register/seller")
    String registerSeller(@RequestBody Seller seller, HttpServletResponse httpServletResponse) {
        String getMessage = userRegisterService.registerSeller(seller);
        if (getMessage.equals("Success")) {
            sendEmail.sendEmail("ACCOUNT CREATED","Your account has been created waiting for approval",seller.getEmail());
            httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
        }
        else  {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return getMessage;
    }


}
