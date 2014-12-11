package com.mybank.gui;

import com.mybank.domain.*;
import com.mybank.data.*;
import java.io.*;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ATMClient {

    private static final String USAGE
            = "USAGE: java com.mybank.gui.ATMClient <dataFilePath>";

    public static void main(String[] args) {

        // Retrieve the dataFilePath command-line argument
        if (args.length != 1) {
            System.out.println(USAGE);
        } else {
            String dataFilePath = args[0];

            try {
                System.out.println("Reading data file: " + dataFilePath);
                // Create the data source and load the Bank data
                DataSource dataSource = new DataSource(dataFilePath);
                dataSource.loadData();

                // Run the ATM GUI
                ATMClient client = new ATMClient();
                client.launchFrame();

            } catch (IOException ioe) {
                System.out.println("Could not load the data file.");
                System.out.println(ioe.getMessage());
                ioe.printStackTrace(System.err);
            }
        }
    }

  // PLACE YOUR GUI CODE HERE
    private void launchFrame() {
        JFrame f = new JFrame("First Java Bank ATM");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        JPanel left = new JPanel(new GridLayout(5, 1));
        JButton 
        
        
        
        JTextArea textarea = new JTextArea("");
        JScrollPane scroll = new JScrollPane(textarea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        f.add(scroll, BorderLayout.CENTER);
        
        f.setSize(500, 300);
        f.setVisible(true);
    }
}
