/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.utils;

/**
 *
 * @author Jesús Gómez
 */
public class Utils {
    
    public String transformCardNumber(String cardNumber) {
        StringBuilder cadena = new StringBuilder(cardNumber);
        for (int i = 5; i < cadena.length(); i++) {
            if (i <= 11) {
                cadena.setCharAt(i, '*');
            }
        }
        return cadena.toString();
    }
    
}
