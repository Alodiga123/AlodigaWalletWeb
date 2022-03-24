package com.alodiga.wallet.controllers.operationsCard;

import com.alodiga.cms.ws.APIAuthorizerCardManagementSystemProxy;
import com.alodiga.cms.ws.TransactionResponse;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.CardResponse;
import com.cms.commons.enumeraciones.ChannelE;
import com.cms.commons.enumeraciones.ResponseCodeE;
import com.alodiga.wallet.common.enumeraciones.DocumentTypeE;
import com.alodiga.wallet.common.enumeraciones.OriginAplicationE;
import com.alodiga.wallet.ws.TransactionApproveRequestResponse;
import com.ericsson.alodiga.ws.Usuario;
import static com.sun.faces.facelets.util.Path.context;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


@ManagedBean(name = "blockedUpController")
@ViewScoped
public class BlockedUpController {

    private APIAuthorizerCardManagementSystemProxy apiAuthorizerCardManagementSystemProxy;
    private HttpSession session;
    private Usuario user;
    public String cardNumber = "";
    private com.alodiga.cms.ws.CardResponse cardResponseCMS = null;
    private CardResponse cardResponseWallet;
    private APIAlodigaWalletProxy apiAlodigaWalletProxy;
    private boolean activeCard;
    private boolean blockedUpCard;
    private ResourceBundle msg;

    @PostConstruct
    public void init() {
        try {
            //Se instancian las API del CMS Autorizador y AlodigaWallet
            apiAuthorizerCardManagementSystemProxy = new APIAuthorizerCardManagementSystemProxy();
            apiAlodigaWalletProxy = new APIAlodigaWalletProxy();
            apiAuthorizerCardManagementSystemProxy.verifyStatusActiveCard(cardNumber);

            //Se obtiene el usuario de sesión
            session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            user = (Usuario) session.getAttribute("user");

            //Se obtiene la tarjeta del usuario   
            cardResponseWallet = apiAlodigaWalletProxy.getCardByEmail(user.getEmail());
            cardNumber = cardResponseWallet.getCardNumber();

            //Se obtiene el estado de la tarjeta (Bloqueada o Desbloqueada)
            blockedUpCard = cardResponseWallet.getIndBlockedUp();

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(BlockedUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public APIAuthorizerCardManagementSystemProxy getApiAuthorizerCardManagementSystemProxy() {
        return apiAuthorizerCardManagementSystemProxy;
    }

    public void setApiAuthorizerCardManagementSystemProxy(APIAuthorizerCardManagementSystemProxy apiAuthorizerCardManagementSystemProxy) {
        this.apiAuthorizerCardManagementSystemProxy = apiAuthorizerCardManagementSystemProxy;
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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public com.alodiga.cms.ws.CardResponse getCardResponseCMS() {
        return cardResponseCMS;
    }

    public void setCardResponseCMS(com.alodiga.cms.ws.CardResponse cardResponseCMS) {
        this.cardResponseCMS = cardResponseCMS;
    }

    public CardResponse getCardResponseWallet() {
        return cardResponseWallet;
    }

    public void setCardResponseWallet(CardResponse cardResponseWallet) {
        this.cardResponseWallet = cardResponseWallet;
    }

    public APIAlodigaWalletProxy getApiAlodigaWalletProxy() {
        return apiAlodigaWalletProxy;
    }

    public void setApiAlodigaWalletProxy(APIAlodigaWalletProxy apiAlodigaWalletProxy) {
        apiAlodigaWalletProxy = apiAlodigaWalletProxy;
    }

    public boolean isActiveCard() {
        return activeCard;
    }

    public void setActiveCard(boolean activeCard) {
        this.activeCard = activeCard;
    }

    public boolean isBlockedUpCard() {
        return blockedUpCard;
    }

    public void setBlockedUpCard(boolean blockedUpCard) {
        this.blockedUpCard = blockedUpCard;
    }

    public boolean verifyCard() {
        activeCard = true;
        try {
            cardResponseCMS = apiAuthorizerCardManagementSystemProxy.verifyStatusActiveCard(cardNumber);
            if (!cardResponseCMS.getCodigoRespuesta().equals(ResponseCodeE.SUCCESS.getCode())) {
                activeCard = false;
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Logger.getLogger(BlockedUpController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return activeCard;
    }
     public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
       FacesContext.getCurrentInstance().
               addMessage(null, new FacesMessage(severity, summary, detail));
   }

    public void changeBlocked() {
        FacesContext context = FacesContext.getCurrentInstance();
        Long messageMiddlewareId = 1L;
        int channelWallet = ChannelE.WALLET.getId();
        Long transactioExternalId = 1L;
        try {
            //Se bloquea o desbloquea la tarjeta
            TransactionResponse transactionResponse = apiAuthorizerCardManagementSystemProxy.blockedUpCard(cardNumber,
                    (blockedUpCard == true) ? 0 : 1, messageMiddlewareId, channelWallet, transactioExternalId);
             //Mensajes
               if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.SUCCESS.getCode()) || blockedUpCard == true)  {
                   addMessage(FacesMessage.SEVERITY_INFO, "Tarjeta Desbloqueada", "" );
            }else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.SUCCESS.getCode()) || blockedUpCard == true)  {
                   addMessage(FacesMessage.SEVERITY_INFO, "Tarjeta Bloqueada", "" );
              }else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.CARD_NOT_EXISTS.getCode())) {
              addMessage(FacesMessage.SEVERITY_INFO, "Tarjeta no existe", ResponseCodeE.CARD_NOT_EXISTS.getMessage());
              }else if (transactionResponse.getCodigoRespuesta().equals(ResponseCodeE.INTERNAL_ERROR.getCode())) {
               addMessage(FacesMessage.SEVERITY_INFO, "No se completo la transaccion", "");      
            }
            }catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(BlockedUpController.class.getName()).log(Level.SEVERE, null, ex);      
        }
       
      }
         
    }
