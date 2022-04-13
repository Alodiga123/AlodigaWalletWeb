package com.alodiga.wallet.controllers.operationsCard;

import com.alodiga.cms.commons.ejb.PersonEJB;
import com.alodiga.cms.ws.APIAuthorizerCardManagementSystemProxy;
import com.alodiga.cms.ws.TransactionResponse;
import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.cms.commons.enumeraciones.ResponseCodeE;
import com.alodiga.wallet.common.exception.KeyLongException;
import com.alodiga.wallet.common.model.Bank;
import com.alodiga.wallet.common.model.Country;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.Product;
import com.alodiga.wallet.ws.ProductResponse;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import com.alodiga.wallet.common.utils.EjbConstants;
import com.alodiga.wallet.common.utils.S3cur1ty3Cryt3r;
import com.alodiga.wallet.ws.BankListResponse;
import com.alodiga.wallet.ws.CardResponse;
import com.alodiga.wallet.ws.Maw_bank;
import com.alodiga.wallet.ws.ProductListResponse;
import com.cms.commons.genericEJB.EJBRequest;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.PreguntaIdioma;
import com.ericsson.alodiga.ws.RespuestaUsuario;
import com.ericsson.alodiga.ws.Usuario;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FlowEvent;
import com.cms.commons.enumeraciones.ChannelE;
import com.alodiga.wallet.utils.Utils;
import com.alodiga.wallet.ws.PersonResponse;
import com.cms.commons.enumeraciones.TransactionE;
import com.cms.commons.models.Card;
import java.time.LocalDate;
import static java.time.temporal.TemporalQueries.localDate;




@ManagedBean(name = "activateCardController")
@ViewScoped
public class ActivateCardController {

    private List<Country> countryList = new ArrayList();
    private Product[] productList;
    private String transactionConcept;
    private Float transactionAmount;
    private String transactionNumber;
    private String accountBankNumber;
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
    private PersonResponse personResponse;
    public String cardNumber;
    private String sourceProduct;
    private String keyOperations;
    private APIAuthorizerCardManagementSystemProxy apiAuthorizerCardManagementSystemProxy1;
    private String idTranstaction; 
    private String date;
    private Utils utils;
    public String transformCardNumber;
    private CardResponse cardResponseWallet;
    private String expirationDate;
    private Card card = null;
    private String email;
    private String phone;
    private String cvv;
    private String documentNumber;
    private String securityQuestions;
    private Calendar dateOfBirth;
    private String cardHolder;
    private Calendar dateTransaction;
    

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

              //Se obtiene el producto por defecto asociado a la billetera
            productResponse = apiAlodigaWalletProxy.getProductPrepaidCardByUser(Long.valueOf(user.getUsuarioID()));
            String productName = productResponse.getResponse().getName();
            this.sourceProduct = productName;
            
             //Se obtiene el nombre del usuario  
            personResponse = apiAlodigaWalletProxy.getPersonByEmail(user.getEmail());
            
             //Se obtiene la tarjeta del usuario  
            cardResponseWallet = apiAlodigaWalletProxy.getCardByEmail(user.getEmail());
            cardNumber = cardResponseWallet.getCardNumber();

            utils = new Utils();
            transformCardNumber = utils.transformCardNumber(cardNumber);

            cardHolder = cardResponseWallet.getCardHolder();
            
            //se Obtiene la fecha de expiracion de la tarjeta
            SimpleDateFormat format = new SimpleDateFormat("MMyy");
            expirationDate = format.format(card.getExpirationDate());

            
          //Fecha Actual
          dateTransaction = Calendar.getInstance();


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

    public PersonResponse getPersonResponse() {
        return personResponse;
    }

    public void setPersonResponse(PersonResponse personResponse) {
        this.personResponse = personResponse;
    }



    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAccountBankNumber() {
        return accountBankNumber;
    }

    public void setAccountBankNumber(String accountBankNumber) {
        this.accountBankNumber = accountBankNumber;
    }

    public String getSourceProduct() {
        return sourceProduct;
    }

    public void setSourceProduct(String sourceProduct) {
        this.sourceProduct = sourceProduct;
    }

    public String getKeyOperations() {
        return keyOperations;
    }

    public void setKeyOperations(String keyOperations) {
        this.keyOperations = keyOperations;
    }

    public APIAuthorizerCardManagementSystemProxy getApiAuthorizerCardManagementSystemProxy1() {
        return apiAuthorizerCardManagementSystemProxy1;
    }

