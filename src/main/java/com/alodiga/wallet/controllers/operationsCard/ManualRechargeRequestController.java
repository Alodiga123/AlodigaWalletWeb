/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.controllers.operationsCard;

import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.cms.ws.APIAuthorizerCardManagementSystemProxy;
import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.enumeraciones.DocumentTypeE;
import com.alodiga.wallet.common.enumeraciones.OriginAplicationE;
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
import com.alodiga.wallet.ws.TransactionApproveRequestResponse;
import com.alodiga.wallet.ws.TransactionResponse;
import com.alodiga.wallet.common.enumeraciones.ResponseCodeE;
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

/**
 *
 * @author jose
 */

@ManagedBean(name = "manualRechargeRequestController")
@ViewScoped
public class ManualRechargeRequestController {

    private List<Country> countryList = new ArrayList();
    private List<Maw_bank> bankList = new ArrayList();
    private Maw_bank[] bankList2;
    private com.alodiga.wallet.ws.Product[] productList;
    private List<com.alodiga.wallet.ws.Product> productList2 = new ArrayList();
    private String transactionConcept;
    private Float transactionAmount;
    private String transactionNumber;
    private Country selectedCountry;
    private Maw_bank selectedBank;
    private com.alodiga.wallet.ws.Product selectedProduct;
    private static APIAuthorizerCardManagementSystemProxy apiAuthorizerCardManagementSystemProxy;
    private static APIAlodigaWalletProxy apiAlodigaWalletProxy;
    private HttpSession session;
    private Usuario user;
    private ProductResponse productResponse;
    private ResourceBundle msg;
    private static BusinessPortalEJB businessPortalEJBProxy;
    private ProductListResponse productListResponse;
    
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
            
            //Se obtiene la lista de productos del usuario
            productListResponse = apiAlodigaWalletProxy.getProductsByUserId(String.valueOf(user.getUsuarioID()));
            productList = productListResponse.getProducts();
            for (com.alodiga.wallet.ws.Product p: productList) {
                productList2.add(p);
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

    public static BusinessPortalEJB getBusinessPortalEJBProxy() {
        return businessPortalEJBProxy;
    }

    public static void setBusinessPortalEJBProxy(BusinessPortalEJB businessPortalEJBProxy) {
        ManualRechargeRequestController.businessPortalEJBProxy = businessPortalEJBProxy;
    }

    public com.alodiga.wallet.ws.Product[] getProductList() {
        return productList;
    }

    public void setProductList(com.alodiga.wallet.ws.Product[] productList) {
        this.productList = productList;
    }

    public ProductListResponse getProductListResponse() {
        return productListResponse;
    }

    public void setProductListResponse(ProductListResponse productListResponse) {
        this.productListResponse = productListResponse;
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

    public static APIAuthorizerCardManagementSystemProxy getApiAuthorizerCardManagementSystemProxy() {
        return apiAuthorizerCardManagementSystemProxy;
    }

    public static void setApiAuthorizerCardManagementSystemProxy(APIAuthorizerCardManagementSystemProxy apiAuthorizerCardManagementSystemProxy) {
        ManualRechargeRequestController.apiAuthorizerCardManagementSystemProxy = apiAuthorizerCardManagementSystemProxy;
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

    public Maw_bank getSelectedBank() {
        return selectedBank;
    }

    public void setSelectedBank(Maw_bank selectedBank) {
        this.selectedBank = selectedBank;
    }

    public com.alodiga.wallet.ws.Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(com.alodiga.wallet.ws.Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public List<com.alodiga.wallet.ws.Product> getProductList2() {
        return productList2;
    }

    public void setProductList2(List<com.alodiga.wallet.ws.Product> productList2) {
        this.productList2 = productList2;
    }
    
    public void sumit() {
        FacesContext context = FacesContext.getCurrentInstance();
        int documentTypeId = DocumentTypeE.MRAR.getId();
        int originApplicationId = OriginAplicationE.AWAWEB.getId();
        
        try {
            //Se guarda la transacción de Recarga Manual en la BD de AlodigaWallet
            TransactionResponse transactionResponse = apiAlodigaWalletProxy.manualRecharge(selectedBank.getId(),user.getEmail(),transactionNumber,transactionAmount,selectedProduct.getId(),transactionConcept,Long.valueOf(documentTypeId),Long.valueOf(originApplicationId));
            if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.SUCCESS.getCode())) {
               TransactionApproveRequestResponse transactionApproveRequestResponse = apiAlodigaWalletProxy.saveTransactionApproveRequest(Long.valueOf(user.getUsuarioID()), selectedProduct.getId(), Long.parseLong(transactionResponse.getIdTransaction()), selectedBank.getId(), Long.valueOf(documentTypeId), Long.valueOf(originApplicationId));
               if (transactionApproveRequestResponse.getCodigoRespuesta().equals(ResponseCodeE.SUCCESS.getCode())) {
                   FacesContext.getCurrentInstance().addMessage("notification", new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg.getString("manualRechargeRequest.saveSuccesfull")));
               } 
            }else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.DISABLED_TRANSACTION.getCode())) {
               context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, transactionResponse.getMensajeRespuesta(), null));  
            } else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.TRANSACTION_AMOUNT_LIMIT.getCode())) {
               context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, transactionResponse.getMensajeRespuesta(), null));      
            } else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.TRANSACTION_QUANTITY_LIMIT_DIALY.getCode())) {
               context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, transactionResponse.getMensajeRespuesta(), null)); 
            } else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.TRANSACTION_AMOUNT_LIMIT_DIALY.getCode())) {
               context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, transactionResponse.getMensajeRespuesta(), null));  
            } else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.TRANSACTION_QUANTITY_LIMIT_DIALY.getCode())) {
               context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, transactionResponse.getMensajeRespuesta(), null));  
            } else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.TRANSACTION_AMOUNT_LIMIT_MONTHLY.getCode())) {
               context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, transactionResponse.getMensajeRespuesta(), null));  
            } else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.TRANSACTION_QUANTITY_LIMIT_YEARLY.getCode())) {
               context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, transactionResponse.getMensajeRespuesta(), null));  
            } else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.TRANSACTION_AMOUNT_LIMIT_YEARLY.getCode())) {
               context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, transactionResponse.getMensajeRespuesta(), null));  
            } else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.INTERNAL_ERROR.getCode())) {
               context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, transactionResponse.getMensajeRespuesta(), null));  
            }           
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(ManualRechargeRequestController.class.getName()).log(Level.SEVERE, null, ex);      
        }
        
        
    }
    
}