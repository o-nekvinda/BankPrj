/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mybank.domain;

import java.util.ArrayList;
import java.util.List;

public class Bank {

    private static List<Customer> customers = new ArrayList<>();
    private static int numberOfCostumers;

    private Bank() {
    }

    public static void addCustomer(String f, String l) {
        customers.add(new Customer(f, l));
        numberOfCostumers++;
    }

    public static int getNumOfCustomers() {
        return numberOfCostumers;
    }

    // Chyba v UML "Using Collections to Represent Association"?
    // getCustomer() musi byt static, v UML neni.
    public static Customer getCustomer(int index) {
        return customers.get(index);
    }
}
