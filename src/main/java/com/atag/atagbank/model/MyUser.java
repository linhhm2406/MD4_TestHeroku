package com.atag.atagbank.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class MyUser {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "username must be fill")
    @Size(min = 6, max = 15, message = "username length must be between 6 and 15")
    private String username;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "phone number must be fill")
    @Size(min = 10, max = 10, message = "phone number have 10 characters")
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "email must be fill")
    @Email(message = "Incorrect form")
    private String email;

    @NotEmpty(message = "password must be fill")
    @Size(min = 6, message = "password length must be at least 6 characters")
    private String password;

    @NotEmpty(message = "confirm password must be fill")
    @Size(min = 6, message = "password length must be at least 6 characters")
    private String confirmPassword;

    @Column(name = "status", nullable = false)
    private boolean enabled;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "roles_id")
    private Role role;

    @Column(name = "dateOfBirth")
    private String dateOfBirth;

    @Column(name = "address")
    private String address;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "users_roles",
//            joinColumns = {@JoinColumn(name = "user_id")},
//            inverseJoinColumns = {@JoinColumn(name = "role_id")})
//    private Set<Role> roles = new HashSet<>();

    @OneToOne
    private Account account;

    public MyUser() {
    }

    public MyUser(@NotEmpty(message = "username must be fill") @Size(min = 6, max = 15, message = "username length must be between 6 and 15") String username, @NotEmpty(message = "email must be fill") @Email(message = "Incorrect form") String email, @NotEmpty(message = "password must be fill") @Size(min = 6, message = "password length must be at least 6 characters") String password, @NotEmpty(message = "confirm password must be fill") @Size(min = 6, message = "password length must be at least 6 characters") String confirmPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public MyUser(@NotEmpty(message = "username must be fill") @Size(min = 6, max = 15, message = "username length must be between 6 and 15") String username,
                  @NotEmpty(message = "email must be fill") @Email(message = "Incorrect form") String email,
                  @NotEmpty(message = "password must be fill") @Size(min = 6, message = "password length must be at least 6 characters") String password,
                  @NotEmpty(message = "confirm password must be fill") @Size(min = 6, message = "password length must be at least 6 characters") String confirmPassword,
                  boolean enabled,
                  String name,
                  String dateOfBirth,
                  String address, Role role,
                  Account account,
                  String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.enabled = enabled;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.role = role;
        this.account = account;
        this.phoneNumber=phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
