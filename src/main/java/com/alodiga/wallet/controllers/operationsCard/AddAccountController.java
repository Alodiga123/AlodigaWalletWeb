/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alodiga.wallet.controllers.operationsCard;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.cms.ws.APIAuthorizerCardManagementSystemProxy;
import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.ejb.UtilsEJB;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.model.AccountTypeBank;
import com.alodiga.wallet.common.model.Bank;
import com.alodiga.wallet.common.model.Country;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.Product;
import com.alodiga.wallet.ws.ProductResponse;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import com.alodiga.wallet.common.utils.EjbConstants;
import com.alodiga.wallet.ws.AccountBank;
import com.alodiga.wallet.ws.BankListResponse;
import com.alodiga.wallet.ws.CreditCardListResponse;
import com.alodiga.wallet.ws.AccountTypeBankListResponse;
import com.alodiga.wallet.ws.Maw_bank;
import com.alodiga.wallet.ws.ProductListResponse;
import com.cms.commons.genericEJB.EJBRequest;
import com.ericsson.alodiga.ws.PreguntaIdioma;
import com.ericsson.alodiga.ws.Usuario;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "addAccountController")
@ViewScoped
public class AddAccountController {

    private List<Country> countryList = new ArrayList();
    private List<AccountTypeBank> accountTypeBankList = new ArrayList();
    private List<Maw_bank> bankList = new ArrayList();
    private Maw_bank[] bankList2;
    private Country selectedCountry;
    private Bank selectedBank;
    private AccountTypeBank selectedAccountTypeBank;
    private APIAlodigaWalletProxy apiAlodigaWalletProxy;
    private HttpSession session;
    private Usuario user;
    private ResourceBundle msg;  
    private String numberAccountBank;
    private static BusinessPortalEJB businessPortalEJBProxy;
    

    @PostConstruct
    public void init() {
        try {
            businessPortalEJBProxy = (BusinessPortalEJB) EJBServiceLocator.getInstance().get(EjbConstants.BUSINESS_PORTAL_EJB);            
            apiAlodigaWalletProxy = new APIAlodigaWalletProxy();
            msg = ResourceBundle.getBundle("com.alodiga.wallet.messages.message", Locale.forLanguageTag("es"));

            //Se obtiene el usuario de sesión
            session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            user = (Usuario) session.getAttribute("user");

            //Se obtiene la lista de países
            countryList = businessPortalEJBProxy.getCountries();
            

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(RechargeCardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public Maw_bank[] getBankList2() {
        return bankList2;
    }

    public void setBankList2(Maw_bank[] bankList) {
        this.bankList2 = bankList2;
    }

    public List<Maw_bank> getBankList() {
        return bankList;
    }

    public void setBankList(List<Maw_bank> bankList) {
        this.bankList = bankList;
    }

    public APIAlodigaWalletProxy getApiAlodigaWalletProxy() {
        return apiAlodigaWalletProxy;
    }

    public void setApiAlodigaWalletProxy(APIAlodigaWalletProxy apiAlodigaWalletProxy) {
        this.apiAlodigaWalletProxy = apiAlodigaWalletProxy;
    }

    public BusinessPortalEJB getBusinessPortalEJBProxy() {
        return businessPortalEJBProxy;
    }

    public void setBusinessPortalEJBProxy(BusinessPortalEJB businessPortalEJBProxy) {
        this.businessPortalEJBProxy = businessPortalEJBProxy;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
        try {
            if (selectedCountry != null) {
                BankListResponse bankListResponse = apiAlodigaWalletProxy.getBankByCountryApp(String.valueOf(selectedCountry.getId()));
                bankList2 = bankListResponse.getBanks();
                for (Maw_bank b : bankList2) {
                    bankList.add(b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bank getSelectedBank() {
        return selectedBank;
    }

    public void setSelectedBank(Bank selectedBank) {
        this.selectedBank = selectedBank;
    }

    public String getNumberAccountBank() {
        return numberAccountBank;
    }

    public void setNumberAccountBank(String numberAccountBank) {
        this.numberAccountBank = numberAccountBank;
    }

    public List<AccountTypeBank> getAccountTypeBankList() {
        return accountTypeBankList;
    }

    public void setAccountTypeBankList(List<AccountTypeBank> accountTypeBankList) {
        this.accountTypeBankList = accountTypeBankList;
    }

    public AccountTypeBank getSelectedAccountTypeBank() {
        return selectedAccountTypeBank;
    }

    public void setSelectedAccountTypeBank(AccountTypeBank selectedAccountTypeBank) {
        this.selectedAccountTypeBank = selectedAccountTypeBank;
    }

    public ResourceBundle getMsg() {
        return msg;
    }

    public void setMsg(ResourceBundle msg) {
        this.msg = msg;
    }

 
 
  public void submit() {
//
//        FacesContext context = FacesContext.getCurrentInstance();
//        if (numberAccountBank != null) {
//            try {
//               accountBank = new AccountBank();
//               accountBank.setAccountNumber(numberAccountBank);
//               accountBank.setAccountTypeBankId(accountTypeBankId);
//               
//               accountBank = proxyUtilEJB.saveBank(accountBank);
//
//               if (numberAccountBank != null) {
//                  context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Las respuestas del usuario se guardaron correctamente en la BD", null));
//               }else{
//                  context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Se presentó un problema al guardar los datos, por favor intente de nuevo", null));  
//               }
//            
//            }

   }
}
