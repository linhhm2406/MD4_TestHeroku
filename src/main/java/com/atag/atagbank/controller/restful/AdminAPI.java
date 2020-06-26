package com.atag.atagbank.controller.restful;

import com.atag.atagbank.model.MyUser;
import com.atag.atagbank.service.user.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminAPI {
    @Autowired
    MyUserService myUserService;

    @GetMapping("/user/list")
    public ResponseEntity<Page<MyUser>> showUsers(Pageable pageable) {
        Page<MyUser> users = myUserService.findAll(pageable);
        if (users == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/user/create")
    public ResponseEntity<MyUser> createUser(@RequestBody MyUser user) {
        myUserService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
