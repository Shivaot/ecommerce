package com.tothenew.ecommerceappAfterStage2Complete.controllers;

import com.tothenew.ecommerceappAfterStage2Complete.dtos.ResponseDTO;
import com.tothenew.ecommerceappAfterStage2Complete.services.ForgotPasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;


@RestController
public class ForgotPassword {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    ForgotPasswordService forgotPasswordService;
    Logger logger = LoggerFactory.getLogger(ForgotPassword.class);

    @PostMapping("/token")
    public ResponseEntity<ResponseDTO> getToken(@RequestBody Map<String,String> email, HttpServletRequest request) {
        if (forgotPasswordService.sendToken(email.get("email"))) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                String tokenValue = authHeader.replace("Bearer", "").trim();
                OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
                tokenStore.removeAccessToken(accessToken);
            }
            return new ResponseEntity<>(new ResponseDTO("Success",new Date()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseDTO("Bad Request",new Date()), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody Map<String,String> resetPass) {
        if (forgotPasswordService.resetPassword(resetPass.get("token"), resetPass.get("pass"), resetPass.get("cpass"))) {
            return new ResponseEntity<>(new ResponseDTO("Success",new Date()),HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseDTO("Bad Request",new Date()), HttpStatus.BAD_REQUEST);
    }
}
