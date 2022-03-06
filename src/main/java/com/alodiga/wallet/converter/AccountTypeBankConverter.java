/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.converter;

import com.alodiga.wallet.common.model.AccountTypeBank;
import com.alodiga.wallet.controllers.operationsCard.AddAccountController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
    
    @ManagedProperty(value = "#{addAccountController}")
    private AddAccountController addAccountController;
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.equals("")) {
            return "";
        }
        try {
            return addAccountController.getAccountTypeBank(Integer.parseInt(submittedValue));
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
                return Integer.toString(((AccountTypeBank) value).getId());
            } else {
                return value.toString();
            }

        }
    } 

    public AddAccountController getAddAccountController() {
        return addAccountController;
    }

    public void setAddAccountController(AddAccountController addAccountController) {
        this.addAccountController = addAccountController;
    }
    
}
