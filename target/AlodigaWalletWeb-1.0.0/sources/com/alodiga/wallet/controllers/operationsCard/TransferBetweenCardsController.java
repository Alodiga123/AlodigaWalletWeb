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

import com.alodiga.wallet.beans.LoginBean;
import com.ericsson.alodiga.ws.Usuario;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.CardResponse;
import com.alodiga.wallet.ws.ProductResponse;
import com.alodiga.wallet.common.exception.KeyLongException;
import com.alodiga.wallet.common.utils.S3cur1ty3Cryt3r;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.RespuestaCodigoRandom;
import com.ericsson.alodiga.ws.RespuestaUsuario;
import java.security.NoSuchAlgorithmException;
import javax.faces.bean.ManagedProperty;
import javax.servlet.http.HttpSession;
import java.util.ResourceBundle;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 *
 * @author Jesús Gómez
 */
@ManagedBean(name = "transferBetweenCardsController")
@ViewScoped
public class TransferBetweenCardsController {

    private String sourceCard;
    private String destinationCard;
    private Float transferAmount;
    private String concept;
    private String emailDestinationCard;
    private String keyOperations;
    private String cardHolder;
    private String numberPhone;
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

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
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

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
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

    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }
    
  
    
}
