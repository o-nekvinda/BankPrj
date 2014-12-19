package com.mybank.gui;

import com.mybank.data.*;
import com.mybank.domain.Bank;
import com.mybank.domain.Customer;
import com.mybank.domain.OverdraftException;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private static JFrame frame;
    private static JPanel pnlLeft, pnlLeftTop, pnlKeys, pnlCenter;
    private static JTextField txtDataEntry, txtMessage;
    private static JScrollPane scrolPane;
    private static JTextArea txtOutput;
    private static JButton btnGetAccBalance, btnMakeDeposit, btnMakeWithdrawal;
    private static JButton[] btnKeyPad;
    private static final GridBagConstraints c = new GridBagConstraints(); // Pravidla pro usporadani component v GridBagLayout.

    private static Customer selectedCustomer;
    private static int selectedAccID;
    private static EATMState ATMState;

    /**
     * Nastaveni stavu bankomatu Vypsani zpravy pri zmene stavu
     */
    public static void setATMState(EATMState ATMState) {
        if (ATMState == ATMClient.ATMState) {
            return;
        }
        if (ATMState == EATMState.ENTER_AMOUNT) {
            txtOutput.append("Enter an amount.\n");
        } else if (ATMState == EATMState.ENTER_ACC_ID) {
            txtOutput.append("Enter account ID.\n");
        } else if (ATMState == EATMState.CHOOSE_ACTION) {
            //outputTextArea.append("Choose an action.\n"); 
        }
        ATMClient.ATMState = ATMState;
    }

    public static EATMState getATMState() {
        return ATMState;
    }

    public static Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public static int getSelectedAccID() {
        return selectedAccID;
    }

    /**
     * setEnabled pro 3 action tlacitka
     *
     * @param state
     */
    public static void setActionBtnsEnabled(boolean state) {
        btnGetAccBalance.setEnabled(state);
        btnMakeDeposit.setEnabled(state);
        btnMakeWithdrawal.setEnabled(state);
    }

    public static void setActionBtnEnabled(JButton btn) {
        setActionBtnsEnabled(true);
        btn.setEnabled(false);
    }

    public static void setSelectedCustomer(Customer selectedCustomer) {
        ATMClient.selectedCustomer = selectedCustomer;
        txtOutput.append("Welcome " + selectedCustomer.getFirstName() + " " + selectedCustomer.getSurName() + ".\n");
        txtDataEntry.setText("");
        setActionBtnsEnabled(true);
        setATMState(EATMState.CHOOSE_ACTION);
    }

    public static void setSelectedAccID(int selectedAccID) {
        ATMClient.selectedAccID = selectedAccID;
    }

    private void launchFrame() {
        frame = new JFrame("First Java Bank ATM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Left
        pnlLeft = new JPanel();
        pnlLeft.setLayout(new GridBagLayout());

        // Left top, 3 action buttons
        pnlLeftTop = new JPanel(new GridLayout(3, 1));
        btnGetAccBalance = new JButton("Display account balance");
        btnGetAccBalance.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < selectedCustomer.getNumOfAccounts(); i++) {
                    txtOutput.append("Your account (ID: " + i + ") balance is: " + selectedCustomer.getAccount(i).getBalance() + "\n");
                    setATMState(EATMState.CHOOSE_ACTION);
                    setActionBtnEnabled(btnGetAccBalance);
                }
            }
        });
        btnMakeDeposit = new JButton("Make a deposit");
        btnMakeDeposit.addActionListener(new ActionActionButton());
        btnMakeWithdrawal = new JButton("Make a withdrawal");
        btnMakeWithdrawal.addActionListener(new ActionActionButton());

        pnlLeftTop.add(btnGetAccBalance);
        pnlLeftTop.add(btnMakeDeposit);
        pnlLeftTop.add(btnMakeWithdrawal);

        // Data entry
        txtDataEntry = new JTextField(10);
        txtDataEntry.setEditable(false);

        // Key panel
        pnlKeys = new JPanel(new GridLayout(4, 3));
        btnKeyPad = new JButton[12];
        for (int i = 0; i < btnKeyPad.length; i++) {
            String name;
            btnKeyPad[i] = new JButton();

            if (i == 9) {
                name = "0";
            } else if (i == 10) {
                name = "";
            } else if (i == 11) {
                name = "ENTER";
            } else {
                name = "" + (i + 1);
            }
            btnKeyPad[i].setText(name);
            //btnKeyPad[i].setPreferredSize(new Dimension(60, 30));
            //btnKeyPad[i].setMargin(new Insets(0, 0, 0, 0)); // Nastaveni odsazeni popisu tlacitka od jeho hrany (jinak se tam nevejde "Enter")
            if (i == 11) {
                btnKeyPad[i].addActionListener(new ActionEnterPressed());
            } else {
                btnKeyPad[i].addActionListener(new ActionNumberPressed());
            }
            pnlKeys.add(btnKeyPad[i]);
        }

        pnlCenter = new JPanel();
