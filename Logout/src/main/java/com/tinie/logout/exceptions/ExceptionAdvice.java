package com.tinie.logout.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * Handle thrown {@link UnauthorisedException} and return a {@link ResponseEntity} with status code of {@literal 401}
     * @param e Instance of {@link UnauthorisedException} thrown
     * @return Instance of {@link ResponseEntity}
     */
    @ExceptionHandler(value = UnauthorisedException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> unauthorisedExceptionHandler(UnauthorisedException e){
        var map = new HashMap<String, String>();
        map.put("phonenumber", String.valueOf(e.getPhoneNumber()));
        map.put("event", "unauthorised");
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }
}
