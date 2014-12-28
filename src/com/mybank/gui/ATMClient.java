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
    private static final GridBagConstraints c = new GridBagConstraints(); // Pravidla pro usporadani component v GridBagLayout.
    private JFrame frame;
    private JPanel pnlLeft, pnlLeftTop, pnlKeys, pnlCenter;
    private JTextField txtDataEntry, txtMessage;
    private JScrollPane scrolPane;
    private JTextArea txtOutput;
    private JButton btnGetAccBalance, btnMakeDeposit, btnMakeWithdrawal;
    private JButton[] btnKeyPad;
    
    private Customer selectedCustomer;
    private int selectedAccID;
    private EATMState ATMState;

    /**
     * Nastaveni stavu bankomatu Vypsani zpravy pri zmene stavu
     *
     * @param ATMState
     */
    public void setATMState(EATMState ATMState) {
        if (ATMState == this.ATMState) {
            return;
        }
        if (ATMState == EATMState.ENTER_AMOUNT) {
            txtOutput.append("Enter an amount.\n");
        } else if (ATMState == EATMState.ENTER_ACC_ID) {
            txtOutput.append("Enter account ID.\n");
        } else if (ATMState == EATMState.CHOOSE_ACTION) {
            //outputTextArea.append("Choose an action.\n"); 
        }
        this.ATMState = ATMState;
    }
    
    public EATMState getATMState() {
        return ATMState;
    }
    
    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }
    
    public int getSelectedAccID() {
        return selectedAccID;
    }

    /**
     * setEnabled pro 3 action tlacitka
     *
     * @param state
     */
    public void setActionBtnsEnabled(boolean state) {
        btnGetAccBalance.setEnabled(state);
        btnMakeDeposit.setEnabled(state);
        btnMakeWithdrawal.setEnabled(state);
    }
    
    public void setActionBtnEnabled(JButton btn) {
        setActionBtnsEnabled(true);
        btn.setEnabled(false);
    }

    /**
     * Ulozeni vybraneho customera pro provadeni operaci s uctem
     *
     * @param selectedCustomer - Vybrany customer
     */
    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
        txtOutput.append("Welcome " + selectedCustomer.getFirstName() + " " + selectedCustomer.getSurName() + ".\n");
        txtDataEntry.setText("");
        setActionBtnsEnabled(true);
        setATMState(EATMState.CHOOSE_ACTION);
    }

    /**
     * Ulozeni vybraneho uctu customera v pripade
     *
     * @param selectedAccID - poradi vybraneho ACC. Pokud ma customer 3 ucty,
     * tak zadane ID ri vyberu uctu je 0, 1 nebo 2.
     */
    public void setSelectedAccID(int selectedAccID) {
        this.selectedAccID = selectedAccID;
    }
    
    private void launchFrame() {
        frame = new JFrame("First Java Bank ATM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

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
        pnlCenter.setLayout(new BorderLayout());
        txtOutput = new JTextArea(10, 75 / 2);
        txtOutput.setEditable(false);

        //Automaticke scrollovani txtOutput http://tips4java.wordpress.com/2008/10/22/text-area-scrolling/
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

    /**
     * Uvede ATM do puvodniho stavu - Vyber ID customera
     */
    public void startNewSession() {
        setActionBtnsEnabled(false);
        txtOutput.append("Enter your customer ID into the key pad and press the ENTER button.\n");
        txtDataEntry.setText("");
        setATMState(EATMState.CHOOSE_CUSTOMER);
    }

    /**
     * Class pro vytvoreni akce pokud se stiskne nektere z ACTION tlacitek().
     * Prakticky jen zmeni stav ATM a o hlavni funkcionalitu se stara
     */
    private class ActionActionButton implements ActionListener {
        
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

    /**
     * Stistknuti tlacitka na NUMPADu pripise jeho hodnotu do txtDataEntry
     */
    class ActionNumberPressed implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonName = e.getActionCommand(); // Ziskani "jmena tlacitka" (1-9 a prazdne jmeno "" u 10. tlacitka)
            if (buttonName.equalsIgnoreCase("")) { // Tlacitko bez jmena slouzi k vymazani textu
                txtDataEntry.setText("");
                return;
            }
            txtDataEntry.setText(txtDataEntry.getText() + buttonName); // Do txtDataEntry se pripise hodnota stistknuteho tlacitka
        }
    }

    /*
     * Akce pri stisknuti ENTER
     */
    class ActionEnterPressed implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            if (txtDataEntry.getText().isEmpty()) {
                txtOutput.append("Invalid input: You must enter a number!\n");
                txtDataEntry.setText("");
                return;
            }
            
            int dataEntryInt = Integer.parseInt(txtDataEntry.getText());
            
            if (getATMState() == EATMState.CHOOSE_CUSTOMER) {// Pocatecni stav, zadani ID Customera

                if (dataEntryInt > Bank.getNumOfCustomers()) {
                    txtOutput.append("Customer ID " + dataEntryInt + " was not found!\n");
                } else {
                    setSelectedCustomer(Bank.getCustomer(dataEntryInt));
                }
                
            } else if (getATMState() == EATMState.ENTER_ACC_ID) {
                /* Stav EATMState.ENTER_ACC_ID nastane pokud ma customer vice uctu.
                 * Vyzaduje se zadani ID uctu konkretniho customera pri vyberu/vlozeni hotovosti na ucet
                 */
                
                if (dataEntryInt > getSelectedCustomer().getNumOfAccounts() - 1) { // Neexistujici ucet
                    txtOutput.append("ACC ID " + dataEntryInt + " was not found! Max. ACC ID is " + (getSelectedCustomer().getNumOfAccounts() - 1) + "\n");
                } else {
                    setSelectedAccID(dataEntryInt);
                    txtOutput.append("Selected ACC ID: " + getSelectedAccID() + ". Current balance: " + getSelectedCustomer().getAccount(getSelectedAccID()).getBalance() + "\n");
                    setATMState(EATMState.ENTER_AMOUNT);
                }
                
            } else if (getATMState() == EATMState.ENTER_AMOUNT) { //  EATMState.ENTER_AMOUNT nastane pri vyberu/vlozeni hotovosti na ucet
                // Vybrane tlacitko pro vyber/vlozeni penez maji setEnabled(false). To vyuzivam k rozpoznani jakou operaci chce customer provest.
                if (!btnMakeDeposit.isEnabled()) { // if btnMakeDeposit is disabled
                    getSelectedCustomer().getAccount(getSelectedAccID()).deposit(dataEntryInt); // Vlozeni penez na ucet
                    txtOutput.append("Your deposit of " + dataEntryInt + " was successful.\n");
                } else if (!btnMakeWithdrawal.isEnabled()) {
                    try { // Testovani uspesne provedeneho vyberu penez
                        getSelectedCustomer().getAccount(getSelectedAccID()).withdraw(dataEntryInt);
                        txtOutput.append("Your withdrawal of " + dataEntryInt + " was successful.\n");
                    } catch (OverdraftException ex) {
                        txtOutput.append("Your withdrawal of " + dataEntryInt + " was unsuccessful! Deficit: " + ex.getDeficit() + ". Repeat.\n");
                        txtDataEntry.setText("");
                        return;
                    }
                }
                // Nastane pouze po uspesnem provednei vyberu/vlozeni penez
                txtOutput.append("Your new account balance is: " + getSelectedCustomer().getAccount(getSelectedAccID()).getBalance() + "\n\n");
                startNewSession(); // Uvede program zpet do puvodniho stavu - zadani ID customera.

            }
            txtDataEntry.setText("");
            
        }
        
    }
}
