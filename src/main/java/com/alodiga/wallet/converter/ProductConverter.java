/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.converter;

import com.alodiga.wallet.common.ejb.ProductEJB;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.genericEJB.EJBRequest;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import com.alodiga.wallet.common.utils.EjbConstants;
import com.alodiga.wallet.controllers.operationsCard.ManualRechargeRequestController;
import com.alodiga.wallet.common.model.Product;
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
 * @author jgomez
 */
@FacesConverter("productConverter")
public class ProductConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        Product selectedProduct = null;
        if (submittedValue == null || submittedValue.equals("")) {
            return "";
        }
        try { 
            ProductEJB  productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            EJBRequest request = new EJBRequest();
            request.setParam(Long.parseLong(submittedValue));
            selectedProduct = productEJB.loadProduct(request);
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(ProductConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ProductConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            ex.printStackTrace();
            Logger.getLogger(ProductConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return selectedProduct;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            if (value instanceof Product) {
                return Long.toString(((Product) value).getId());
            } else {
                return value.toString();
            }

        }
    }
   
}
