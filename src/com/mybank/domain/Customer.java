/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mybank.domain;

/**
 *
 * @author STUDENT-FES-EA03029
 */
public class Customer {

    private String firstName;
    private String lastName;
    private Account accounts[] = new Account[999];
    private int numbeOfAccounts;

    public Customer(String f, String l) {
        this.firstName = f;
        this.lastName = l;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Account getAccount(int i) {
        return this.accounts[i];
    }

    public void addAccount(Account acct) {
        this.accounts[numbeOfAccounts] = acct;
        numbeOfAccounts++;
    }

    public int getNumOfAccounts() {
        return numbeOfAccounts;
    }
}