    public void setApiAuthorizerCardManagementSystemProxy1(APIAuthorizerCardManagementSystemProxy apiAuthorizerCardManagementSystemProxy1) {
        this.apiAuthorizerCardManagementSystemProxy1 = apiAuthorizerCardManagementSystemProxy1;
    }

    public String getIdTranstaction() {
        return idTranstaction;
    }

    public void setIdTranstaction(String idTranstaction) {
        this.idTranstaction = idTranstaction;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

   
    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public Utils getUtils() {
        return utils;
    }

    public void setUtils(Utils utils) {
        this.utils = utils;
    }

    public String getTransformCardNumber() {
        return transformCardNumber;
    }

    public void setTransformCardNumber(String transformCardNumber) {
        this.transformCardNumber = transformCardNumber;
    }

    public CardResponse getCardResponseWallet() {
        return cardResponseWallet;
    }

    public void setCardResponseWallet(CardResponse cardResponseWallet) {
        this.cardResponseWallet = cardResponseWallet;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getSecurityQuestions() {
        return securityQuestions;
    }

    public void setSecurityQuestions(String securityQuestions) {
        this.securityQuestions = securityQuestions;
    }

    public Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Calendar getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(Calendar dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

 
    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

 

    
  
    
    
    public String onFlowProcess(FlowEvent event) {
//        APIAlodigaWalletProxy walletProxy = new APIAlodigaWalletProxy();
        RespuestaUsuario respUser = new RespuestaUsuario();
        APIRegistroUnificadoProxy unificadoProxy = new APIRegistroUnificadoProxy();
        switch (event.getOldStep()) {
            case "key": {
                
                try {
                    String pass = S3cur1ty3Cryt3r.aloDesencript(keyOperations, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
                    respUser = unificadoProxy.validarPin("usuarioWS", "passwordWS", user.getUsuarioID(), pass);

                    if (respUser.getCodigoRespuesta().equals("00")) {
                        System.out.println("paso validacion pin" + respUser.getCodigoRespuesta());
                    } else {
                        addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");
                        return "key";
                    }


                } catch (NoSuchAlgorithmException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");
                    return "key";
                } catch (IllegalBlockSizeException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");
                    return "key";
                } catch (NoSuchPaddingException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");
                    return "key";
                } catch (BadPaddingException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");
                    return "key";
                } catch (KeyLongException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");
                    return "key";
                } catch (Exception e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");
                    return "key";
                }

            }

            break;
            case "confirmation": {
                
             
            }
            
        }
        return event.getNewStep();

    }

   public void submit(){
      Long messageMiddlewareId = 1L;
      int channelWallet = ChannelE.WALLET.getId();
      Long transactioExternalId = 1L;
      int countryAcquirerId = 862;
      FacesContext context = FacesContext.getCurrentInstance();
      try{
       
       TransactionResponse transactionResponse = apiAuthorizerCardManagementSystemProxy.activateCard( cardNumber, cardHolder, cvv, expirationDate, documentNumber, phone, dateOfBirth, email, messageMiddlewareId, TransactionE.ACTIVACION_TARJETA.getId(), channelWallet, dateTransaction, "1", "1", "1", countryAcquirerId);
        if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.SUCCESS.getCode())) {
                   FacesContext.getCurrentInstance().addMessage("notification", new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg.getString("transferTitleSucces")));
        }else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.INTERNAL_ERROR.getCode())) {
               FacesContext.getCurrentInstance().addMessage("notification", new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg.getString("theOperationFailed")));   
        } else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.ACTIVE_CARD_YES.getCode())) {
               FacesContext.getCurrentInstance().addMessage("notification", new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg.getString("theOperationFailed")));       
        }else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.DATE_BIRTH_NOT_MATCH.getCode())) {
               FacesContext.getCurrentInstance().addMessage("notification", new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg.getString("theOperationFailed")));       
        }else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.EMAIL_CUSTOMER_NOT_MATCH.getCode())) {
               FacesContext.getCurrentInstance().addMessage("notification", new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg.getString("theOperationFailed")));       
        }else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.PHONE_CUSTOMER_NOT_MATCH.getCode())) {
                FacesContext.getCurrentInstance().addMessage("notification", new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg.getString("theOperationFailed")));       
        }
        }catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(ActivateCardController.class.getName()).log(Level.SEVERE, null, ex);
       } 
     }
}