package com.atag.atagbank.repository;

import com.atag.atagbank.model.MyUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MyUserRepository extends PagingAndSortingRepository<MyUser, Long>{
    MyUser findByUsername(String username);
    MyUser findByName(String name);
    MyUser findByEmail(String email);
    @Query("select u from MyUser u where u.name like %?1%"
            + "or u.address like %?1%"
    +"or u.role.role like %?1%"
    +"or u.username like %?1%")
    List<MyUser> findByNameOrUsernameOrAddressOrRole_RoleLike(String keyword);
}
