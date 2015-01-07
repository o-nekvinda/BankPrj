package com.mybank.gui;

/**
 * Enum moznych stavu ATM.
 *
 */
public enum EATMState {

    /**
     * Stav spusteni ATM - zadani ID customera.
     */
    CHOOSE_CUSTOMER,
    /**
     * Stav ve kterem se vyckava na vyber jednoho z action tlacitek.
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
