/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.beans;
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

@ManagedBean(name = "tokenRecoverBean")
@SessionScoped
public class TokenRecoverBean extends GenericController implements Serializable {

    
    private static final long serialVersionUID = 1L;
    private String token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }  
    
    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }
    
    public String recover() {
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        Respuesta respuesta = new Respuesta();
        Boolean email = false;
        FacesContext context = FacesContext.getCurrentInstance();
        try {
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                    HttpSession session = request.getSession(false);
                    Object o =  session.getAttribute("token");                    
                    o.equals(token);
                    session.setAttribute("user", "");          
                return "passwordRecover.xhtml";           
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error al recuperar la contraseña", ""));
            return "recover.xhtml";
        } 
    }

    
    public String cancel() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "login.xhtml";
    }
    
    public static Boolean IsNumberPhone(String value) {
        try {    
            return (value.matches("[+]?\\d*") && value.equals("")==false);
        } catch (NumberFormatException e) {
                
                return false;
        }
   
    }
}