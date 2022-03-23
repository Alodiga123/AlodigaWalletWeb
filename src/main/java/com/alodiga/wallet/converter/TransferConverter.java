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
import com.alodiga.wallet.controllers.transferWallet.TransferWalletController;
import com.alodiga.wallet.ws.Maw_bank;
import com.ericsson.alodiga.ws.RespuestaListadoProducto;
import com.ericsson.alodiga.ws.Usuario;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jose
 */


@ManagedBean(name = "transferConverter")
@ViewScoped
public class TransferConverter implements Converter {
    
    @ManagedProperty(value = "#{transferWalletController}")
    private TransferWalletController transferWalletController;
    
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.equals("")) {
            return "";
        }
        try {            
            return transferWalletController.getListadoProductos(Long.parseLong(submittedValue));
        } catch (NumberFormatException ex) {
            Logger.getLogger(TransferConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            try {
               if (value instanceof RespuestaListadoProducto) {
                return Long.toString(((RespuestaListadoProducto) value).getId());
            } else {
                return value.toString();
            } 
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("vslue " + ((RespuestaListadoProducto) value).getId());
                return "";
            }
            

        }
    }



    public void setTransferWalletController(TransferWalletController transferWalletController) {
        this.transferWalletController = transferWalletController;
    }
    
    
    
}
