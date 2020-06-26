package com.atag.atagbank.service;
import com.atag.atagbank.model.OtpCode;
import com.atag.atagbank.service.otpCode.OtpCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MyEmailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private OtpCodeService otpCodeService;

    public void sendOtpMessage(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        logger.info(subject);
        logger.info(to);
        logger.info(message);
        OtpCode otpCode = new OtpCode(message,to);
        otpCodeService.save(otpCode);
        javaMailSender.send(simpleMailMessage);
    }
}
