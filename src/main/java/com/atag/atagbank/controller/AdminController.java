package com.atag.atagbank.controller;

import com.atag.atagbank.model.Account;
import com.atag.atagbank.model.MyUser;
import com.atag.atagbank.model.Role;
import com.atag.atagbank.model.Transaction;
import com.atag.atagbank.service.account.IAccountService;
import com.atag.atagbank.service.role.IRoleService;
import com.atag.atagbank.service.transaction.ITransactionService;
import com.atag.atagbank.service.user.MyUserService;
import jdk.nashorn.internal.ir.Optimistic;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
//@RequestMapping("/admin")
public class AdminController {

    @Autowired
    MyUserService myUserService;

    @Autowired
    IRoleService roleService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    IAccountService accountService;
    @Autowired
    ITransactionService iTransactionService;

    @ModelAttribute("roleList")
    Iterable<Role> roleList(){
        return roleService.findAll();
    }


    @GetMapping("/admin/customerManagement")
    public ModelAndView showManagementForm(@PageableDefault(10) Pageable pageable){
        Page<MyUser> userList = myUserService.findAll(pageable);
        return new ModelAndView("admin/customerManagement","userList",userList);
    }

    @GetMapping("/admin/user-update/{id}")
    public ModelAndView showUpdateForm(@PathVariable Long id , @ModelAttribute("user") MyUser user){
        ModelAndView modelAndView = new ModelAndView("admin/editCustomer");
        user = myUserService.findById(id);
        modelAndView.addObject("user",user);
        return modelAndView;
    }

    @PostMapping("/admin/user-update")
    public ModelAndView updateUser(@ModelAttribute MyUser user) {
        ModelAndView modelAndView = new ModelAndView("admin/editCustomer");
        myUserService.save(user);
        modelAndView.addObject("user",user);
        modelAndView.addObject("announcement","Successfully!");
        return modelAndView;
    }

    @GetMapping("/admin/createNewCustomer")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("admin/createCustomer");
        modelAndView.addObject("user",new MyUser());
        return modelAndView;
    }

    @PostMapping("/admin/createNewCustomer")
    public ModelAndView createNewCustomer(@Validated @ModelAttribute("user") MyUser user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("admin/createCustomer");
            return modelAndView;
        }

        if (myUserService.findByUserName(user.getUsername())!=null){
            ModelAndView modelAndView = new ModelAndView("admin/createCustomer");
            modelAndView.addObject("user",user);
            modelAndView.addObject("announce","Already exist!");
            return modelAndView;
        }

        List<MyUser> userList = myUserService.findAllList();
        user.setId(Long.parseLong(String.valueOf(userList.size()+1)));
        Account account = new Account();

        Date now = new Date();
        String accountNumber = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(now);

        account.setId(Long.parseLong(accountNumber));
        account.setBalance(50000F);

        user.setAccount(account);
        accountService.save(account);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        myUserService.save(user);

        ModelAndView modelAndView = new ModelAndView("admin/createCustomer");
        modelAndView.addObject("user",user);
        modelAndView.addObject("announceSuccess","Successfully!");

        return modelAndView;
    }

    @GetMapping("/admin/searchUser")
    public ModelAndView searchUser(@Param("keyword")String keyword){
        ModelAndView modelAndView =new ModelAndView("/admin/customerManagement");
        List<MyUser> users =myUserService.findByNameOrUsernameOrAddressOrRole_RoleLike("%"+keyword+"%");
        if (users.isEmpty()){
            modelAndView.addObject("message","User Not Found");
            return modelAndView;
        }
        modelAndView.addObject("userList",users);
        return modelAndView;
    }

    @GetMapping("/admin/makeDeposit/{id}")
    public ModelAndView showMakeDepositForm(@PathVariable("id") Long id){
        ModelAndView modelAndView =new ModelAndView("/admin/makeDeposit");
        MyUser currentUser = myUserService.findById(id);
        Account currentAccount =currentUser.getAccount();
        modelAndView.addObject("account",currentAccount);
        return modelAndView;
    }

    @PostMapping("/admin/makeDeposit")
    public ModelAndView makeDeposit(@ModelAttribute("account") Account account,@RequestParam("amount") String amount){
        ModelAndView modelAndView =new ModelAndView("/admin/makeDeposit");
        Float amountNeededToDeposit = Float.parseFloat(amount);
        Account currentAccount = accountService.findById(account.getId()).get();
        Float currentBalance = currentAccount.getBalance();
        currentAccount.setBalance(currentBalance+amountNeededToDeposit);
        accountService.save(currentAccount);
        modelAndView.addObject("account",account);
        modelAndView.addObject("message","Make deposit to account successfully!");
        return modelAndView;
    }

    @GetMapping("/admin/transactionListing")
    ModelAndView getAllTransaction(@PageableDefault(sort = "time", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Transaction> transactions;
        ModelAndView modelAndView = new ModelAndView("/admin/transactionListing");
        transactions = iTransactionService.findAll( pageable);
        modelAndView.addObject("transactions", transactions);
        return modelAndView;
    }

    @GetMapping("/admin/listingTransactionByUserID{id}")
    ModelAndView getAllTransaction(@PageableDefault(sort = "time", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable("id")Long id) {
        Page<Transaction> transactions;
        ModelAndView modelAndView = new ModelAndView("/admin/transactionListing");
        MyUser currentUser = myUserService.findById(id);
        Account currentAccount = currentUser.getAccount();
        transactions = iTransactionService.findAllByAccount(currentAccount, pageable);
        modelAndView.addObject("transactions", transactions);
        return modelAndView;
    }

    @GetMapping("/admin/viewCustomerDetail{id}")
    ModelAndView viewCustomerDetail(@PathVariable("id")Long id) {
        MyUser myUser;
        ModelAndView modelAndView = new ModelAndView("/admin/viewCustomerDetail");
        MyUser currentUser = myUserService.findById(id);
        modelAndView.addObject("customer", currentUser);
        return modelAndView;
    }

    @GetMapping("/admin/deactive/{id}")
    public ModelAndView deactiveUser(@PathVariable Long id,@PageableDefault(10) Pageable pageable){
        MyUser user = myUserService.findById(id);
        user.setEnabled(false);
        myUserService.save(user);
        Page<MyUser> userList = myUserService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("admin/customerManagement");
        modelAndView.addObject("userList",userList);
        return modelAndView;
    }

    @GetMapping("/admin/active/{id}")
    public ModelAndView activeUser(@PathVariable Long id,@PageableDefault(10) Pageable pageable){
        MyUser user = myUserService.findById(id);
        user.setEnabled(true);
        myUserService.save(user);
        Page<MyUser> userList = myUserService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("admin/customerManagement");
        modelAndView.addObject("userList",userList);
        return modelAndView;
    }
}
