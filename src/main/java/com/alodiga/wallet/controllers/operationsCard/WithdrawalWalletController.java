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
import com.alodiga.wallet.common.model.Product;
import com.alodiga.wallet.ws.ProductResponse;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import com.alodiga.wallet.common.utils.EjbConstants;
import com.alodiga.wallet.ws.BankListResponse;
import com.alodiga.wallet.common.genericEJB.EJBRequest;
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
import com.alodiga.wallet.common.ejb.ProductEJB;
import com.alodiga.wallet.common.ejb.UtilsEJB;
import com.alodiga.wallet.common.enumeraciones.DocumentTypeE;
import com.alodiga.wallet.common.enumeraciones.OriginAplicationE;
import com.alodiga.wallet.common.enumeraciones.ResponseCodeE;
import com.alodiga.wallet.common.enumeraciones.StatusAccountBankE;
import com.alodiga.wallet.common.exception.EmptyListException;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.model.AccountTypeBank;
import com.alodiga.wallet.common.model.Bank;
import com.alodiga.wallet.common.model.Country;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.ProductResponse;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import com.alodiga.wallet.common.utils.EjbConstants;
import com.alodiga.wallet.common.model.AccountTypeBank;
import com.alodiga.wallet.ws.BankListResponse;
import com.alodiga.wallet.ws.CreditCardListResponse;
import com.alodiga.wallet.ws.AccountTypeBankListResponse;
import com.alodiga.wallet.ws.ProductListResponse;
import com.alodiga.wallet.common.model.AccountBank;
import com.alodiga.wallet.common.model.StatusAccountBank;
import static com.alodiga.wallet.controllers.operationsCard.AddAccountController.businessPortalEJBProxy;
import com.alodiga.wallet.ws.AccountBankListResponse;
import com.alodiga.wallet.ws.TransactionApproveRequestResponse;
import com.alodiga.wallet.ws.TransactionResponse;
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
    private List<Bank> bankList = new ArrayList();
    private List<Bank> bankListWithdrawal = new ArrayList();
    private List<AccountBank> accountBankList = new ArrayList();
    private List<Product> productList = new ArrayList();
    private String transactionConcept;
    private Float transactionAmount;
    private String transactionNumber;
    private String accountBankType;
    private Country selectedCountry;
    private Country selectedCountryWithdrawal;
    private Bank selectedBank;
    private Bank selectedBankWithdrawal;
    private AccountBank selectedAccountBank;
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
    private UtilsEJB proxyUtilEJB;
    public String onFlowProcess;
    private UtilsEJB utilsEJBProxy;
    private static ProductEJB productEJBProxy;
    
   
    @PostConstruct
    public void init() {
        try {

            businessPortalEJBProxy = (BusinessPortalEJB) EJBServiceLocator.getInstance().get(EjbConstants.BUSINESS_PORTAL_EJB);
            apiAuthorizerCardManagementSystemProxy = new APIAuthorizerCardManagementSystemProxy();
            utilsEJBProxy = (UtilsEJB) EJBServiceLocator.getInstance().get(EjbConstants.UTILS_EJB);
            apiAlodigaWalletProxy = new APIAlodigaWalletProxy();
            msg = ResourceBundle.getBundle("com.alodiga.wallet.messages.message", Locale.forLanguageTag("es"));
            productEJBProxy = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);


            //Se obtiene el usuario de sesión
            session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            user = (Usuario) session.getAttribute("user");

            EJBRequest request = new EJBRequest();  
            accountTypeBankList = businessPortalEJBProxy.getAccountTypeBanks(request); 

            //Se obtiene la lista de países
            countryList = businessPortalEJBProxy.getCountries();

            //Se obtiene la lista de bancos por usuario
//            accountBankByUser = proxyUtilEJB.getAccountBankByUser();

            //Se obtiene la lista de productos del usuario
            productList = productEJBProxy.getProductsByWalletUser(Long.valueOf(user.getUsuarioID()));
             //Se obtiene la lista de bancos por usuario


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

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public static ProductEJB getProductEJBProxy() {
        return productEJBProxy;
    }

    public static void setProductEJBProxy(ProductEJB productEJBProxy) {
        WithdrawalWalletController.productEJBProxy = productEJBProxy;
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

  public void setSelectedCountry(Country selectedCountry) throws EmptyListException {
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

  

    public boolean haveAccount(){
      boolean answer = true;
       if( accountBankList == null || accountBankList.size() <= 0){
          answer = false;
        }
       return answer;
     }

    public AccountBank getSelectedAccountBank() {
        return selectedAccountBank;
    }

    public void setSelectedAccountBank(AccountBank selectedAccountBank) {
        this.selectedAccountBank = selectedAccountBank;
        this.accountBankType = selectedAccountBank.getAccountTypeBankId().getDescription();
    }


   
    public List<Bank> getBankList() {
        return bankList;
    }

    public void setBankList(List<Bank> bankList) {
        this.bankList = bankList;
    }

    public UtilsEJB getUtilsEJBProxy() {
        return utilsEJBProxy;
    }

    public void setUtilsEJBProxy(UtilsEJB utilsEJBProxy) {
        this.utilsEJBProxy = utilsEJBProxy;
    }

    public List<Bank> getBankListWithdrawal() {
        return bankListWithdrawal;
    }

    public void setBankListWithdrawal(List<Bank> bankListWithdrawal) {
        this.bankListWithdrawal = bankListWithdrawal;
    }

    public List<AccountBank> getAccountBankList() {
        return accountBankList;
    }

    public void setAccountBankList(List<AccountBank> accountBankList) {
        this.accountBankList = accountBankList;
    }

    public String getAccountBankType() {
        return accountBankType;
    }

    public void setAccountBankType(String accountBankType) {
        this.accountBankType = accountBankType;
    }

 


    public Country getSelectedCountryWithdrawal() {
        return selectedCountryWithdrawal;
    }

    public void setSelectedCountryWithdrawal(Country selectedCountryWithdrawal) {
        this.selectedCountryWithdrawal = selectedCountryWithdrawal;
        bankListWithdrawal.clear();
        try {
            if (selectedCountryWithdrawal != null) {
                bankListWithdrawal = utilsEJBProxy.getBankByCountry(Long.valueOf(selectedCountryWithdrawal.getId()));
            }
         
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public void submit() {
//cambiar el nombre del campo por objeto
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
                  FacesContext context = FacesContext.getCurrentInstance();
                  context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Las respuestas del usuario se guardaron correctamente en la BD", null));
               }else{
                  FacesContext context = FacesContext.getCurrentInstance();
                  context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Se presentó un problema al guardar los datos, por favor intente de nuevo", null));  
               }
            
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(RechargeCardController.class.getName()).log(Level.SEVERE, null, ex);      
            }

   }
  }
    
        public void submitWithdrawal() {
        FacesContext context = FacesContext.getCurrentInstance();
        int documentTypeId = DocumentTypeE.MWAR.getId();
        int originApplicationId = OriginAplicationE.AWAWEB.getId();
        
        try {
            //Se guarda la transacción de Retiro Manual en la BD de AlodigaWallet
            TransactionResponse transactionResponse = apiAlodigaWalletProxy.manualWithdrawals(selectedBankWithdrawal.getId(), user.getEmail(), transactionAmount, selectedProduct.getId(), transactionConcept, Long.valueOf(documentTypeId),Long.valueOf(originApplicationId));
            if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.SUCCESS.getCode())) {
               TransactionApproveRequestResponse transactionApproveRequestResponse = apiAlodigaWalletProxy.saveTransactionApproveRequest(Long.valueOf(user.getUsuarioID()), selectedProduct.getId(), Long.parseLong(transactionResponse.getIdTransaction()), selectedAccountBank.getId(), Long.valueOf(documentTypeId), Long.valueOf(originApplicationId));
               if (transactionApproveRequestResponse.getCodigoRespuesta().equals(ResponseCodeE.SUCCESS.getCode())) {
                   FacesContext.getCurrentInstance().addMessage("notification", new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg.getString("manualWithdrawals.saveSuccesfull")));
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
            Logger.getLogger(WithdrawalWalletController.class.getName()).log(Level.SEVERE, null, ex);      
        }        
      }

    public Bank getSelectedBankWithdrawal() {
        return selectedBankWithdrawal;
    }

    public void setSelectedBankWithdrawal(Bank selectedBankWithdrawal) {
        this.selectedBankWithdrawal = selectedBankWithdrawal;
        accountBankList.clear();
        try {
            if (selectedBankWithdrawal != null) {
               accountBankList = utilsEJBProxy.getAccountBankByBankByUser(selectedBankWithdrawal.getId(),Long.valueOf(user.getUsuarioID()));
            }
         
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }


}