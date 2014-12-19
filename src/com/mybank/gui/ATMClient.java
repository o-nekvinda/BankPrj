package com.mybank.gui;

import com.mybank.data.*;
import com.mybank.domain.Bank;
import com.mybank.domain.Customer;
import com.mybank.domain.EATMStatus;
import com.mybank.domain.OverdraftException;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

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
    private static JFrame f;
    private static JPanel left, leftTop, keyPadPane, center;
    private static JTextArea outputTextArea;
    private static JScrollPane outputScrollPane;
    private static JButton displayAccBalance, makeDeposit, makeWithdrawal;
    private static JTextField dataEntry, message;
    private static JButton[] keyPadBtn;
    private static final GridBagConstraints c = new GridBagConstraints(); // Pravidla pro usporadani component v GridBagLayout.

    private static Customer selectedCustomer;
    private static int selectedAccID;
    private static EATMStatus statusATM;

    public static void setStatusATM(EATMStatus statusATM) {
        if (statusATM == ATMClient.statusATM) {
            return;
        }
        if (statusATM == EATMStatus.ENTER_AMOUNT) {
            outputTextArea.append("Enter an amount.\n");
        } else if (statusATM == EATMStatus.ENTER_ACC_ID) {
            outputTextArea.append("Enter account ID.\n");
        } else if (statusATM == EATMStatus.CHOOSE_ACTION) {
            //outputTextArea.append("Choose an action.\n"); 
        }
        ATMClient.statusATM = statusATM;
        System.out.println(statusATM);
    }

    public static EATMStatus getStatusATM() {
        return statusATM;
    }

    public static Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public static int getSelectedAccID() {
        return selectedAccID;
    }

    public static void setSelectedCustomer(Customer selectedCustomer) {
        ATMClient.selectedCustomer = selectedCustomer;
        outputTextArea.append("Welcome " + selectedCustomer.getFirstName() + " " + selectedCustomer.getSurName() + ".\n");
        displayAccBalance.setEnabled(true);
        makeDeposit.setEnabled(true);
        makeWithdrawal.setEnabled(true);
        dataEntry.setText("");
        setStatusATM(EATMStatus.CHOOSE_ACTION);

    }

    public static void setSelectedAccID(int selectedAccID) {
        ATMClient.selectedAccID = selectedAccID;
    }

    private void launchFrame() {
        f = new JFrame("First Java Bank ATM");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        left = new JPanel();
        left.setLayout(new GridBagLayout());

        leftTop = new JPanel(new GridLayout(3, 1)); // 3radky, 1sloupec
        displayAccBalance = new JButton("Display account balance");
        displayAccBalance.addActionListener(new accAction());
        makeDeposit = new JButton("Make a deposit");
        makeDeposit.addActionListener(new accAction());
        makeWithdrawal = new JButton("Make a withdrawal");
        makeWithdrawal.addActionListener(new accAction());

        leftTop.add(displayAccBalance);
        leftTop.add(makeDeposit);
        leftTop.add(makeWithdrawal);

        dataEntry = new JTextField(10);
        dataEntry.setEditable(false);

        keyPadPane = new JPanel(new GridLayout(4, 3)); // 
        keyPadBtn = new JButton[12];
        // Vytvoreni 12-ti tlacitek.
        for (int i = 0; i < keyPadBtn.length; i++) {
            String name;
            keyPadBtn[i] = new JButton();
            switch (i) {
                case 9: // 9 tlacitko ma popis "0".
                    name = "0";
                    break;
                case 10:
                    name = "";
                    break;
                case 11: // 9 tlacitko ma popis "Enter".
                    name = "Enter";
                    break;
                default:
                    name = "" + (i + 1); // Popis tlacitek 1-9
            }
            keyPadBtn[i].setName(name);
            keyPadBtn[i].setText(name);

            keyPadBtn[i].setPreferredSize(new Dimension(60, 30));
            keyPadBtn[i].setMargin(new Insets(0, 0, 0, 0)); // Nastaveni odsazeni popisu tlacitka od jeho hrany (jinak se tam nevejde "Enter")
            if (i == 11) {
                keyPadBtn[i].addActionListener(new KeyPanelEnterAction());
            } else {
                keyPadBtn[i].addActionListener(new KeyPanelAction());
            }
            keyPadPane.add(keyPadBtn[i]);

        }

        center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        outputTextArea = new JTextArea(10, 75);
        outputTextArea.setEditable(false);
        // TextArea automaticke scrollovani http://tips4java.wordpress.com/2008/10/22/text-area-scrolling/
        DefaultCaret caret = (DefaultCaret) outputTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        outputScrollPane = new JScrollPane(outputTextArea);
        outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        message = new JTextField(75);
        center.add(outputScrollPane);
        center.add(message);

        c.gridx = 0; // Pozice component
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL; // Roztahnuti horizontalne
        left.add(leftTop, c);

        c.gridy = 1; // Posunuti o jednu pozici dolu
        left.add(dataEntry, c);

        c.gridy = 2;
        left.add(keyPadPane, c);

        f.add(left, BorderLayout.WEST);
        f.add(center, BorderLayout.CENTER);
        startNewSession();
        f.pack();
        f.setVisible(true);
    }

    public static void startNewSession() {
        displayAccBalance.setEnabled(false);
        makeDeposit.setEnabled(false);
        makeWithdrawal.setEnabled(false);
        outputTextArea.append("Enter your customer ID into the key pad and press the ENTER button.\n");
        dataEntry.setText("");
//        selectedCustomer = null;
//        selectedAccID = 0;
        setStatusATM(EATMStatus.CHOOSE_CUSTOMER);
    }

    private static class accAction implements ActionListener {

        public accAction() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!displayAccBalance.isEnabled()) {
                displayAccBalance.setEnabled(true);
            }
            if (!makeDeposit.isEnabled()) {
                makeDeposit.setEnabled(true);
            }
            if (!makeWithdrawal.isEnabled()) {
                makeWithdrawal.setEnabled(true);
            }
            JButton btn = (JButton) e.getSource();
            btn.setEnabled(false);
            if (!displayAccBalance.isEnabled()) {
                for (int i = 0; i < selectedCustomer.getNumOfAccounts(); i++) {
                    outputTextArea.append("Your account (ID: " + i + ") balance is: " + selectedCustomer.getAccount(i).getBalance() + "\n");
                    setStatusATM(EATMStatus.CHOOSE_ACTION);
                }
            } else {
                if (selectedCustomer.getNumOfAccounts() > 1) {
                    setStatusATM(EATMStatus.ENTER_ACC_ID);
                } else {
                    setSelectedAccID(0);
                    setStatusATM(EATMStatus.ENTER_AMOUNT);
                }
            }
        }
    }

    class KeyPanelAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonName = e.getActionCommand();
            if (buttonName.equalsIgnoreCase("")) {
                dataEntry.setText("");
                return;
            }
            dataEntry.setText(dataEntry.getText() + buttonName);
        }

    }

    class KeyPanelEnterAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (dataEntry.getText().isEmpty()) {
                outputTextArea.append("Invalid input: You must enter a number!\n");
                dataEntry.setText("");
                return;
            }

            int dataEntryInt = Integer.parseInt(dataEntry.getText());

            if (getStatusATM() == EATMStatus.CHOOSE_CUSTOMER) {

                if (dataEntryInt > Bank.getNumOfCustomers()) {
                    outputTextArea.append("Customer ID " + dataEntryInt + " was not found!\n");
                } else {
                    setSelectedCustomer(Bank.getCustomer(dataEntryInt));
                }

            } else if (getStatusATM() == EATMStatus.ENTER_ACC_ID) {
                if (dataEntryInt > getSelectedCustomer().getNumOfAccounts() - 1) {
                    outputTextArea.append("ACC ID " + dataEntryInt + " was not found! Max. ACC ID is " + (getSelectedCustomer().getNumOfAccounts() - 1) + "\n");
                } else {
                    setSelectedAccID(dataEntryInt);
                    outputTextArea.append("Selected ACC ID: " + getSelectedAccID() + ". Current balance: " + getSelectedCustomer().getAccount(getSelectedAccID()).getBalance() + "\n");
                    setStatusATM(EATMStatus.ENTER_AMOUNT);
                }

            } else if (getStatusATM() == EATMStatus.ENTER_AMOUNT) {
                if (!makeDeposit.isEnabled()) {
                    getSelectedCustomer().getAccount(getSelectedAccID()).deposit(dataEntryInt);
                    outputTextArea.append("Your deposit of " + dataEntryInt + " was successful.\n");
                } else if (!makeWithdrawal.isEnabled()) {
                    try {
                        System.out.println(dataEntryInt);
                        getSelectedCustomer().getAccount(getSelectedAccID()).withdraw(dataEntryInt);
                        outputTextArea.append("Your withdrawal of " + dataEntryInt + " was successful.\n");
                    } catch (OverdraftException ex) {
                        outputTextArea.append("Your withdrawal of " + dataEntryInt + " was unsuccessful! Deficit: " + ex.getDeficit() + ". Repeat.\n");
                        dataEntry.setText("");
                        return;
                    }
                }
                outputTextArea.append("Your new account balance is: " + getSelectedCustomer().getAccount(getSelectedAccID()).getBalance() + "\n\n");
                startNewSession();

            }
            dataEntry.setText("");

        }

    }
}
