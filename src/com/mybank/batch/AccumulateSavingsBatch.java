/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mybank.batch;

import com.mybank.domain.*;

/***
 *
 * @author onekv_000
 */
public class AccumulateSavingsBatch {

    private Bank bank;

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void doBatch() {
        for (int i = 0; i < this.bank.getNumOfCustomers(); i++) {
            for (int j = 0; j < bank.getCustomer(i).getNumOfAccounts(); j++) {
                if (bank.getCustomer(i).getAccount(j) instanceof SavingsAccount) {
                    bank.getCustomer(i).getAccount(j).accumulateInterest();
                }
            }
        }
    }
}
