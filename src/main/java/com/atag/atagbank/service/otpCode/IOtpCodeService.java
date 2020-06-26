package com.atag.atagbank.service.otpCode;

import com.atag.atagbank.model.OtpCode;

public interface IOtpCodeService {
    void save(OtpCode otpCode);

    OtpCode findByEmail(String email);

    void removeByEmail(String email);
}
