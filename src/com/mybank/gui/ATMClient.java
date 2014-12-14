package com.mybank.gui;

import com.mybank.data.*;
import java.io.*;
import java.awt.*;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
    JFrame f;
    JPanel left, leftTop, leftMid, leftBot, center;
    JTextArea outputTextArea;
    JScrollPane outputScrollPane;
    JButton displayAccBalance, makeDeposit, makeWithdrawal;
    JTextField dataEntry, message;
    JButton[] keyPadBtn;
    GridBagConstraints c = new GridBagConstraints();

    private void launchFrame() {
        f = new JFrame("First Java Bank ATM");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        left = new JPanel();
        left.setLayout(new GridBagLayout());

        leftTop = new JPanel(new GridLayout(3, 1));
        displayAccBalance = new JButton("Display account balance");
        makeDeposit = new JButton("Make a deposit");
        makeWithdrawal = new JButton("Make a withdrawal");
        leftTop.add(displayAccBalance);
        leftTop.add(makeDeposit);
        leftTop.add(makeWithdrawal);

        dataEntry = new JTextField(10);
        dataEntry.setEditable(false);

        leftBot = new JPanel(new GridLayout(4, 3)); // 
        keyPadBtn = new JButton[12];
        for (int i = 0; i < keyPadBtn.length; i++) {
            String name;
            switch (i) {
                case 9:
                    name = "0";
                    break;
                case 10:
                    name = "";
                    break;
                case 11:
                    name = "Enter";
                    break;
                default:
                    name = "" + (i + 1);
            }

            keyPadBtn[i] = new JButton(name);
            keyPadBtn[i].setPreferredSize(new Dimension(60, 30));
            keyPadBtn[i].setMargin(new Insets(0, 0, 0, 0));
            leftBot.add(keyPadBtn[i]);
        }

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        outputTextArea = new JTextArea(10, 75);
        outputTextArea.setEditable(false);
        outputScrollPane = new JScrollPane(outputTextArea);
        outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        message = new JTextField(75);
        center.add(outputScrollPane);
        center.add(message);

        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        left.add(leftTop, c);

        c.gridx = 0;
        c.gridy = 1;
        left.add(dataEntry, c);

        c.gridx = 0;
        c.gridy = 2;
        left.add(leftBot, c);

        f.add(left, BorderLayout.WEST);
        f.add(center, BorderLayout.CENTER);

        f.pack();
        f.setVisible(true);
    }
}
