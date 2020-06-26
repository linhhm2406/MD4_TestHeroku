package com.atag.atagbank.formatter;

import com.atag.atagbank.model.Account;
import com.atag.atagbank.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

@Component
public class AccountFormatter implements Formatter<Account> {
    AccountService accountService;
    @Autowired
    public AccountFormatter (AccountService accountService){
        this.accountService = accountService;
    }
    @Override
    public Account parse(String text, Locale locale) throws ParseException {
        Optional<Account> optionalAccount = accountService.findById(Long.parseLong(text));
        Account account = optionalAccount.get();
        return account;
    }

    @Override
    public String print(Account object, Locale locale) {
        return null;
    }
}
