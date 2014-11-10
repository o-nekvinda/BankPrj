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
public class SavingsAccount extends Account {

    private double interestRate;

    public SavingsAccount(double initBalance, double interestRate) {
        super(initBalance);
        this.interestRate = interestRate;
    }

    @Override
    public void accumulateInterest() {
        this.balance = this.balance + (this.interestRate * this.balance);
    }

}
