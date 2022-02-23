/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.converter;

import com.alodiga.wallet.common.model.Bank;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author jose
 */
//@FacesConverter("bankConverter")
@ManagedBean(name = "bankConverter")
@ViewScoped
public class BankConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.equals("")) {
            return "";
        }
        try {
            long idBank = Long.parseLong(submittedValue);
            Bank bank = new Bank();
            bank.setId(idBank);
            return bank;
        } catch (NumberFormatException ex) {
            Logger.getLogger(CountryConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            if (value instanceof Bank) {
                return Long.toString(((Bank) value).getId());
            } else {
                return value.toString();
            }

        }
    }
    
}
