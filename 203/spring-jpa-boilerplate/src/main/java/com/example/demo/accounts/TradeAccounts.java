package com.example.demo.accounts;

import com.example.demo.config.NotFoundException;
import com.example.demo.config.UnauthorizedException;

import org.springframework.beans.factory.annotation.Autowired; 

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import org.springframework.web.server.ResponseStatusException;

public class TradeAccounts {

    private AccountsController accController;

    @Autowired
    public TradeAccounts(AccountsController accController) {
        this.accController = accController;
    }

    // Return account requested with ID
    public Account returnAccountWithID(int id) throws NotFoundException, UnauthorizedException {
        return accController.getAccountById(id);
    }

    // Verify account belongs to customer
    public boolean verifyAccountOwnership(int customer_id, int account_id)
    throws NotFoundException, UnauthorizedException {
        Account account = accController.getAccountById(account_id);
        if (account.getCustomer_id() != customer_id) return false;
        return true;
    }

}