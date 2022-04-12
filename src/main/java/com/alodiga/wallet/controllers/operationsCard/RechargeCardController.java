package com.alodiga.wallet.controllers.operationsCard;



import com.ericsson.alodiga.ws.PreguntaIdioma;
import com.ericsson.alodiga.ws.Respuesta;
import com.ericsson.alodiga.ws.RespuestaPreguntasSecretas;
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
import com.cms.commons.enumeraciones.ChannelE;
import com.cms.commons.enumeraciones.ResponseCodeE;
import javax.faces.bean.ManagedProperty;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.ResourceBundle;
import org.primefaces.context.RequestContext;


/**
 *
 * @author henry
 */
@ManagedBean(name = "rechargeCardController")
@ViewScoped
public class RechargeCardController {

    private String sourceProduct;
    private Float rechargeAmount;
    private String rechargeConcept;
    private static APIAuthorizerCardManagementSystemProxy apiAuthorizerCardManagementSystemProxy;
    private static APIAlodigaWalletProxy apiAlodigaWalletProxy;
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
            
            //Se obtiene el producto por defecto asociado a la billetera
            productResponse = apiAlodigaWalletProxy.getProductPrepaidCardByUser(Long.valueOf(user.getUsuarioID()));
            String productName = productResponse.getResponse().getName();
            this.sourceProduct = productName;
        
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(RechargeCardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getSourceProduct() {
        return sourceProduct;
    }

    public void setSourceProduct(String sourceProduct) {
        this.sourceProduct = sourceProduct;
    }

    public Float getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Float rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public String getRechargeConcept() {
        return rechargeConcept;
    }

    public void setRechargeConcept(String rechargeConcept) {
        this.rechargeConcept = rechargeConcept;
    }

    public static APIAlodigaWalletProxy getApiAlodigaWalletProxy() {
        return apiAlodigaWalletProxy;
    }

    public static void setApiAlodigaWalletProxy(APIAlodigaWalletProxy apiAlodigaWalletProxy) {
        RechargeCardController.apiAlodigaWalletProxy = apiAlodigaWalletProxy;
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
        RechargeCardController.apiAuthorizerCardManagementSystemProxy = apiAuthorizerCardManagementSystemProxy;
    }
    
    
    public void submit() {
        FacesContext context = FacesContext.getCurrentInstance();
        Card card = null;
        Long messageMiddlewareId = 1L;
        int channelWallet = ChannelE.WALLET.getId();
        Long transactioExternalId = 1L;
        Integer acquirerCountry = 862;
        String cardNumber = "";

        //Se obtiene la tarjeta del usuario
        try {
            CardResponse cardResponse = apiAlodigaWalletProxy.getCardByEmail(user.getEmail());
            cardNumber = cardResponse.getCardNumber();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Logger.getLogger(RechargeCardController.class.getName()).log(Level.SEVERE, null, ex);      
        }
        
        if (sourceProduct != null && rechargeAmount != null) {
            try {
                com.alodiga.cms.ws.TransactionResponse transactionResponse = apiAuthorizerCardManagementSystemProxy.cardRechargeWallet(cardNumber,messageMiddlewareId,channelWallet,rechargeAmount,rechargeConcept,transactioExternalId,acquirerCountry);
                if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.SUCCESS.getCode())) {
                   FacesContext.getCurrentInstance().addMessage("notification", new FacesMessage(FacesMessage.SEVERITY_INFO, "", msg.getString("cardRechargeSaveSuccesfull")));
                }else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.INVALID_TRANSACTIONAL_LIMITS.getCode())) {
                   context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, transactionResponse.getMensajeRespuesta(), null));  
                } else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.CARD_NOT_VALIDATE.getCode())) {
                   context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, transactionResponse.getMensajeRespuesta(), null));      
                } else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.INTERNAL_ERROR.getCode())) {
                   context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, transactionResponse.getMensajeRespuesta(), null));  
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(RechargeCardController.class.getName()).log(Level.SEVERE, null, ex);      
            }
        }  
    }

}
