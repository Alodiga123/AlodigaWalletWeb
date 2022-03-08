package com.alodiga.wallet.controllers.operationsCard;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author angel
 */
import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.cms.ws.APIAuthorizerCardManagementSystemProxy;
import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.model.AccountBank;
import com.alodiga.wallet.common.model.AccountTypeBank;
import com.alodiga.wallet.common.model.Bank;
import com.alodiga.wallet.common.model.Country;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.Product;
import com.alodiga.wallet.ws.ProductResponse;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import com.alodiga.wallet.common.utils.EjbConstants;
import com.alodiga.wallet.ws.BankListResponse;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
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
import com.alodiga.wallet.common.model.AccountBank;
import com.alodiga.wallet.common.model.StatusAccountBank;
import com.alodiga.wallet.ws.AccountBankListResponse;
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
import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import org.primefaces.event.FlowEvent;
import org.primefaces.context.RequestContext;
import com.ericsson.alodiga.ws.RespuestaCodigoRandom;


@ManagedBean(name = "withdrawalWalletController")
@ViewScoped
public class WithdrawalWalletController {

    private List<Country> countryList = new ArrayList();
    private List<Maw_bank> bankList = new ArrayList();
    private Maw_bank[] bankList2;
    private Product[] productList;
    private String transactionConcept;
    private Float transactionAmount;
    private String transactionNumber;
    private Country selectedCountry;
    private Bank selectedBank;
    private Product selectedProduct;
    private APIAuthorizerCardManagementSystemProxy apiAuthorizerCardManagementSystemProxy;
    private APIAlodigaWalletProxy apiAlodigaWalletProxy;
    private HttpSession session;
    private Usuario user;
    private ProductResponse productResponse;
    private ResourceBundle msg;
    private BusinessPortalEJB businessPortalEJBProxy;
    private ProductListResponse productListResponse;
    private AccountBankListResponse accountBankListResponse;
    private List<AccountTypeBank> accountTypeBankList = new ArrayList();
    private AccountTypeBank selectedAccountTypeBank;
    private String numberAccountBank;
    private AccountBank accountBank = null;
    private com.alodiga.wallet.ws.AccountBank[] accountBankList;
    private UtilsEJB proxyUtilEJB;
    public String onFlowProcess;
   

    @PostConstruct
    public void init() {
        try {

            businessPortalEJBProxy = (BusinessPortalEJB) EJBServiceLocator.getInstance().get(EjbConstants.BUSINESS_PORTAL_EJB);
            apiAuthorizerCardManagementSystemProxy = new APIAuthorizerCardManagementSystemProxy();
            apiAlodigaWalletProxy = new APIAlodigaWalletProxy();
            msg = ResourceBundle.getBundle("com.alodiga.wallet.messages.message", Locale.forLanguageTag("es"));


            //Se obtiene el usuario de sesión
            session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            user = (Usuario) session.getAttribute("user");

         

            //Se obtiene la lista de países
            countryList = businessPortalEJBProxy.getCountries();

            //Se obtiene la lista de bancos por usuario
//            accountBankByUser = proxyUtilEJB.getAccountBankByUser();

            //Se obtiene la lista de productos del usuario
            productListResponse = apiAlodigaWalletProxy.getProductsByUserId(String.valueOf(user.getUsuarioID()));
            productList = productListResponse.getProducts();
             //Se obtiene la lista de bancos por usuario

            accountBankListResponse = apiAlodigaWalletProxy.getAccountBankByUser(Long.valueOf(user.getUsuarioID()));
            accountBankList = accountBankListResponse.getAccountBanks();
            if (accountBankList.length > 0) {

            }

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

    public List<Maw_bank> getBankList() {
        return bankList;
    }

    public void setBankList(List<Maw_bank> bankList) {
        this.bankList = bankList;
    }

    public Maw_bank[] getBankList2() {
        return bankList2;
    }

    public void setBankList2(Maw_bank[] bankList2) {
        this.bankList2 = bankList2;
    }

    public Product[] getProductList() {
        return productList;
    }

    public void setProductList(Product[] productList) {
        this.productList = productList;
    }

    public String getTransactionConcept() {
        return transactionConcept;
    }

    public void setTransactionConcept(String transactionConcept) {
        this.transactionConcept = transactionConcept;
    }

    public Float getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Float transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
        bankList.clear();
        try {
            if (selectedCountry != null) {
                BankListResponse bankListResponse = apiAlodigaWalletProxy.getBankByCountryApp(String.valueOf(selectedCountry.getId()));
                bankList2 = bankListResponse.getBanks();
                for (Maw_bank b: bankList2) {
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

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public APIAuthorizerCardManagementSystemProxy getApiAuthorizerCardManagementSystemProxy() {
        return apiAuthorizerCardManagementSystemProxy;
    }

    public void setApiAuthorizerCardManagementSystemProxy(APIAuthorizerCardManagementSystemProxy apiAuthorizerCardManagementSystemProxy) {
        this.apiAuthorizerCardManagementSystemProxy = apiAuthorizerCardManagementSystemProxy;
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

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public void setProductResponse(ProductResponse productResponse) {
        this.productResponse = productResponse;
    }

    public ResourceBundle getMsg() {
        return msg;
    }

    public void setMsg(ResourceBundle msg) {
        this.msg = msg;
    }

    public BusinessPortalEJB getBusinessPortalEJBProxy() {
        return businessPortalEJBProxy;
    }

    public void setBusinessPortalEJBProxy(BusinessPortalEJB businessPortalEJBProxy) {
        this.businessPortalEJBProxy = businessPortalEJBProxy;
    }

    public ProductListResponse getProductListResponse() {
        return productListResponse;
    }

    public void setProductListResponse(ProductListResponse productListResponse) {
        this.productListResponse = productListResponse;
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

    public String getNumberAccountBank() {
        return numberAccountBank;
    }

    public void setNumberAccountBank(String numberAccountBank) {
        this.numberAccountBank = numberAccountBank;
    }

    public AccountBank getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(AccountBank accountBank) {
        this.accountBank = accountBank;
    }

    public AccountBankListResponse getAccountBankListResponse() {
        return accountBankListResponse;
    }

    public void setAccountBankListResponse(AccountBankListResponse accountBankListResponse) {
        this.accountBankListResponse = accountBankListResponse;
    }

    public UtilsEJB getProxyUtilEJB() {
        return proxyUtilEJB;
    }

    public void setProxyUtilEJB(UtilsEJB proxyUtilEJB) {
        this.proxyUtilEJB = proxyUtilEJB;
    }

    public com.alodiga.wallet.ws.AccountBank[] getAccountBankList() {
        return accountBankList;
    }

    public void setAccountBankList(com.alodiga.wallet.ws.AccountBank[] accountBankList) {
        this.accountBankList = accountBankList;
    }

    public String getOnFlowProcess() {
        return onFlowProcess;
    }

    public void setOnFlowProcess(String onFlowProcess) {
        this.onFlowProcess = onFlowProcess;
    }

    



}