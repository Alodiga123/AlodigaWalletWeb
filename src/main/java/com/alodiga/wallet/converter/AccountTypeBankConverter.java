/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.converter;

import com.alodiga.wallet.common.model.AccountTypeBank;
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

@ManagedBean(name = "accountTypeBankConverter")
@ViewScoped
public class AccountTypeBankConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.equals("")) {
            return "";
        }
        try {
            int idAccountTypeBank = Integer.valueOf(submittedValue);
            AccountTypeBank accountTypeBank = new AccountTypeBank();
            accountTypeBank.setId(idAccountTypeBank);
            return accountTypeBank;
        } catch (NumberFormatException ex) {
            Logger.getLogger(AccountTypeBankConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            if (value instanceof AccountTypeBank) {
                return Long.toString(((AccountTypeBank) value).getId());
            } else {
                return value.toString();
            }

        }
    } 
    
}
