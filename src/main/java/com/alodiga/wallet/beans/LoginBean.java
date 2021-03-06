package com.alodiga.wallet.beans;

import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.KeyLongException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.utils.S3cur1ty3Cryt3r;
import com.alodiga.wallet.parent.GenericController;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.Product;
import com.alodiga.wallet.ws.ProductListResponse;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.RespuestaUsuario;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean extends GenericController implements Serializable {

    private static final long serialVersionUID = 1L;
    private String uname;
    private String password;
//    private BPUser userSession;
    private Map<String, String> profiles = null;
//    private BPProfile profile;
//    private UserData userData = null;

    private float discountRate = 0;

    private String locale;

    private float businessPercentFee = 1;

    private Product businessProduct;

//    private Map<BusinessServiceType, TransactionCommissionResponse> comissions = new HashMap();

    @PostConstruct
    public void init() {
        //userData = new UserData();
        locale = "es";

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

//    public BPUser getUserSession() {
//        return userSession;
//    }
//
//    public void setUserSession(BPUser userSession) {
//        this.userSession = userSession;
//    }

    public float getBusinessPercentFee() {
        return businessPercentFee;
    }

    public void setBusinessPercentFee(float businessPercentFee) {
        this.businessPercentFee = businessPercentFee;
    }

    public float getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(float discountRate) {
        this.discountRate = discountRate;
    }

//    public Map<String, String> getProfiles() {
//        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        HttpSession session = request.getSession(false);
//        locale = (String) session.getAttribute("languaje");
//        Long languageId;
//        if (locale == null) {
//            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
//        }
//        if (locale.equals("en")) {
//            languageId = 1L;
//        } else {
//            languageId = 2L;
//        }
//        profiles = new TreeMap<String, String>();
//        try {
//            List<BPProfile> profile1 = userData.getProfiles();
//            for (BPProfile prof : profile1) {
//                profiles.put(prof.getProfileDataByLanguageId(languageId).getAlias(), prof.getId().toString());
//            }
//        } catch (EmptyListException | GeneralException ex) {
//            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return profiles;
//    }
//
//    public void setProfiles(Map<String, String> profiles) {
//        this.profiles = profiles;
//    }
//
//    public BPProfile getProfile() {
//        return profile;
//    }
//
//    public void setProfile(BPProfile profile) {
//        this.profile = profile;
//    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String loginProject() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaUsuario respuestaUsuario = new RespuestaUsuario();
        if (uname.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, getStandarMessage("emptyField"),
                            getStandarMessage("emptyFieldLogin")));
            RequestContext.getCurrentInstance().scrollTo("username");

            uname = null;
        } else if (password.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            getStandarMessage("emptyField"),
                            getStandarMessage("emptyFieldPassword")));
            password = null;
        } else {
            try {

                String pass = S3cur1ty3Cryt3r.aloDesencript(password, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
                respuestaUsuario = proxy.loginAplicacionMovil("usuarioWS", "passwordWS", uname, null, pass, "");
                System.out.println("codigo respuest " + respuestaUsuario.getCodigoRespuesta()) ;
                if (respuestaUsuario.getCodigoRespuesta().equals("00")) {
                    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                    HttpSession session = request.getSession(false);
                    session.setAttribute("user", respuestaUsuario.getDatosRespuesta());
                    session.setAttribute("userId", respuestaUsuario.getDatosRespuesta().getUsuarioID());
                    session.setAttribute("countryId", respuestaUsuario.getDatosRespuesta().getDireccion().getPaisId());
                    session.setAttribute("zipCode", respuestaUsuario.getDatosRespuesta().getDireccion().getCodigoPostal());
                    return "dashboard.xhtml?faces-redirect=true";
                } else {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    respuestaUsuario.getMensajeRespuesta(),
                                    "Intente nuevamente"));
                    return "login.xhtml";
                }
                

                
            } catch (RegisterNotFoundException ex) {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Usuario o password invalido",
                                "Intente nuevamente"));
                return "login.xhtml";
            } catch (NullParameterException ex) {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Usuario vacio",
                                "Intente nuevamente"));
                return "login.xhtml";
            } catch (GeneralException ex) {
                ex.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error general",
                                "Intente nuevamente"));
                return "login.xhtml";
            } catch (IllegalBlockSizeException ex) {
                ex.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error general",
                                "Intente nuevamente"));
                return "login.xhtml";
            } catch (NoSuchPaddingException ex) {
                ex.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error general",
                                "Intente nuevamente"));
                return "login.xhtml";
            } catch (BadPaddingException ex) {
                ex.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error general",
                                "Intente nuevamente"));
                return "login.xhtml";
            } catch (KeyLongException ex) {
                ex.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error general",
                                "Intente nuevamente"));
                return "login.xhtml";
            } catch (Exception ex) {
                ex.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error general",
                                "Intente nuevamente"));
                return "login.xhtml";
            }
        }

        return null;
    }

    public String logout() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "login.xhtml";
    }

//    public void rechargeDashboard(AjaxBehaviorEvent event) {
//        BPProfile p = (BPProfile) ((UIOutput) event.getSource()).getValue();
//        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        HttpSession session = request.getSession(false);
//        session.setAttribute("profile", p);
//        profile = p;
//        try {
//            FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.xhtml");
//        } catch (IOException ex) {
//            System.out.println("com.alodiga.remittance.beans.LoginBean.rechargeDashboard()");
//        }
//    }

    public Product getBusinessProduct() {
        return businessProduct;
    }

//    public Business getCurrentBusiness() {
//        if (this.userSession instanceof Business) {
//            return (Business) this.userSession;
//        } else if (this.userSession instanceof Operator) {
//            return ((Operator) this.userSession).getCommerce();
//        }
//        return null;
//    }
//
//    public Map<BusinessServiceType, TransactionCommissionResponse> getComissions() {
//        return comissions;
//    }
//
//    public TransactionCommissionResponse getComission(BusinessServiceType type) {
//        return comissions.get(type);
//    }
}
