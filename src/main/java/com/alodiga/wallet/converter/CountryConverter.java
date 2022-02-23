package com.alodiga.wallet.converter;

import com.alodiga.wallet.common.model.Country;
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
@ManagedBean(name = "countryConverter")
@ViewScoped
public class CountryConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue == null || submittedValue.equals("")) {
            return "";
        }
        try {
            long idCountry = Long.parseLong(submittedValue);
            Country country = new Country();
            country.setId(idCountry);
            return country;
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
            if (value instanceof Country) {
                return Long.toString(((Country) value).getId());
            } else {
                return value.toString();
            }

        }
    }

}
