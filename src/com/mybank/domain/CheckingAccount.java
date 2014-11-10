/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mybank.domain;

/**
 *
 * @author onekv_000
 */
public class CheckingAccount extends Account {

    private double overdraftAmount;

    public CheckingAccount(double initBalance, double overdraft) {
        super(initBalance);
        this.overdraftAmount = overdraft;
    }

    public CheckingAccount(double initBalance) {
        super(initBalance);
    }

    @Override
    public boolean withdraw(double amt) {
        if (amt > 0 && this.balance - amt > 0 - this.overdraftAmount) {
            if (this.balance - amt < 0) {
                this.overdraftAmount += this.balance - amt;;
                this.balance = 0;
            } else {
                this.balance = this.balance - amt;
            }
            return true;
        }
        return false;
    }

}
