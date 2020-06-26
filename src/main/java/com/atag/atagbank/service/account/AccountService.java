package com.atag.atagbank.service.account;

import com.atag.atagbank.model.Account;
import com.atag.atagbank.repository.AccountRepository;
import com.atag.atagbank.service.exception.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Component
public class AccountService implements IAccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired


    @Override
    public Iterable<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void addMoneyToAccount(Float amount, Long id) {
        Optional<Account> currentAccountOptional = this.findById(id);
        if (currentAccountOptional.isPresent()) {
            Account currentAccount = currentAccountOptional.get();
            currentAccount.setBalance(currentAccount.getBalance() + amount);
            save(currentAccount);
        }
    }

    @Transactional (propagation = Propagation.MANDATORY)
    @Override
    public void minusMoneyFromAccount(Float amount, Long id) throws TransactionException {
        Optional<Account> currentAccountOptional = this.findById(id);
        if (currentAccountOptional.isPresent()) {
            Account currentAccount = currentAccountOptional.get();
            if(!checkBalance(amount,currentAccount)){
                throw new TransactionException("Your balance is not enough to make this transfer");
            }
            currentAccount.setBalance(currentAccount.getBalance() - amount);
            save(currentAccount);
        }else throw new TransactionException("Account not found!");
    }

    @Override
    public boolean checkBalance(float amount, Account account) {
        if (account.getBalance() <= amount) {
            return false;
        }
        return true;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = TransactionException.class)
    public void transfer(Long senderAccountID, Long receiverAccountID,Float amount,String otp)throws TransactionException{
        minusMoneyFromAccount(amount,senderAccountID);
        addMoneyToAccount(amount,receiverAccountID);
    }
}
