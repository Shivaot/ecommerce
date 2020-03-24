package com.tothenew.ecommerceapp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivationController {


    @PutMapping("customer/activate")
    public void activateCustomer() {

    }
}
