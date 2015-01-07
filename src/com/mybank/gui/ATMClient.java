package com.mybank.gui;

import com.mybank.data.*;
import com.mybank.domain.*;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
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
    private JFrame frame;
    private JPanel pnlLeft, pnlActionBtns, pnlKeys, pnlCenter;
    private JTextField txtDataEntry, txtMessage;
    private JScrollPane scrollPane;
    private JTextArea txtOutput;
    private JButton btnGetAccBalance, btnMakeDeposit, btnMakeWithdrawal;
    private JButton[] btnKeyPad;

    /**
     * Vybrany customer
     */
    private Customer selectedCustomer;
    /**
     * Vybrany account customera
     */
    private int selectedAccID;
    /**
     * Soucasny stav ATM
     */
    private EATMState ATMState;

    /**
     * Zmena stavu ATM.
     *
     * @param ATMState
     */
    private void setAtmState(EATMState ATMState) {
        if (ATMState == this.ATMState) {
            return;
        }
        switch (ATMState) {
            default:
            case ENTER_AMOUNT:
                setKeyPadBtnsEnabled(true);
                txtOutput.append("Enter an amount.\n");
                break;
            case ENTER_ACC_ID:
                setKeyPadBtnsEnabled(true);
                txtOutput.append("Enter an account ID.\n");
                break;
            case CHOOSE_ACTION:
                // txtOutput.append("Choose an action.\n");
                setKeyPadBtnsEnabled(false);
                break;
            case CHOOSE_CUSTOMER:
                startNewSession();
                break;

        }
        this.ATMState = ATMState;
//        frame.setTitle("First Java Bank ATM - " + ATMState);
    }

    /**
     * Vytvoreni a spusteni ATM GUI.
     */
    public void launchFrame() {
        frame = new JFrame("First Java Bank ATM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Left JPanel
        pnlLeft = new JPanel();
        pnlLeft.setLayout(new GridBagLayout());

        // Left top, 3 action buttons
        pnlActionBtns = new JPanel(new GridLayout(3, 1));
        btnGetAccBalance = new JButton("Display account balance");
        btnGetAccBalance.addActionListener(new ActionListener() {
            // Vypis informaci o uctech customera
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedCustomer.getNumOfAccounts() == 1) {
                    // Output: "Your CheckingAccount balance is: 200.0"
                    txtOutput.append("Your " + selectedCustomer.getAccount(0).getClass().getSimpleName()
                            + " balance is: " + selectedCustomer.getAccount(0).getBalance() + "\n");
                } else { // Multiple accounts
                    for (int i = 0; i < selectedCustomer.getNumOfAccounts(); i++) {
                        Account acc = selectedCustomer.getAccount(i);
                        //txtOutput.append("Your account (ID: " + i + ") balance is: " + acc.getBalance() + "\n");

                        /* Output:
                         "Account ID: 0, Balance: 500.0, Type: SavingsAccount"
                         "Account ID: 1, Balance: 200.0, Type: CheckingAccount"
                         */
                        txtOutput.append("Account ID: " + i);
                        txtOutput.append(", Balance: " + acc.getBalance());
                        txtOutput.append(", Type: " + acc.getClass().getSimpleName());
                        txtOutput.append("\n");
                    }
                }
                setAtmState(EATMState.CHOOSE_ACTION);
                setActionBtnEnabled(btnGetAccBalance);
            }
        });
        btnMakeDeposit = new JButton("Make a deposit");
        btnMakeWithdrawal = new JButton("Make a withdrawal");
        ActionActionButton a = new ActionActionButton();
        btnMakeDeposit.addActionListener(a);
        btnMakeWithdrawal.addActionListener(a);

        pnlActionBtns.add(btnGetAccBalance);
        pnlActionBtns.add(btnMakeDeposit);
        pnlActionBtns.add(btnMakeWithdrawal);

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
                // 10. tlacitko je "0"
                name = "0";
            } else if (i == 10) {
                // 11. tlacitko je bez jmena
                name = "";
            } else if (i == 11) {
                // Posledni, 12. tlacitko je "ENTER"
                name = "ENTER";
            } else {
                // Tlacitka 1-9
                name = "" + (i + 1);
            }
            btnKeyPad[i].setText(name);
            if (i == 11) { // ENTER
                btnKeyPad[i].addActionListener(new ActionEnterPressed());
            } else { // Numbers and a button with an empty name 
                btnKeyPad[i].addActionListener(new ActionNumberPressed());
            }
            pnlKeys.add(btnKeyPad[i]);
        }

        pnlCenter = new JPanel();
        pnlCenter.setLayout(new BorderLayout());
        txtOutput = new JTextArea(10, 75 / 2);
        txtOutput.setEditable(false);

        //Automaticke scrollovani txtOutput http://tips4java.wordpress.com/2008/10/22/text-area-scrolling/
        DefaultCaret caret = (DefaultCaret) txtOutput.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        scrollPane = new JScrollPane(txtOutput);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        txtMessage = new JTextField(75 / 2);
        pnlCenter.add(scrollPane, BorderLayout.CENTER);
        pnlCenter.add(txtMessage, BorderLayout.SOUTH);

        GridBagConstraints c = new GridBagConstraints(); // Pravidla pro usporadani component v GridBagLayout.
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        pnlLeft.add(pnlActionBtns, c);

        c.gridy = 1;
        pnlLeft.add(txtDataEntry, c);

        c.gridy = 2;
        pnlLeft.add(pnlKeys, c);

        frame.add(pnlLeft, BorderLayout.WEST);
        frame.add(pnlCenter, BorderLayout.CENTER);

        setAtmState(EATMState.CHOOSE_CUSTOMER);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * Zapne/vypne tlacitka na key padu
     *
     * @param state
     */
    private void setKeyPadBtnsEnabled(boolean state) {
        if (btnKeyPad[0].isEnabled() ^ state) { // Pokud se stav tlacitek a param lisi.
            for (JButton btnKeyPad1 : btnKeyPad) {
                btnKeyPad1.setEnabled(state);
            }
        }
    }

    /**
     * Spusteni nove session ATM (novy customer). Probehne pri nastaveni stavu
     * ATM na CHOOSE_CUSTOMER.
     */
    private void startNewSession() {
        //txtOutput.setText("");
        txtOutput.append("Enter your customer ID into the key pad and press the ENTER button.\n");
        setActionBtnsEnabled(false);
        clearTxtDataEntry();
    }

    private EATMState getATMState() {
        return ATMState;
    }

    private Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    private int getSelectedAccID() {
        return selectedAccID;
    }

    /**
     * Zapne/vypne 3 action tlacitka (stav uctu a vyber/vlozeni penez)
     *
     * @param state - true to enable and false to disable action buttons
     */
    private void setActionBtnsEnabled(boolean state) {
        btnGetAccBalance.setEnabled(state);
        btnMakeDeposit.setEnabled(state);
        btnMakeWithdrawal.setEnabled(state);
    }

    /**
     * Vypnuti vsech tlacitek a nasledne zapnuti konkretniho(param) tlacitka.
     * Slouzi k odliseni volby, ktera je vybrana.
     */
    private void setActionBtnEnabled(JButton btn) {
        setActionBtnsEnabled(true);
        btn.setEnabled(false);
    }

    /**
     * Ulozeni vybraneho customera pro provadeni operaci s jeho uctem
     *
     * @param selectedCustomer - Vybrany customer
     */
    private void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
        txtOutput.append("Welcome " + selectedCustomer.getFirstName() + " " + selectedCustomer.getSurName() + ".\n");
        clearTxtDataEntry();
        setActionBtnsEnabled(true);
        setAtmState(EATMState.CHOOSE_ACTION);
    }

    /**
     * Smaze text z txtDataEntry.
     */
    private void clearTxtDataEntry() {
        txtDataEntry.setText("");
    }

    /**
     * Ulozeni vybraneho uctu customera v pripade
     *
     * @param selectedAccID - poradi vybraneho ACC. Pokud ma customer 3 ucty,
     * tak zadane ID ri vyberu uctu je 0, 1 nebo 2.
     */
    private void setSelectedAccID(int selectedAccID) {
        this.selectedAccID = selectedAccID;
    }

    /**
     * Meni stav ATM, pri vyberu vybrani/vlozeni penez, podle poctu uctu
     * zakaznika
     */
    private class ActionActionButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedCustomer.getNumOfAccounts() > 1) {
                setAtmState(EATMState.ENTER_ACC_ID);
            } else {
                setSelectedAccID(0);
                setAtmState(EATMState.ENTER_AMOUNT);
            }
            setActionBtnEnabled((JButton) e.getSource());

        }
    }

    /**
     * Stistknuti tlacitka na NUMPADu pripise jeho hodnotu do txtDataEntry
     */
    class ActionNumberPressed implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonName = e.getActionCommand(); // Ziskani "jmena tlacitka" (1-9 a prazdne jmeno "" u 10. tlacitka)
            if (buttonName.equalsIgnoreCase("")) { // Tlacitko bez jmena slouzi k vymazani textu
                clearTxtDataEntry();
                return;
            }

