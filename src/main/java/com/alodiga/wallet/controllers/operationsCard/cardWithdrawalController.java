/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alodiga.wallet.controllers.operationsCard;

import com.alodiga.wallet.controllers.*;
import com.alodiga.cms.commons.ejb.UtilsEJB;
import com.alodiga.cms.commons.exception.EmptyListException;
import com.alodiga.cms.commons.exception.GeneralException;
import com.alodiga.cms.commons.exception.NullParameterException;
import com.cms.commons.genericEJB.EJBRequest;
import com.cms.commons.models.Country;
import com.cms.commons.models.Currency;
import com.cms.commons.util.EJBServiceLocator;
import com.cms.commons.util.EjbConstants;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
//import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author angel
 */
@ManagedBean(name = "RetirementController")
@ViewScoped
public class cardWithdrawalController {

    private List<Country> countryList = new ArrayList();
    private List<Country> countrynew = new ArrayList();
    private static UtilsEJB proxyUtilEJB;
    public EJBRequest request = new EJBRequest();
    private Country selectedCountry;
    private String countryName;
    private String bank;
    private Country country = null;
    private Map<String, String> bankdependency;
    private Map<String, String> countries;
    private Map<String, Map<String, String>> data = new HashMap<>();

    @PostConstruct
     public void init() {
        try {
//            
            proxyUtilEJB = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
//            
            try {                
                countryList = proxyUtilEJB.getCountries(request);

                for (Country country : countryList) {
                    try {
                        countrynew.add(country);
                    } catch (Exception e) {
                        e.printStackTrace();
}
                }
            } catch (EmptyListException ex) {
                Logger.getLogger(cardWithdrawalController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (GeneralException ex) {
                Logger.getLogger(cardWithdrawalController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullParameterException ex) {
                Logger.getLogger(cardWithdrawalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(cardWithdrawalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
  

//    public void submit() {
//
//        FacesContext context = FacesContext.getCurrentInstance();
//        if (countryName != null) {
//            try {
//               country = new Country();
//               country.setCode(code);
//               country.setCodeIso2(codeIso2);
//               country.setCodeIso3(codeIso3);
//               country.setName(countryName);
//               country.setCurrencyId(selectedCurrency);
//               country = proxyUtilEJB.saveCountry(country);
//
//               if (country != null) {
//                  context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Las respuestas del usuario se guardaron correctamente en la BD", null));
//               }else{
//                  context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Se presentó un problema al guardar los datos, por favor intente de nuevo", null));  
//               }
//            } catch (GeneralException ex) {
//                Logger.getLogger(AdminCountryController.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (NullParameterException ex) {
//                Logger.getLogger(AdminCountryController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } 
//
//    }


     public void onCountryChange() {
        if (country != null && !"".equals(country)) {
           bankdependency = data.get(country);
        }
        else {
            bankdependency = new HashMap<>();
        }
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

  
    public static UtilsEJB getProxyUtilEJB() {
        return proxyUtilEJB;
    }

    public static void setProxyUtilEJB(UtilsEJB proxyUtilEJB) {
        cardWithdrawalController.proxyUtilEJB = proxyUtilEJB;
    }

    public EJBRequest getRequest() {
        return request;
    }

    public void setRequest(EJBRequest request) {
        this.request = request;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

   
    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public Map<String, String> getBankdependency() {
        return bankdependency;
    }

    public void setBankdependency(Map<String, String> bankdependency) {
        this.bankdependency = bankdependency;
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public Map<String, String> getCountries() {
        return countries;
    }

    public void setCountries(Map<String, String> countries) {
        this.countries = countries;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }
   
    
    
}

