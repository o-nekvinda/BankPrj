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
        int numberofCustomers;// Pocet klientu v souboru (prvni cislo)
        int numberOdAccounts; // Pocet uctu klienta
        String accountType;
        int customerId = 0;
        Account acc = null;

        Scanner sc = new Scanner(dataFile);
        numberofCustomers = sc.nextInt();

        while (customerId < numberofCustomers) { // Opakuje pro stanovey pocet klientu

            Bank.addCustomer(sc.next(), sc.next()); // Nacteni druheho a tretho zaznamu v souboru (jmeno, prijmeni)
            numberOdAccounts = sc.nextInt(); // Nacteni dalsiho zaznamu (pocet uctu klienta)

            for (int i = 0; i < numberOdAccounts - 0; i++) { // Opakuje pro vsechny ucty klienta
                accountType = sc.next(); // Typ uctu S nebo C

                if (accountType.equalsIgnoreCase("s")) { // Pokud je accountType "S"
                    // Vytvoreni noveho uctu. 
                    // sc.nextDouble() nefunguje, proto se pouzije sc.next() a double se parsuje.
                    acc = new SavingsAccount(Double.parseDouble(sc.next()), Double.parseDouble(sc.next()));
                } else if (accountType.equalsIgnoreCase("c")) { // Pokud je accountType "C"
                    acc = new CheckingAccount(Double.parseDouble(sc.next()), Double.parseDouble(sc.next()));
                }

                if (acc != null) {
                    Bank.getCustomer(customerId).addAccount(acc);
                }
            }
            customerId++; // A jdeme na dalsiho klienta...
        }

    }

}