//            if (txtDataEntry.getText().length() < 10) {
            txtDataEntry.setText(txtDataEntry.getText() + buttonName); // Do txtDataEntry se pripise hodnota stistknuteho tlacitka
//            }
        }
    }

    /*
     * Akce pri stisknuti ENTER
     */
    class ActionEnterPressed implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            double dataEntryInt;
            try {
                dataEntryInt = Double.parseDouble(txtDataEntry.getText());
            } catch (Exception exception) {
                txtOutput.append("Invalid input: You must enter a number!\n");
                return;
            }

            if (getATMState() == EATMState.CHOOSE_CUSTOMER) {
                chooseCustomer((int) dataEntryInt);
            } else if (getATMState() == EATMState.ENTER_ACC_ID) {
                enterAccId((int) dataEntryInt);
            } else if (getATMState() == EATMState.ENTER_AMOUNT) {
                enterAmount(dataEntryInt);
            }
            clearTxtDataEntry();
        }

        /**
         * Vlozeni/vyber penez z ATM
         *
         * @param dataEntryInt - amount
         * @return
         */
        private void enterAmount(double dataEntryInt) {

            // Vybrane tlacitko pro vyber/vlozeni penez maji setEnabled(false). To se vyuziva k rozpoznani jakou operaci chce customer provest.
            if (!btnMakeDeposit.isEnabled()) {
                // if btnMakeDeposit is disabled - make a deposit
                getSelectedCustomer().getAccount(getSelectedAccID()).deposit(dataEntryInt); // Vlozeni penez na ucet
                txtOutput.append("Your deposit of " + dataEntryInt + " was successful.\n");
            } else if (!btnMakeWithdrawal.isEnabled()) {
                // if btnMakeWithdrawal is disabled - make a withdrawal
                try {
                    getSelectedCustomer().getAccount(getSelectedAccID()).withdraw(dataEntryInt);
                    txtOutput.append("Your withdrawal of " + dataEntryInt + " was successful.\n");
                } catch (OverdraftException ex) { //  Neuspesny vyber penez
                    txtOutput.append("!!!  Your withdrawal of " + dataEntryInt + " was unsuccessful! Deficit: " + ex.getDeficit() + ".   !!!\n\n");
                    clearTxtDataEntry();
                    // Nastaveni pocatecniho stavu ATM - vyber customera.
                    setAtmState(EATMState.CHOOSE_CUSTOMER);
                    return;
                }
            }
            // Nastane pouze po uspesnem provedeni vyberu/vlozeni penez
            txtOutput.append("Your new account balance is: " + getSelectedCustomer().getAccount(getSelectedAccID()).getBalance() + "\n\n");
            // Nastaveni pocatecniho stavu ATM - vyber customera.
            setAtmState(EATMState.CHOOSE_CUSTOMER);
        }

        /**
         * Zadani ID uctu konkretniho customera pri vyberu/vlozeni hotovosti na
         * ucet
         *
         * @param dataEntryInt - ACC ID urciteho cusmera. Pokud ma 2 ucty,
         * jejich ID je 0 a 1.
         */
        private void enterAccId(int dataEntryInt) {
            if (dataEntryInt > getSelectedCustomer().getNumOfAccounts() - 1) { // Neexistujici ucet
                txtOutput.append("ACC ID " + dataEntryInt + " was not found! Max. ACC ID is " + (getSelectedCustomer().getNumOfAccounts() - 1) + "\n");
            } else {
                setSelectedAccID(dataEntryInt);
                txtOutput.append("Selected ACC ID: " + getSelectedAccID() + ". Current balance: " + getSelectedCustomer().getAccount(getSelectedAccID()).getBalance() + "\n");
                setAtmState(EATMState.ENTER_AMOUNT);
            }
        }

        /**
         * Vybrani customera podle je ho ID
         *
         * @param dataEntryInt - Customer ID
         */
        private void chooseCustomer(int dataEntryInt) {
            // Pocatecni stav, zadani ID Customera

            if (dataEntryInt >= Bank.getNumOfCustomers()) {
                txtOutput.append("Customer ID " + dataEntryInt + " was not found!\n");
            } else {
                setSelectedCustomer(Bank.getCustomer(dataEntryInt));
            }
        }
    }
}
