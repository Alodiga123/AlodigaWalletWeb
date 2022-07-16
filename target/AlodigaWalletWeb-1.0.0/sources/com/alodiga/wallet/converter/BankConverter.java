/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.converter;

import com.alodiga.wallet.common.ejb.UtilsEJB;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.genericEJB.EJBRequest;
import com.alodiga.wallet.common.model.Bank;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import com.alodiga.wallet.common.utils.EjbConstants;
import com.alodiga.wallet.controllers.operationsCard.AddAccountController;
import com.alodiga.wallet.ws.Maw_bank;
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

@FacesConverter("bankConverter")
public class BankConverter implements Converter {
    
    Bank bank = null;
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.equals("")) {
            return "";
        }
        try {
            UtilsEJB  utilsEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            EJBRequest request = new EJBRequest();
            request.setParam(Long.parseLong(submittedValue));
            bank = utilsEJB.loadBank(request);
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(BankConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(BankConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            ex.printStackTrace();
            Logger.getLogger(BankConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bank;
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
