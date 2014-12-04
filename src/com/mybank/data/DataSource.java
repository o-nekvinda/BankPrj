/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mybank.data;

import com.mybank.domain.Account;
import com.mybank.domain.Bank;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author STUDENT-FES-EA03029
 */
public class DataSource {

    private File dataFile;

    public DataSource(String dataFilePath) {
        this.dataFile = new File(dataFilePath);
    }

    public void loadData() {
        int klientId = 1;
        Account acc;
        String accType;
        int pocetUctu;
        String lastName;
        String firstName;
        int pocetKlientu;

        try {
            Scanner sc = new Scanner(dataFile);
            pocetKlientu = sc.nextInt();

            firstName = sc.next();
            lastName = sc.next();

            Bank.addCustomer(firstName, lastName);

            pocetUctu = sc.nextInt();

            for (int i = 0; i < pocetUctu - 0; i++) {
                accType = sc.next();

                String zustatek = sc.next();
                String sazba = sc.next();
//                System.out.println(accType + " " + zustatek + " " + sazba);
                if (accType.equalsIgnoreCase("s")) {
//                    System.out.println("S");
                } else if (accType.equalsIgnoreCase("c")) {
//                    System.out.println("C");
                }
            }

//            acc = new Account
//            Bank.getCustomer(klientId).addAccount(null);
        } catch (FileNotFoundException ex) {
        }

    }

}
