package com.tothenew.ecommerceappAfterStage2Complete.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FormatInvalidException extends RuntimeException {
    public FormatInvalidException(String s) {
        super(s);
    }
}
