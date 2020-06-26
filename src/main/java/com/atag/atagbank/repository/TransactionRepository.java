package com.atag.atagbank.repository;

import com.atag.atagbank.model.Account;
import com.atag.atagbank.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository <Transaction,Long>{
    Page<Transaction> findAllByAccount(Account account, Pageable pageable);
}
