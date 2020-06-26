package com.atag.atagbank.controller.restful;

import com.atag.atagbank.model.Account;
import com.atag.atagbank.model.MyUser;
import com.atag.atagbank.service.account.IAccountService;
import com.atag.atagbank.service.user.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
@SessionAttributes("currentUser")
@RequestMapping("/admin/api")
public class UserAPI {

    @Autowired
    IAccountService accountService;

    @ModelAttribute("currentUser")
    MyUser recentUser() {
        return new MyUser();
    }
    @GetMapping(value = "makeDeposit", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Void> makeDeposit(@RequestParam("amount") String amount, @SessionAttribute MyUser currentUser) {
        accountService.addMoneyToAccount(Float.parseFloat(amount), currentUser.getAccount().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
