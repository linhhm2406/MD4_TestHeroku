package com.atag.atagbank.service.confirmationToken;

import com.atag.atagbank.model.ConfirmationToken;

public interface IConfirmationTokenService {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
    void save(ConfirmationToken confirmationToken);
}
