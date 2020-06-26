package com.atag.atagbank.controller;

import com.atag.atagbank.model.Account;
import com.atag.atagbank.model.MyUser;
import com.atag.atagbank.model.OtpCode;
import com.atag.atagbank.model.Transaction;
import com.atag.atagbank.service.MyEmailService;
import com.atag.atagbank.service.OTPService;
import com.atag.atagbank.service.account.IAccountService;
import com.atag.atagbank.service.exception.TransactionException;
import com.atag.atagbank.service.otpCode.IOtpCodeService;
import com.atag.atagbank.service.transaction.ITransactionService;
import com.atag.atagbank.service.user.MyUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.transaction.TransactionalException;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/transaction")
public class TransactionController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ITransactionService iTransactionService;
    @Autowired
    IAccountService iAccountService;
    @Autowired
    MyUserService myUserService;

    @Autowired
    IOtpCodeService iOtpCodeService;
    @Autowired
    OTPService otpService;

    @Autowired
    public MyEmailService myEmailService;

    @ModelAttribute("accounts")
    public Iterable<Account> accounts() {
        return iAccountService.findAll();
    }

    @GetMapping("/OTPPage")
    public String getOPTPage() {
        return "transaction/OTPPage";
    }

    @GetMapping("/listing")
    ModelAndView getAllTransaction(@PageableDefault(sort = "time", direction = Sort.Direction.DESC) Pageable pageable, HttpSession session) {
        Page<Transaction> transactions;
        ModelAndView modelAndView = new ModelAndView("transaction/listing");
        String currentUserName = (String) session.getAttribute("currentUserName");
        MyUser currentUser = myUserService.findByName(currentUserName);
        transactions = iTransactionService.findAllByAccount(currentUser.getAccount(), pageable);
        modelAndView.addObject("transactions", transactions);
        return modelAndView;
    }

    @GetMapping("/create")
    ModelAndView showCreateTransactionForm(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("transaction/create");
        String username = (String) session.getAttribute("currentUserName");
        MyUser currentUser = myUserService.findByName(username);
        int otp = otpService.generateOTP(username);
        logger.info("OTP : " + otp);
        myEmailService.sendOtpMessage(currentUser.getEmail(), "OTP -SpringBoot", String.valueOf(otp));
        modelAndView.addObject("otpCode", new OtpCode());
        modelAndView.addObject("transaction", new Transaction());
        return modelAndView;
    }

    @PostMapping("/create")
    ModelAndView createTransaction(@Valid @ModelAttribute("transaction") Transaction transaction, BindingResult bindingResult, @RequestParam(name = "otp") String otp, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("transaction/create");
        String otpCode = otp;
        Transaction syncTransaction;
        String currentUsername = (String) session.getAttribute("currentUserName");
        MyUser currentUser = myUserService.findByName(currentUsername);
        String currentEmail = currentUser.getEmail();
        String otpCodeInDB = iOtpCodeService.findByEmail(currentEmail).getCode();

        if (bindingResult.hasFieldErrors()) {
            modelAndView.addObject("errorMessage", "Amount must be positive number!");
            return modelAndView;
        } else {
            try {
                if (!otpCode.equals(otpCodeInDB)) {
                    modelAndView.addObject("errorMessage", "OTP invalid!");
                    return modelAndView;
                }
                iOtpCodeService.removeByEmail(currentEmail);
                String senderName = (String) session.getAttribute("currentUserName");
                MyUser senderAccount = myUserService.findByName(senderName);
                String receiverName = String.valueOf(transaction.getAccount().getId());
                //Receiver scope
                transaction.setType("CREDIT");
                transaction.setPartnerAccount(String.valueOf(senderAccount.getAccount().getId()));
                transaction.setTime(new Timestamp(System.currentTimeMillis()));
                //Sender scope
                syncTransaction = getSyncTransaction(transaction, senderAccount, receiverName);
                iAccountService.transfer(senderAccount.getAccount().getId(), transaction.getAccount().getId(), transaction.getAmount(), otp);
            } catch (TransactionException transactionException) {
                iOtpCodeService.removeByEmail(currentEmail);
                modelAndView.addObject("errorMessage", transactionException.getMessage());
                return modelAndView;
            }
            iTransactionService.save(syncTransaction);
            iTransactionService.save(transaction);
            modelAndView.addObject("transaction", new Transaction());
            modelAndView.addObject("message", "Send successfully");
            return modelAndView;
        }

    }

    private Transaction getSyncTransaction(@ModelAttribute("transaction") @Valid Transaction transaction, MyUser senderAccount, String receiverName) {
        Transaction syncTransaction = new Transaction();
        syncTransaction.setTime(new Timestamp(System.currentTimeMillis()));
        syncTransaction.setAmount(transaction.getAmount());
        syncTransaction.setType("DEBIT");
        syncTransaction.setAccount(senderAccount.getAccount());
        syncTransaction.setTransactionMessage(transaction.getTransactionMessage());
        syncTransaction.setPartnerAccount(receiverName);
        return syncTransaction;
    }
}
