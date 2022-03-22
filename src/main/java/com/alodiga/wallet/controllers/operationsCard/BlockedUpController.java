package com.alodiga.wallet.controllers.operationsCard;

import com.alodiga.cms.ws.APIAuthorizerCardManagementSystemProxy;
import com.alodiga.cms.ws.TransactionResponse;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.CardResponse;
import com.cms.commons.enumeraciones.ChannelE;
import com.cms.commons.enumeraciones.ResponseCodeE;
import com.ericsson.alodiga.ws.Usuario;
import java.rmi.RemoteException;
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
    private boolean blocked;
    private boolean blockedUpCard;

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

            blockedUpCard = cardResponseWallet.getIndBlockedUp();

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(BlockedUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean verifyCard() {
        blockedUpCard = true;
        try {
            cardResponseCMS = apiAuthorizerCardManagementSystemProxy.verifyStatusActiveCard(cardNumber);
            if (!cardResponseCMS.getCodigoRespuesta().equals(ResponseCodeE.SUCCESS.getCode())) {
                blockedUpCard = false;
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Logger.getLogger(BlockedUpController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return blockedUpCard;
    }

    public void changeBlocked() {
        Long messageMiddlewareId = 1L;
        int channelWallet = ChannelE.WALLET.getId();
        Long transactioExternalId = 1L;
        try {
            TransactionResponse transactionResponse = apiAuthorizerCardManagementSystemProxy.blockedUpCard(cardNumber,
                    (blocked == true) ? 0 : 1, messageMiddlewareId, channelWallet, transactioExternalId);

        } catch (RemoteException e) {
            e.printStackTrace();
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

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

 

    public boolean isBlockedUpCard() {
        return blockedUpCard;
    }

    public void setBlockedUpCard(boolean blockedUpCard) {
        this.blockedUpCard = blockedUpCard;
    }

}
