package com.example.demo.accounts;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class AccountNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public AccountNotFoundException(int aid) {
        super("Could not find account " + aid);
    }
}
