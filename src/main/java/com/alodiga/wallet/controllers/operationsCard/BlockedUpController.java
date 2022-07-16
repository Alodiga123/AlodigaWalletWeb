package com.alodiga.wallet.controllers.operationsCard;


import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.CardResponse;
import com.alodiga.wallet.common.enumeraciones.DocumentTypeE;
import com.alodiga.wallet.common.enumeraciones.OriginAplicationE;
import com.alodiga.wallet.ws.TransactionApproveRequestResponse;
import com.ericsson.alodiga.ws.Usuario;
import static com.sun.faces.facelets.util.Path.context;
import java.rmi.RemoteException;
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


@ManagedBean(name = "blockedUpController")
@ViewScoped
public class BlockedUpController {


    private HttpSession session;
    private Usuario user;
    public String cardNumber = "";
    private CardResponse cardResponseWallet;
    private APIAlodigaWalletProxy apiAlodigaWalletProxy;
    private boolean activeCard;
    private boolean blockedUpCard;
    private ResourceBundle msg;
    private boolean radioBlocked;

    @PostConstruct
    public void init() {
        try {
           

        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(BlockedUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public ResourceBundle getMsg() {
        return msg;
    }

    public void setMsg(ResourceBundle msg) {
        this.msg = msg;
    }

    public boolean isRadioBlocked() {
        return radioBlocked;
    }

    public void setRadioBlocked(boolean radioBlocked) {
        this.radioBlocked = radioBlocked;
    }

    public boolean verifyCard() {
        activeCard = true;
     

        return activeCard;
    }
     public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
       FacesContext.getCurrentInstance().
               addMessage(null, new FacesMessage(severity, summary, detail));
   }

    public String changeBlocked() {
        FacesContext context = FacesContext.getCurrentInstance();
        Long messageMiddlewareId = 1L;
        Long transactioExternalId = 1L;
        
        //Se bloquea o desbloquea la tarjeta
        if (radioBlocked == true) {
            
        } else {
            return "data.xhtml?faces-redirect=true";
        }
        return null;
      }
         
    }
