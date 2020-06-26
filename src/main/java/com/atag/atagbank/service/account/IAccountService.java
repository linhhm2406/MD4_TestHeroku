package com.atag.atagbank.service.account;

import com.atag.atagbank.model.Account;
import com.atag.atagbank.service.exception.TransactionException;

import java.util.Optional;

public interface IAccountService {
    Iterable<Account> findAll();
    Optional<Account> findById(Long id);
    void addMoneyToAccount(Float amount, Long id);
    void save(Account account);
    void minusMoneyFromAccount(Float amount,Long id) throws TransactionException;
    void transfer(Long senderAccountID,Long receiverAccountID, Float amount,String otp) throws TransactionException;
    boolean checkBalance(float amount,Account account);
}
