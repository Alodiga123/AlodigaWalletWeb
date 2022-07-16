/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.controllers.operationsCard;


import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.ejb.ProductEJB;
import com.alodiga.wallet.common.ejb.UtilsEJB;
import com.alodiga.wallet.common.enumeraciones.DocumentTypeE;
import com.alodiga.wallet.common.enumeraciones.OriginAplicationE;
import com.alodiga.wallet.common.model.Bank;
import com.alodiga.wallet.common.model.Country;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.common.model.Product;
import com.alodiga.wallet.ws.ProductResponse;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import com.alodiga.wallet.common.utils.EjbConstants;
import com.alodiga.wallet.ws.ProductListResponse;
import com.alodiga.wallet.ws.TransactionApproveRequestResponse;
import com.alodiga.wallet.ws.TransactionResponse;
import com.ericsson.alodiga.ws.Usuario;
import java.util.ArrayList;
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

/**
 *
 * @author jose
 */

@ManagedBean(name = "manualRechargeRequestController")
@ViewScoped
public class ManualRechargeRequestController {

    private List<Country> countryList = new ArrayList();
    private List<Bank> bankList = new ArrayList();
    private List<Product> productList = new ArrayList();
    private String transactionConcept;
    private Float transactionAmount;
    private String transactionNumber;
    private Country selectedCountry;
    private Bank selectedBank;
    private Product selectedProduct;

    private static APIAlodigaWalletProxy apiAlodigaWalletProxy;
    private HttpSession session;
    private Usuario user;
    private ProductResponse productResponse;
    private ResourceBundle msg;
    private static BusinessPortalEJB businessPortalEJBProxy;
    private static ProductEJB productEJBProxy;
    private static UtilsEJB utilsEJBProxy;
    private ProductListResponse productListResponse;
    
    @PostConstruct
    public void init() {
        try {
            businessPortalEJBProxy = (BusinessPortalEJB) EJBServiceLocator.getInstance().get(EjbConstants.BUSINESS_PORTAL_EJB);
            productEJBProxy = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            utilsEJBProxy = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);

            apiAlodigaWalletProxy = new APIAlodigaWalletProxy();
            msg = ResourceBundle.getBundle("com.alodiga.wallet.messages.message", Locale.forLanguageTag("es"));
            
            //Se obtiene el usuario de sesión
            session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            user = (Usuario) session.getAttribute("user");
            
            //Se obtiene la lista de países
            countryList = businessPortalEJBProxy.getCountries();
            
            //Se obtiene la lista de productos del usuario

        
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(RechargeCardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
        bankList.clear();
        try {
            if (selectedCountry != null) {
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public List<Bank> getBankList() {
        return bankList;
    }

    public void setBankList(List<Bank> bankList) {
        this.bankList = bankList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
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


    public static APIAlodigaWalletProxy getApiAlodigaWalletProxy() {
        return apiAlodigaWalletProxy;
    }

    public static void setApiAlodigaWalletProxy(APIAlodigaWalletProxy apiAlodigaWalletProxy) {
        ManualRechargeRequestController.apiAlodigaWalletProxy = apiAlodigaWalletProxy;
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

    public static BusinessPortalEJB getBusinessPortalEJBProxy() {
        return businessPortalEJBProxy;
    }

    public static void setBusinessPortalEJBProxy(BusinessPortalEJB businessPortalEJBProxy) {
        ManualRechargeRequestController.businessPortalEJBProxy = businessPortalEJBProxy;
    }

    public static ProductEJB getProductEJBProxy() {
        return productEJBProxy;
    }

    public static void setProductEJBProxy(ProductEJB productEJBProxy) {
        ManualRechargeRequestController.productEJBProxy = productEJBProxy;
    }

    public static UtilsEJB getUtilsEJBProxy() {
        return utilsEJBProxy;
    }

    public static void setUtilsEJBProxy(UtilsEJB utilsEJBProxy) {
        ManualRechargeRequestController.utilsEJBProxy = utilsEJBProxy;
    }

    public ProductListResponse getProductListResponse() {
        return productListResponse;
    }

    public void setProductListResponse(ProductListResponse productListResponse) {
        this.productListResponse = productListResponse;
    }
    
    public Product getProduct(long id) {
        for (Product product : productList) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }
    
    public void submit() {
        FacesContext context = FacesContext.getCurrentInstance();
        int documentTypeId = DocumentTypeE.MRAR.getId();
        int originApplicationId = OriginAplicationE.AWAWEB.getId();
        
       
        
        
    }
    
}
