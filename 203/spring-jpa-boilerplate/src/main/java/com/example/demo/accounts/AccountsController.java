package com.example.demo.accounts;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import org.springframework.web.server.ResponseStatusException;

import com.example.demo.security.AuthorizedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import com.example.demo.user.User;

import com.example.demo.user.UserService;

import com.example.demo.config.*;

@RestController
public class AccountsController {
    // @Autowired
    private AccountsRepository accRepository;

    // @Autowired
    private UserService usrService;

    @Autowired
    public AccountsController(AccountsRepository accRepository, UserService usrService) {
        this.accRepository = accRepository;
        this.usrService = usrService;
    }

    @PostMapping(path="/accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Account addAccount (@RequestBody Account account) throws NotFoundException, UnauthorizedException {
        AuthorizedUser context = new AuthorizedUser();
        context.validate();
        account.setAvailableBalance(account.getBalance());

        // Check if current user associated with new account exists
        if (usrService.findById(account.getCustomer_id()) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return accRepository.save(account);
        // return "Account saved!\n" + account.toString();
    }

    @GetMapping(path="/accounts")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<Account> getAccounts() throws NotFoundException, UnauthorizedException {
        AuthorizedUser context = new AuthorizedUser();
        context.validate();

        int userID = context.getId();
        
        Iterable<Account> accounts = accRepository.findAll();
        Iterator<Account> iter = accounts.iterator();

        while(iter.hasNext()) {
            Account acc = iter.next();
            if(acc.getCustomer_id() != userID) {
                iter.remove();
            }
        }
        return accounts;
    }

    @GetMapping(path="/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Account getAccountById(@PathVariable int id) throws NotFoundException, UnauthorizedException {
        AuthorizedUser context = new AuthorizedUser();
        context.validate();

        int userID = context.getId();

        Optional<Account> accountEntity = accRepository.findById(id);
        Account account;
        if (!accountEntity.isPresent()) {
            throw new AccountNotFoundException(id);
        } else {
            account = accountEntity.get();
            if (account.getCustomer_id() != userID) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }

        return account;
    }
}