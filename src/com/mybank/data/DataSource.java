/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mybank.data;

import com.mybank.domain.Account;
import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.SavingsAccount;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author STUDENT-FES-EA03029
 */
public class DataSource {

    private File dataFile;

    public DataSource(String dataFilePath) {
        this.dataFile = new File(dataFilePath);
    }

    public void loadData() throws FileNotFoundException {

        int numberofCustomers;
        int numberOdAccounts;
        String accountType;
        int customerId = 0;
        Account acc = null;

        Scanner sc = new Scanner(dataFile);
        numberofCustomers = sc.nextInt();

        while (customerId < numberofCustomers) {

            Bank.addCustomer(sc.next(), sc.next());
            numberOdAccounts = sc.nextInt();

            for (int i = 0; i < numberOdAccounts - 0; i++) {
                accountType = sc.next();

                if (accountType.equalsIgnoreCase("s")) {

                    acc = new SavingsAccount(Double.parseDouble(sc.next()), Double.parseDouble(sc.next()));
                } else if (accountType.equalsIgnoreCase("c")) {
                    acc = new CheckingAccount(Double.parseDouble(sc.next()), Double.parseDouble(sc.next()));
                }

                if (acc != null) {
                    Bank.getCustomer(customerId).addAccount(acc);
                }
            }
            customerId++;
        }
    }
}
