package com.alodiga.wallet.controllers.qr;


import com.alodiga.wallet.common.exception.EmptyListException;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.utils.AlodigaCryptographyUtils;
import com.ericsson.alodiga.ws.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author hvarona
 */
@ManagedBean(name = "viewqrController")
@ViewScoped
public class ViewQrController {

 private HttpSession session;
    private Usuario user;
    private String qrtext;
    private String apellido;

   

    private boolean showStore = false;
    private boolean showPos = false;

    @PostConstruct
    public void init() {
        try {
            session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            user = (Usuario) session.getAttribute("user");            
            
            String toEncrypt = user.getApellido() +";"+ user.getNombre();
            //this.qrtext = AlodigaCryptographyUtils.encrypt(toEncrypt, "1nt3r4xt3l3ph0ny");            
            this.qrtext = user.getApellido() +";"+ user.getNombre();
            
            System.out.println("qr " + qrtext.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getQrtext() {
        return qrtext;
    }

    public void setQrtext(String qrtext) {
        this.qrtext = qrtext;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    

    

}
