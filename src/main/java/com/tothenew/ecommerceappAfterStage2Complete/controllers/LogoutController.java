package com.tothenew.ecommerceappAfterStage2Complete.controllers;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class LogoutController {

    @Autowired
    private TokenStore tokenStore;

    @PostMapping("/doLogout")
    public ResponseEntity<ResponseDTO> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
        return new ResponseEntity<>(new ResponseDTO("Logged out Successfully",new Date()), HttpStatus.OK);
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/admin/home")
    public String adminHome() {
        return "Admin home";
    }

    @GetMapping("/user/home")
    public String userHome() {
        return "User home";
    }

}
