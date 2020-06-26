package com.atag.atagbank.service.user;

import com.atag.atagbank.model.MyUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface MyUserService extends UserDetailsService {
    Page<MyUser> findAll(Pageable pageable);

    List<MyUser> findAllList();

    void save(MyUser user);

    MyUser findById(Long id);

    MyUser findByUserName(String username);

    MyUser findByName(String name);

    MyUser findByEmail(String email);

    List<MyUser> findByNameOrUsernameOrAddressOrRole_RoleLike(String keyword);

    boolean isRegister(MyUser user);

    boolean isCorrectConfirmPassword(MyUser user);
}
