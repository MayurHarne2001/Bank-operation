package com.example.Bank.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AccNotFoundException extends RuntimeException{

    public AccNotFoundException(String message){
        super(message);
    }
}
