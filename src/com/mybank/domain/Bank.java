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

    private static Customer[] customers = new Customer[999];
    private static int numberOfCostumers;

//    static {
//    
//    }
    public Bank() {
    }

    public static void addCustomer(String f, String l) {
//        System.out.println(f + " " + l);
        customers[numberOfCostumers] = new Customer(f, l);
        numberOfCostumers++;
    }

    public static int getNumOfCustomers() {
        return numberOfCostumers;
    }

    public static Customer getCustomer(int index) {
        return customers[index];
    }
}
