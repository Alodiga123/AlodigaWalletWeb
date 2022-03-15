/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.beans;
import com.alodiga.wallet.parent.GenericController;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.Respuesta;
import com.ericsson.alodiga.ws.RespuestaCodigoRandom;
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

@ManagedBean(name = "recoverBean")
@SessionScoped
public class RecoverBean extends GenericController implements Serializable {

    
    private static final long serialVersionUID = 1L;
    private String emailAddress;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

 


   
            
    public void validateEmail() {
        String email = getEmailAddress();
        if (!isValid(email)) {
             FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                    getStandarMessage("invalidFielAddress"),
                    getStandarMessage("invalidEmailAddress")));
 
        }
    }
 
    boolean isValid(String email) {
        // Reqular expression pattern to validate the format submitted
        String validator = "^[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+"
                + "(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,})$";
        if (!email.matches(validator)) {
            return false;
        }
        // Split the user and the domain name
        String[] parts = email.split("@");
 
        boolean retval=true;
        // This is similar to nslookup –q=mx domain_name.com to query
        // the mail exchanger of the domain.
        try {
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put("java.naming.factory.initial",
                    "com.sun.jndi.dns.DnsContextFactory");
            DirContext context = new InitialDirContext(env);
            Attributes attributes =
                    context.getAttributes(parts[1], new String[]{"MX"});
            Attribute attribute = attributes.get("MX");
            if (attribute.size() == 0) {
               retval=false;
            }
            context.close();
            return retval;
 
        } catch (Exception exception) {
            return false;
        }        
    }
    
    public String recover() {
        APIRegistroUnificadoProxy proxy = new APIRegistroUnificadoProxy();
        RespuestaCodigoRandom respuesta = new RespuestaCodigoRandom();
        Boolean email = false;
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            email = isValid(emailAddress);
            System.out.println("boolean email " + email);
            if (email) {
              respuesta = proxy.generarCodigoMovilSMSAplicacionMovil("usuarioWS", "passwordWS", null, emailAddress);  
            }else{
                respuesta = proxy.generarCodigoMovilSMSAplicacionMovil("usuarioWS", "passwordWS", emailAddress, null);
            }     
            System.out.println("respuesta.getCodigoRespuesta() " + respuesta.getCodigoRespuesta());
            if (respuesta.getCodigoRespuesta().equals("00")) {
                System.out.println("token " + respuesta.getDatosRespuesta());
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                    HttpSession session = request.getSession(false);                    
                    session.setAttribute("token", respuesta.getDatosRespuesta());
                    session.setAttribute("phoneOrEmail", emailAddress);
                    session.setAttribute("user", "");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha enviado una clave temporal", ""));                
                return "tokenRecover.xhtml";
            }else{
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error al recuperar la contraseña", ""));

                return "recover.xhtml"; 
            }
            
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