package com.mybank.gui;

/**
 * Enum moznych stavu ATM.
 *
 */
public enum EATMState {

    /**
     * Spusteni ATM - zadani ID customera.
     */
    CHOOSE_CUSTOMER,
    /**
     * Vyber z action tlacitek.
     */
    CHOOSE_ACTION,
    /**
     * Stav nastane pokud ma customer vice uctu a snazi se vybrat/vlozit nejakou
     * castku.
     */
    ENTER_ACC_ID,
    /**
     * Stav nastane pri vyberu/vlozeni hotovosti na ucet
     */
    ENTER_AMOUNT;

}
