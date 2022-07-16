/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.converter;

import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.ejb.UtilsEJB;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.genericEJB.EJBRequest;
import com.alodiga.wallet.common.model.AccountTypeBank;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import com.alodiga.wallet.common.utils.EjbConstants;
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

@FacesConverter("accountTypeBankConverter")
public class AccountTypeBankConverter implements Converter {
    AccountTypeBank accountTypeBank = null;
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.equals("")) {
            return "";
        }
        try {
           BusinessPortalEJB  businessPortalEJB = (BusinessPortalEJB) EJBServiceLocator.getInstance().get(EjbConstants.BUSINESS_PORTAL_EJB);
           int accountTypeBankId = Integer.parseInt(submittedValue);
           accountTypeBank = businessPortalEJB.loadAccountTypeBankById(accountTypeBankId); 
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(AccountTypeBankConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(AccountTypeBankConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            ex.printStackTrace();
            Logger.getLogger(AccountTypeBankConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return accountTypeBank;
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
    
}
