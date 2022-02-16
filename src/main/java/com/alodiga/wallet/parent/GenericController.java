/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.parent;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

/**
 *
 * @author usuario
 */
public class GenericController implements Serializable {

    public static String getStandarMessage(String key) {
        String bundleQualifierName = "com.alodiga.wallet.messages.message";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Locale locale = facesContext.getViewRoot().getLocale();
        ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleQualifierName, locale);
        return resourceBundle.getString(key);
    }

}