//        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.setLayout(new BorderLayout());
        txtOutput = new JTextArea(10, 75 / 2);
        txtOutput.setEditable(false);
        // TextArea automatic scroll http://tips4java.wordpress.com/2008/10/22/text-area-scrolling/
        DefaultCaret caret = (DefaultCaret) txtOutput.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        scrolPane = new JScrollPane(txtOutput);
        scrolPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        txtMessage = new JTextField(75 / 2);
        pnlCenter.add(scrolPane, BorderLayout.CENTER);
        pnlCenter.add(txtMessage, BorderLayout.SOUTH);

        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        pnlLeft.add(pnlLeftTop, c);

        c.gridy = 1;
        pnlLeft.add(txtDataEntry, c);

        c.gridy = 2;
        pnlLeft.add(pnlKeys, c);

        frame.add(pnlLeft, BorderLayout.WEST);
        frame.add(pnlCenter, BorderLayout.CENTER);

        startNewSession();
        frame.pack();
        frame.setVisible(true);
    }

    public static void startNewSession() {
        setActionBtnsEnabled(false);
        txtOutput.append("Enter your customer ID into the key pad and press the ENTER button.\n");
        txtDataEntry.setText("");
        setATMState(EATMState.CHOOSE_CUSTOMER);
    }

    private static class ActionActionButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedCustomer.getNumOfAccounts() > 1) {
                setATMState(EATMState.ENTER_ACC_ID);
            } else {
                setSelectedAccID(0);
                setATMState(EATMState.ENTER_AMOUNT);
            }
            setActionBtnEnabled((JButton) e.getSource());
        }
    }

    class ActionNumberPressed implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonName = e.getActionCommand();
            if (buttonName.equalsIgnoreCase("")) {
                txtDataEntry.setText("");
                return;
            }
            txtDataEntry.setText(txtDataEntry.getText() + buttonName);
        }
    }

    class ActionEnterPressed implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (txtDataEntry.getText().isEmpty()) {
                txtOutput.append("Invalid input: You must enter a number!\n");
                txtDataEntry.setText("");
                return;
            }

            int dataEntryInt = Integer.parseInt(txtDataEntry.getText());

            if (getATMState() == EATMState.CHOOSE_CUSTOMER) {

                if (dataEntryInt > Bank.getNumOfCustomers()) {
                    txtOutput.append("Customer ID " + dataEntryInt + " was not found!\n");
                } else {
                    setSelectedCustomer(Bank.getCustomer(dataEntryInt));
                }

            } else if (getATMState() == EATMState.ENTER_ACC_ID) {
                if (dataEntryInt > getSelectedCustomer().getNumOfAccounts() - 1) {
                    txtOutput.append("ACC ID " + dataEntryInt + " was not found! Max. ACC ID is " + (getSelectedCustomer().getNumOfAccounts() - 1) + "\n");
                } else {
                    setSelectedAccID(dataEntryInt);
                    txtOutput.append("Selected ACC ID: " + getSelectedAccID() + ". Current balance: " + getSelectedCustomer().getAccount(getSelectedAccID()).getBalance() + "\n");
                    setATMState(EATMState.ENTER_AMOUNT);
                }

            } else if (getATMState() == EATMState.ENTER_AMOUNT) {
                if (!btnMakeDeposit.isEnabled()) {
                    getSelectedCustomer().getAccount(getSelectedAccID()).deposit(dataEntryInt);
                    txtOutput.append("Your deposit of " + dataEntryInt + " was successful.\n");
                } else if (!btnMakeWithdrawal.isEnabled()) {
                    try {
                        getSelectedCustomer().getAccount(getSelectedAccID()).withdraw(dataEntryInt);
                        txtOutput.append("Your withdrawal of " + dataEntryInt + " was successful.\n");
                    } catch (OverdraftException ex) {
                        txtOutput.append("Your withdrawal of " + dataEntryInt + " was unsuccessful! Deficit: " + ex.getDeficit() + ". Repeat.\n");
                        txtDataEntry.setText("");
                        return;
                    }
                }
                txtOutput.append("Your new account balance is: " + getSelectedCustomer().getAccount(getSelectedAccID()).getBalance() + "\n\n");
                startNewSession();

            }
            txtDataEntry.setText("");

        }

    }
}
