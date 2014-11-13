/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mybank.domain;

/**
 *
 * @author MSI
 */
public class Bank {

    private Customer[] customers = new Customer[999];
    private int numberOfCostumers;

    public Bank() {
    }

    public void addCustomer(String f, String l) {
        customers[numberOfCostumers] = new Customer(f, l);
        numberOfCostumers++;
    }

    public int getNumOfCustomers() {
        return numberOfCostumers;
    }

    public Customer getCustomer(int index) {
        return customers[index];
    }
}
