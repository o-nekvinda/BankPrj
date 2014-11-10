package com.mybank.domain;

/**
 *
 * @author student
 */
public class Account {

    protected double balance;

    protected Account(double initBalance) {
        this.balance = initBalance;
    }

    public double getBalance() {
        return balance;
    }

    public boolean deposit(double amt) {
        if (amt > 0) {
            this.balance = this.balance + amt;
            return true;
        } else {
            return false;
        }
    }

    public boolean withdraw(double amt) {
        if (this.balance - amt > 0 && amt > 0) {
            this.balance = this.balance - amt;
            return true;
        } else {
            return false;
        }

    }

    public void accumulateInterest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
