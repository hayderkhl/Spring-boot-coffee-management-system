package com.haidar.coffeemanagementsystem.cofeUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CafeUtil {

    private CafeUtil() {

    }

    public  static ResponseEntity<String> getResponsseEntity(String responseMessage, HttpStatus httpStatus) {

        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}", httpStatus);

    }
}
