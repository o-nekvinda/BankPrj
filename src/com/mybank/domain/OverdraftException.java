package com.mybank.domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author onekv_000
 */
public class OverdraftException extends Exception {

    private double deficit;

    public OverdraftException(String msg, double deficit) {
        super(msg);
        this.deficit = deficit;
    }

    public double getDeficit() {
        return deficit;
    }

}
