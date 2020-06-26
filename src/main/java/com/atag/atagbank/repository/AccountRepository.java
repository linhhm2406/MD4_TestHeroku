package com.atag.atagbank.repository;

import com.atag.atagbank.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
//@Component
public interface AccountRepository extends CrudRepository<Account,Long> {
}
