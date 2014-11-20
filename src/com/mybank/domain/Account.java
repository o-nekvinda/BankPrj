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

    public void deposit(double amt)  {
        if (amt > 0) {
            this.balance = this.balance + amt;
        } else {
        }
    }

    public void withdraw(double amt) throws OverdraftException {
        if (this.balance - amt > 0 && amt > 0) {
            this.balance = this.balance - amt;
        } else {
            throw new OverdraftException("Account.java OverdraftException!", Math.abs(this.balance - amt));
        }

    }

    public void accumulateInterest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
