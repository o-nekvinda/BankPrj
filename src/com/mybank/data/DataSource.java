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
        int klientId = 0;
        Account acc = null;
        String accType;
        int pocetUctu;
        String lastName;
        String firstName;
        int pocetKlientu;

        Scanner sc = new Scanner(dataFile);
        pocetKlientu = sc.nextInt();

        while (klientId < pocetKlientu) {
            firstName = sc.next();
            lastName = sc.next();

            Bank.addCustomer(firstName, lastName);
            System.out.println("Vytvoren klient: " + firstName + " " + lastName);
            pocetUctu = sc.nextInt();

            for (int i = 0; i < pocetUctu - 0; i++) {
                accType = sc.next();

                String zustatek = sc.next();
                String sazba = sc.next();
                if (accType.equalsIgnoreCase("s")) {
                    acc = new SavingsAccount(Double.parseDouble(zustatek), Double.parseDouble(sazba));
                } else if (accType.equalsIgnoreCase("c")) {
                    acc = new CheckingAccount(Double.parseDouble(zustatek), Double.parseDouble(sazba));
                }

                if (acc != null) {
                    Bank.getCustomer(klientId).addAccount(acc);
                    System.out.println("Vytvoren ucet typu: " + accType + " se zustatkem: " + zustatek + "a 2param:" + sazba);
                }
            }
            klientId++;
        }

    }

}
