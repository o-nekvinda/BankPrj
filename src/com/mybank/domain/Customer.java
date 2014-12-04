/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mybank.domain;

import java.util.ArrayList;
import java.util.List;

public class Customer {

    private List<Account> accounts;
    private String firstName;
    private String surName;

    public Customer(String f, String l) {
        this.accounts = new ArrayList<>();
        this.firstName = f;
        this.surName = l;
    }

    public void addAccount(Account acct) {
        accounts.add(acct);
    }

    public int getNumOfAccounts() {
        return accounts.size();
    }

    public Account getAccount(int index) {
        return this.accounts.get(index);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurName() {
        return surName;
    }
}
