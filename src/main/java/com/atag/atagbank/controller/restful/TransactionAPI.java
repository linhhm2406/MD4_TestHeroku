package com.atag.atagbank.controller.restful;

import com.atag.atagbank.model.Transaction;
import com.atag.atagbank.service.transaction.ITransactionService;
import org.omg.IOP.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionAPI {

    @Autowired
    ITransactionService iTransactionService;

    @RequestMapping(value = "/listing",method = RequestMethod.GET)
    ResponseEntity<List<Transaction>> getAllTransaction(Pageable pageable){
        List<Transaction> transactions = iTransactionService.findAll(pageable).getContent();
        if(transactions==null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(transactions,HttpStatus.OK);
    }
}
