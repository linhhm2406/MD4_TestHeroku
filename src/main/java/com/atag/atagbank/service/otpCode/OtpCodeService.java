package com.atag.atagbank.service.otpCode;

import com.atag.atagbank.model.OtpCode;
import com.atag.atagbank.repository.OtpCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpCodeService implements IOtpCodeService{
    @Autowired
    OtpCodeRepository otpCodeRepository;
    @Override
    public void save(OtpCode otpCode) {
        otpCodeRepository.save(otpCode);
    }

    @Override
    public OtpCode findByEmail(String email) {
        return otpCodeRepository.findByEmail(email).orElse(null);
    }

    @Override
    public void removeByEmail(String email) {
        otpCodeRepository.deleteByEmail(email);
    }
}
