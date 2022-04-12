package com.alodiga.wallet.controllers.operationsCard;

import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.cms.ws.APIAuthorizerCardManagementSystemProxy;
import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.ejb.UtilsEJB;
import com.alodiga.wallet.common.enumeraciones.StatusAccountBankE;
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
import com.alodiga.wallet.common.model.AccountTypeBank;
import com.alodiga.wallet.ws.BankListResponse;
import com.alodiga.wallet.ws.CreditCardListResponse;
import com.alodiga.wallet.ws.AccountTypeBankListResponse;
import com.alodiga.wallet.ws.Maw_bank;
import com.alodiga.wallet.ws.ProductListResponse;
import com.alodiga.wallet.common.genericEJB.EJBRequest;
import com.alodiga.wallet.common.model.AccountBank;
import com.alodiga.wallet.common.model.StatusAccountBank;
import com.ericsson.alodiga.ws.PreguntaIdioma;
import com.ericsson.alodiga.ws.Usuario;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    private List<Bank> bankList = new ArrayList();
    private Country selectedCountry;
    private Bank selectedBank;
    private AccountTypeBank selectedAccountTypeBank;
    private APIAlodigaWalletProxy apiAlodigaWalletProxy;
    private HttpSession session;
    private Usuario user;
    private ResourceBundle msg;  
    private String numberAccountBank;
    static BusinessPortalEJB businessPortalEJBProxy;
    private AccountBank accountBank = null;
    private static UtilsEJB utilsEJBProxy;
    
    

    @PostConstruct
    public void init() {
        try {
            businessPortalEJBProxy = (BusinessPortalEJB) EJBServiceLocator.getInstance().get(EjbConstants.BUSINESS_PORTAL_EJB);            
            utilsEJBProxy = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            apiAlodigaWalletProxy = new APIAlodigaWalletProxy();
            msg = ResourceBundle.getBundle("com.alodiga.wallet.messages.message", Locale.forLanguageTag("es"));

            //Se obtiene el usuario de sesión
            session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            user = (Usuario) session.getAttribute("user");

            //Se obtiene la lista de países
            countryList = businessPortalEJBProxy.getCountries();

            //Se obtiene la lista de tipos de cuentas bancarias
            EJBRequest request = new EJBRequest(); 
            accountTypeBankList = businessPortalEJBProxy.getAccountTypeBanks(request);

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

    public List<AccountTypeBank> getAccountTypeBankList() {
        return accountTypeBankList;
    }

    public void setAccountTypeBankList(List<AccountTypeBank> accountTypeBankList) {
        this.accountTypeBankList = accountTypeBankList;
    }

    public List<Bank> getBankList() {
        return bankList;
    }

    public void setBankList(List<Bank> bankList) {
        this.bankList = bankList;
    }

    public Bank getSelectedBank() {
        return selectedBank;
    }

    public void setSelectedBank(Bank selectedBank) {
        this.selectedBank = selectedBank;
    }

    public AccountTypeBank getSelectedAccountTypeBank() {
        return selectedAccountTypeBank;
    }

    public void setSelectedAccountTypeBank(AccountTypeBank selectedAccountTypeBank) {
        this.selectedAccountTypeBank = selectedAccountTypeBank;
    }

    public APIAlodigaWalletProxy getApiAlodigaWalletProxy() {
        return apiAlodigaWalletProxy;
    }

    public void setApiAlodigaWalletProxy(APIAlodigaWalletProxy apiAlodigaWalletProxy) {
        this.apiAlodigaWalletProxy = apiAlodigaWalletProxy;
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

    public ResourceBundle getMsg() {
        return msg;
    }

    public void setMsg(ResourceBundle msg) {
        this.msg = msg;
    }

    public String getNumberAccountBank() {
        return numberAccountBank;
    }

    public void setNumberAccountBank(String numberAccountBank) {
        this.numberAccountBank = numberAccountBank;
    }

    public static BusinessPortalEJB getBusinessPortalEJBProxy() {
        return businessPortalEJBProxy;
    }

    public static void setBusinessPortalEJBProxy(BusinessPortalEJB businessPortalEJBProxy) {
        AddAccountController.businessPortalEJBProxy = businessPortalEJBProxy;
    }

    public AccountBank getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(AccountBank accountBank) {
        this.accountBank = accountBank;
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }

     public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
        bankList.clear();
        try {
            if (selectedCountry != null) {
                bankList = utilsEJBProxy.getBankByCountry(Long.valueOf(selectedCountry.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submit() {
        if (numberAccountBank != null) {
            try {
               //Obtener el estatus ACTIVA de la cuenta bancaria
               StatusAccountBank statusAccountBankActiva = businessPortalEJBProxy.loadStatusAccountBankById(StatusAccountBankE.ACTIVA.getId());
               
               //Creando el objeto AccountBank
               AccountBank accountBank = new AccountBank();
               accountBank.setBankId(selectedBank);
               accountBank.setAccountNumber(numberAccountBank);
               accountBank.setAccountTypeBankId(selectedAccountTypeBank);
               accountBank.setStatusAccountBankId(statusAccountBankActiva);
               accountBank.setCreateDate(new Timestamp(new Date().getTime()));
               accountBank.setUnifiedRegistryId(user.getUsuarioID());
               //Guardar la cuenta bancaria en la BD
               accountBank = businessPortalEJBProxy.saveAccountBank(accountBank);

               if (accountBank != null) {
                 FacesContext.getCurrentInstance().addMessage("notification", new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg.getString("addBankAccountSaveSuccesfull")));
               }else{
                  FacesContext.getCurrentInstance().addMessage("notification", new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg.getString("addBankAccountSaveError"))); 
               }
            
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(RechargeCardController.class.getName()).log(Level.SEVERE, null, ex);      
            }

   }
  }
}