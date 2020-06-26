//package com.atag.atagbank.controller;
//
//import com.atag.atagbank.model.MyUser;
//import com.atag.atagbank.model.OtpCode;
//import com.atag.atagbank.service.MyEmailService;
//import com.atag.atagbank.service.OTPService;
//import com.atag.atagbank.service.otpCode.IOtpCodeService;
//import com.atag.atagbank.service.user.MyUserService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpSession;
//
//@Controller
//public class OTPController {
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    public OTPService otpService;
//
//    @Autowired
//    public MyEmailService myEmailService;
//
//    @Autowired
//    public IOtpCodeService iOtpCodeService;
//
//    @Autowired
//    public MyUserService myUserService;
//
//    @GetMapping("/generateOTP")
//    public ModelAndView generateOTP(@ModelAttribute("otpCode") OtpCode otpCode, HttpSession session) {
//        ModelAndView modelAndView = new ModelAndView("personal/confirmOTP");
//        String username = (String) session.getAttribute("currentUserName");
//        MyUser currentUser = myUserService.findByName(username);
//        int otp = otpService.generateOTP(username);
//        logger.info("OTP : " + otp);
//        myEmailService.sendOtpMessage(currentUser.getEmail(), "OTP -SpringBoot", String.valueOf(otp));
//        modelAndView.addObject("otpCode", new OtpCode());
//        return modelAndView;
//    }
//
//    @PostMapping("/validateOTP")
//    public ModelAndView validateOTP(@ModelAttribute("otpCode") OtpCode otpCode, HttpSession session) {
//        String currentUsername = (String) session.getAttribute("currentUserName");
//        MyUser currentUser = myUserService.findByName(currentUsername);
//        String currentEmail = currentUser.getEmail();
//        ModelAndView modelAndView = new ModelAndView("personal/confirmOTP");
//        String otpCodeInDB = iOtpCodeService.findByEmail(currentEmail).getCode();
//        if (otpCode.getCode().equals(otpCodeInDB)) {
//            modelAndView.addObject("message", "OTP valid");
//            iOtpCodeService.removeByEmail(currentEmail);
//            return modelAndView;
//        }
//        modelAndView.addObject("errorMessage", "OTP invalid");
//        return modelAndView;
//    }
//}
