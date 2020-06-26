package com.atag.atagbank.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    không để auto sinh ra, id lấy bằng số tài khoản luôn, số tk có 6 chữ số
    private Long id;

    @Column(name = "balance")
    private Float balance;

    @OneToMany(targetEntity = Transaction.class)
    private List<Transaction> transactions;

    public Account() {
    }

    public Account(Float balance) {
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }
}
