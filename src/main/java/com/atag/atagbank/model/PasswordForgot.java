package com.atag.atagbank.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class PasswordForgot {
    @Email
    @NotEmpty(message = "Please input your email")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
