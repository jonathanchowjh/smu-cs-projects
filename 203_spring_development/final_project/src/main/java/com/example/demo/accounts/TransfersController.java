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

import com.example.demo.config.*;
import com.example.demo.security.*;

@RestController
public class TransfersController {

    @Autowired
    private TransfersRepository transfersRepository;

    @Autowired
    private AccountsRepository accRepository;

    public TransfersController() {}

    @GetMapping(path="/accounts/{id}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<Transfer> getTransfers(@PathVariable int id) throws NotFoundException, UnauthorizedException {
        AuthorizedUser context = new AuthorizedUser();
        context.validate();

        verifyAccountOwnership(context.getId(), id);

        Iterable<Transfer> transfers = transfersRepository.findAll();
        Iterator<Transfer> iter = transfers.iterator();

        while (iter.hasNext()) {
            Transfer transfer = iter.next();
            if (transfer.getFrom() != id && transfer.getTo() != id) {
                iter.remove();
            }
        }

        return transfers;
    }

    @PostMapping(path="/accounts/{id}/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Transfer createTransfer(@RequestBody Transfer transfer, @PathVariable int id) throws NotFoundException, UnauthorizedException {

        AuthorizedUser context = new AuthorizedUser();
        context.validate();

        verifyAccountOwnership(context.getId(), id);

        int sender_account_id = transfer.getFrom();
        int receiver_account_id = transfer.getTo();

        if (id != transfer.getFrom()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Optional<Account> sender_account = accRepository.findById(sender_account_id);
        Optional<Account> receiver_account = accRepository.findById(receiver_account_id);
        if (!sender_account.isPresent()) throw new AccountNotFoundException(sender_account_id);
        if (!receiver_account.isPresent()) throw new AccountNotFoundException(receiver_account_id);

        double transfer_amount = transfer.getAmount();

        Account sender = sender_account.get();
        Account receiver = receiver_account.get();

        if (sender.getBalance() < transfer_amount) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        sender.updateBalance(-transfer_amount);
        receiver.updateBalance(transfer_amount);

        accRepository.save(sender);
        accRepository.save(receiver);
        return(transfersRepository.save(transfer));
    }

    private void verifyAccountOwnership(int user_id, int account_id) {
        Account acc = findAccount(account_id);
        if (acc.getCustomer_id() != user_id) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    private Account findAccount(int account_id) {
        Optional<Account> accountEntity = accRepository.findById(account_id);
        if (!accountEntity.isPresent()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return accountEntity.get();
    }

}