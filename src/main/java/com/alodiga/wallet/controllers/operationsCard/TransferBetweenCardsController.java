package com.alodiga.wallet.controllers.operationsCard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import com.alodiga.cms.ws.APIAuthorizerCardManagementSystemProxy;
import com.alodiga.wallet.beans.LoginBean;
import com.ericsson.alodiga.ws.Usuario;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.Card;
import com.alodiga.wallet.ws.CardResponse;
import com.alodiga.wallet.ws.ProductResponse;
import com.alodiga.cms.ws.TransactionResponse;
import com.alodiga.wallet.common.exception.KeyLongException;
import com.alodiga.wallet.common.utils.S3cur1ty3Cryt3r;
import com.cms.commons.enumeraciones.ChannelE;
import com.cms.commons.enumeraciones.ResponseCodeE;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.RespuestaUsuario;
import java.security.NoSuchAlgorithmException;
import javax.faces.bean.ManagedProperty;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;


/**
 *
 * @author henry
 */
@ManagedBean(name = "transferBetweenCardsController")
@ViewScoped
public class TransferBetweenCardsController {

    private String sourceCard;
    private String destinationCard;
    private Float transferAmount;
    private String transferConcept;
    private String emailDestinationCard;
    private String keyOperations;
    private static APIAuthorizerCardManagementSystemProxy apiAuthorizerCardManagementSystemProxy;
    private static APIAlodigaWalletProxy apiAlodigaWalletProxy;
    public String cardNumber = "";
    private CardResponse cardResponseWallet;
    private HttpSession session;
    private Usuario user;
    private ProductResponse productResponse;
    private ResourceBundle msg;
    
    @ManagedProperty(value = "#{loginBean}")
    LoginBean loginBean;

    @PostConstruct
    public void init() {
        try {
            apiAuthorizerCardManagementSystemProxy = new APIAuthorizerCardManagementSystemProxy();
            apiAlodigaWalletProxy = new APIAlodigaWalletProxy();
            msg = ResourceBundle.getBundle("com.alodiga.wallet.messages.message", Locale.forLanguageTag("es"));
            
            
            //Se obtiene el usuario de sesión
            session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            user = (Usuario) session.getAttribute("user");
            
            //Se obtiene la tarjeta de origen
            cardResponseWallet = apiAlodigaWalletProxy.getCardByEmail(user.getEmail());
            sourceCard = cardResponseWallet.getCardNumber();
        
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(TransferBetweenCardsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getEmailDestinationCard() {
        return emailDestinationCard;
    }

    public void setEmailDestinationCard(String emailDestinationCard) {
        this.emailDestinationCard = emailDestinationCard;
    }

    public String getSourceCard() {
        return sourceCard;
    }

    public void setSourceCard(String sourceCard) {
        this.sourceCard = sourceCard;
    }

    public Float getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Float transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferConcept() {
        return transferConcept;
    }

    public void setTransferConcept(String transferConcept) {
        this.transferConcept = transferConcept;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getDestinationCard() {
        return destinationCard;
    }

    public void setDestinationCard(String destinationCard) {
        this.destinationCard = destinationCard;
    }

    public String getKeyOperations() {
        return keyOperations;
    }

    public void setKeyOperations(String keyOperations) {
        this.keyOperations = keyOperations;
    }

    public CardResponse getCardResponseWallet() {
        return cardResponseWallet;
    }

    public void setCardResponseWallet(CardResponse cardResponseWallet) {
        this.cardResponseWallet = cardResponseWallet;
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

    public static APIAlodigaWalletProxy getApiAlodigaWalletProxy() {
        return apiAlodigaWalletProxy;
    }

    public static void setApiAlodigaWalletProxy(APIAlodigaWalletProxy apiAlodigaWalletProxy) {
        TransferBetweenCardsController.apiAlodigaWalletProxy = apiAlodigaWalletProxy;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public static APIAuthorizerCardManagementSystemProxy getApiAuthorizerCardManagementSystemProxy() {
        return apiAuthorizerCardManagementSystemProxy;
    }

    public static void setApiAuthorizerCardManagementSystemProxy(APIAuthorizerCardManagementSystemProxy apiAuthorizerCardManagementSystemProxy) {
        TransferBetweenCardsController.apiAuthorizerCardManagementSystemProxy = apiAuthorizerCardManagementSystemProxy;
    }
    
    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }
    
    public void searchDestinationCard() {
        try {
            //Se obtiene la tarjeta de destino
            cardResponseWallet = apiAlodigaWalletProxy.getCardByEmail(emailDestinationCard);
            destinationCard = cardResponseWallet.getCardNumber();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
        
    }
    
    public String onFlowProcess(FlowEvent event) {
        RespuestaUsuario respUser = new RespuestaUsuario();
        APIRegistroUnificadoProxy unificadoProxy = new APIRegistroUnificadoProxy();
        switch (event.getOldStep()) {
            case "transferBetweenCardsData": {
                

            }
            
            case "key": { 
                try {
                    String pass = S3cur1ty3Cryt3r.aloDesencript(keyOperations, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
                    respUser = unificadoProxy.validarPin("usuarioWS", "passwordWS", user.getUsuarioID(), pass);

                    if (!respUser.getCodigoRespuesta().equals("00")) {
                        FacesContext.getCurrentInstance().addMessage("notification", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg.getString("applicationTitle"), msg.getString("keyOperationsInvalid")));
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
    
    
//    public void submit() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        Card card = null;
//        Long messageMiddlewareId = 1L;
//        int channelWallet = ChannelE.WALLET.getId();
//        Long transactioExternalId = 1L;
//        Integer acquirerCountry = 862;
//        String cardNumber = "";
//
//        //Se obtiene la tarjeta del usuario
//        try {
//            CardResponse cardResponse = apiAlodigaWalletProxy.getCardByEmail(user.getEmail());
//            cardNumber = cardResponse.getCardNumber();
//        } catch (RemoteException ex) {
//            ex.printStackTrace();
//            Logger.getLogger(TransferBetweenCardsController.class.getName()).log(Level.SEVERE, null, ex);      
//        }
//        
//        if (sourceCard != null) {
//            try {
//                com.alodiga.cms.ws.TransactionResponse transactionResponse = apiAuthorizerCardManagementSystemProxy.cardRechargeWallet(cardNumber,messageMiddlewareId,channelWallet,rechargeAmount,rechargeConcept,transactioExternalId,acquirerCountry);
//                if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.SUCCESS.getCode())) {
//                   FacesContext.getCurrentInstance().addMessage("notification", new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg.getString("cardRecharge.saveSuccesfull")));
//                }else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.INVALID_TRANSACTIONAL_LIMITS.getCode())) {
//                   context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, transactionResponse.getMensajeRespuesta(), null));  
//                } else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.CARD_NOT_VALIDATE.getCode())) {
//                   context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, transactionResponse.getMensajeRespuesta(), null));      
//                } else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.INTERNAL_ERROR.getCode())) {
//                   context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, transactionResponse.getMensajeRespuesta(), null));  
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                Logger.getLogger(TransferBetweenCardsController.class.getName()).log(Level.SEVERE, null, ex);      
//            }
//        }  
//    }

}
