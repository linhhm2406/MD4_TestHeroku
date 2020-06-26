package com.atag.atagbank.controller;

import com.atag.atagbank.model.*;
import com.atag.atagbank.service.EmailSenderService;
import com.atag.atagbank.service.account.IAccountService;
import com.atag.atagbank.service.confirmationToken.IConfirmationTokenService;
import com.atag.atagbank.service.role.IRoleService;
import com.atag.atagbank.service.user.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Random;
import java.util.UUID;

@Controller
@SessionAttributes("currentUser")
public class MainController {

    @ModelAttribute("currentUser")
    public MyUser getCurrentUser() {
        return new MyUser();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IAccountService accountService;
    @Autowired
    MyUserService myUserService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private IConfirmationTokenService confirmationTokenService;

    @GetMapping("/")
    public ModelAndView getHomePage(HttpSession session) {
        MyUser currentUser = getUserFromPrincipal();
        if (currentUser != null) {
            session.setAttribute("currentUserName", currentUser.getName() + "-" + currentUser.getRole().getRole());
        }
        return new ModelAndView("index");
    }

    private MyUser getUserFromPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != "anonymousUser") {
            String username = ((UserDetails) principal).getUsername();
            return myUserService.findByUserName(username);
        }
        return null;
    }

    @GetMapping("/login")
    public ModelAndView getLoginForm(@ModelAttribute MyUser currentUser) {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("currentUser", currentUser);
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute MyUser currentUser, HttpSession session) {
        MyUser loginUser = myUserService.findByUserName(currentUser.getUsername());
        if (loginUser != null && currentUser.getPassword().equals(loginUser.getPassword())) {
            if (loginUser.isEnabled()) {
                session.setAttribute("currentUser", loginUser);
                session.setAttribute("currentUserName", loginUser.getName());
                ModelAndView modelAndView = new ModelAndView("/");
                modelAndView.addObject("currentUser", loginUser);
                return modelAndView;
            } else {
                return new ModelAndView("login", "deactivated", "Account is deactivated. Please contact Admin to active!");
            }
        }
        return new ModelAndView("login", "notFound", "Wrong username or password!");
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        MyUser user = new MyUser();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid @ModelAttribute("user") MyUser user, BindingResult bindingResult) {
        Long id = Long.valueOf(myUserService.findAllList().size() + 1);
        user.setId(id);
        if (myUserService.isRegister(user)) {
            ModelAndView modelAndView = new ModelAndView("registration");
            modelAndView.addObject("message", "Username or email is already registered");
            return modelAndView;
        }
        if (!myUserService.isCorrectConfirmPassword(user)) {
            ModelAndView modelAndView = new ModelAndView("registration");
            modelAndView.addObject("message", "Confirm Password is incorrect");
            return modelAndView;
        }
        if (bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView1 = new ModelAndView();
            modelAndView1.setViewName("registration");
            modelAndView1.addObject("user", user);
            return modelAndView1;
        } else {
            ModelAndView modelAndView = new ModelAndView("successfulRegisteration");
            Role userRole = new Role(2L,"ROLE_USER");
            user.setRole(userRole);
            user.setUsername(user.getUsername());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
            user.setEmail(user.getEmail());
            myUserService.save(user);
            ConfirmationToken confirmationToken = new ConfirmationToken(user);

            confirmationTokenService.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("huynhxuanbui@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    + "http://localhost:5000/confirm-account?token=" + confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);

            modelAndView.addObject("email", user.getEmail());
            modelAndView.addObject("successMessage", "User has been registered successfully");
            return modelAndView;
        }
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") String confirmationToken) {
        ConfirmationToken token = confirmationTokenService.findByConfirmationToken(confirmationToken);

        if(token != null){
            MyUser user = token.getUser();
            user.setEnabled(true);
            Account account = new Account();
            Random random = new Random();
            Long accountId = Long.valueOf(100000 + random.nextInt(900000));
            float balane = 50000;
            account.setId(accountId);
            account.setBalance(balane);
            accountService.save(account);
            user.setAccount(account);
            myUserService.save(user);
            modelAndView.setViewName("accountVerified");
        } else {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }

    @GetMapping("/forgotPassword")
    public ModelAndView forgotPasswordForm() {
        ModelAndView modelAndView = new ModelAndView("forgotPassword");
        modelAndView.addObject("passwordForgot", new PasswordForgot());
        return modelAndView;
    }

    @PostMapping("/forgotPassword")
    public ModelAndView forgotPassword(@Valid @ModelAttribute PasswordForgot passwordForgot, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return new ModelAndView("forgotPassword");
        }
        MyUser user = myUserService.findByEmail(passwordForgot.getEmail());
        if (user == null) {
            ModelAndView modelAndView = new ModelAndView("forgotPassword");
            modelAndView.addObject("message", "Your email isn't exist");
            return modelAndView;
        }
//        ModelAndView modelAndView = new ModelAndView("newPassword");
        ModelAndView modelAndView = new ModelAndView("successfulRegisteration");
        modelAndView.addObject("passwordForgot", passwordForgot);
        ConfirmationToken token = new ConfirmationToken(user);
        token.setConfirmationToken(UUID.randomUUID().toString());
        token.setExpiryDate(1);
        confirmationTokenService.save(token);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("You've successfully requested a new password reset!");
        mailMessage.setText("To change you password, please click here : "
                + "http://localhost:8080/newPassword/" + user.getId()
                + "?token=" + token.getConfirmationToken());
        emailSenderService.sendEmail(mailMessage);
        modelAndView.addObject("email", user.getEmail());
        return modelAndView;
    }

    @GetMapping("/newPassword/{id}")
    public ModelAndView showEditForm(@PathVariable Long id, @RequestParam("token") String confirmationToken) {
        ConfirmationToken token = confirmationTokenService.findByConfirmationToken(confirmationToken);
        if (token != null) {
            boolean isExpired = token.isExpired();
            if (!isExpired) {
                MyUser user = myUserService.findById(id);
                if (user != null) {
                    ModelAndView modelAndView = new ModelAndView("newPassword");
                    modelAndView.addObject("user", user);
                    return modelAndView;
                }
            }
        } else {
            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.addObject("message", "The link is invalid or broken!");
            return modelAndView;
        }
        return new ModelAndView("error");
    }

    @RequestMapping(value = "/newPassword",method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView editUser(@ModelAttribute MyUser user,BindingResult bindingResult) {
        ConfirmationToken token = new ConfirmationToken(user);
        ModelAndView modelAndView = new ModelAndView("newPassword");
        if (user.getPassword().length()<6){
            modelAndView.addObject("message","Password length must be between 6 and 15");
            return modelAndView;
        }
        if (!myUserService.isCorrectConfirmPassword(user)) {
            modelAndView.addObject("message", "Your confirm password is incorrect");
            return modelAndView;
        } else {
            String newPassword = passwordEncoder.encode(user.getPassword());
            user = myUserService.findById(user.getId());
            user.setPassword(newPassword);
            myUserService.save(user);
            modelAndView.addObject("user", user);
            modelAndView.addObject("message", "Your password is changed");
        }
        return modelAndView;
    }

    @GetMapping("/accessDenied")
    public ModelAndView showAccessDeniedPage() {
        return new ModelAndView("error", "message", "You don't have access to go to this page");
    }

    @GetMapping("/reactivated")
    public ModelAndView showReactiveForm(){
        return new ModelAndView("reactivatedForm");
    }

    @PostMapping("/reactivated")
    public ModelAndView reactiveForm(@RequestParam("username") String username){
        MyUser user = myUserService.findByUserName(username);
        if (user==null){
            return new ModelAndView("reactivatedForm","noHaveAccount","Not Found Username");
        }
        user.setEnabled(true);
        myUserService.save(user);
        return new ModelAndView("reactivatedForm","success","Successfully!");
    }
}