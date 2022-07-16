/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.beans;

import com.alodiga.wallet.common.utils.S3cur1ty3Cryt3r;
import com.alodiga.wallet.parent.GenericController;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.Respuesta;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.Serializable;
import java.util.Hashtable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "passwordRecoverBean")
@SessionScoped
public class PasswordRecoverBean extends GenericController implements Serializable {

    private static final long serialVersionUID = 1L;
    private String password;
    private String confirmPassword;
    private String locale;

    @PostConstruct
    public void init() {
        //userData = new UserData();
        locale = "es";

    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String recover() {
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        Respuesta respuesta = new Respuesta();  
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        try {

            String pass = S3cur1ty3Cryt3r.aloDesencript(password, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
            Object phone = session.getAttribute("phoneOrEmail");
            respuesta = proxy.cambiarCredencialAplicacionMovilEmailOrPhone("usuarioWS", "passwordWS", phone.toString(), pass);
            if (respuesta.getCodigoRespuesta().equals("00")) {
                session.setAttribute("user", "");
                //context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cambio de contraseña correctamente","" ));
                addMessage(FacesMessage.SEVERITY_INFO, "Cambio de contraseña correctamente", "");                    
                return "login.xhtml";
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error al cambiar la contraseña", "");                    
                //context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error al cambiar la contraseña", ""));
                return "recover.xhtml";
            }

        } catch (Exception ex) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error al cambiar la contraseña", "");                    
            //context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error al cambiar la contraseña", ""));
            return "recover.xhtml";
        }

    }

    
    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }
    public String cancel() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "login.xhtml";
    }

    public static Boolean IsNumberPhone(String value) {
        try {
            return (value.matches("[+]?\\d*") && value.equals("") == false);
        } catch (NumberFormatException e) {

            return false;
        }

    }
}
