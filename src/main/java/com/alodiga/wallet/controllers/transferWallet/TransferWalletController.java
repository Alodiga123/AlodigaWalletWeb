package com.alodiga.wallet.controllers.transferWallet;

import com.alodiga.wallet.common.ejb.ProductEJB;
import com.alodiga.wallet.common.exception.KeyLongException;
import com.alodiga.wallet.controllers.operationsCard.*;

import com.alodiga.wallet.common.model.Product;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import com.alodiga.wallet.common.utils.EjbConstants;
import com.alodiga.wallet.common.utils.S3cur1ty3Cryt3r;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.TransactionResponse;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.RespuestaCodigoRandom;
import com.ericsson.alodiga.ws.RespuestaListadoProducto;
import com.ericsson.alodiga.ws.RespuestaUsuario;
import com.ericsson.alodiga.ws.Usuario;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FlowEvent;

@ManagedBean(name = "transferWalletController")
@ViewScoped
public class TransferWalletController {

    private Product selectedProduct;

    private HttpSession session;
    private Usuario user;
    private ResourceBundle msg;

    private Long respuestaListadoProductoId;
    private List<Product> productList = new ArrayList();
    private List<RespuestaListadoProducto> listadoProductos = new ArrayList<RespuestaListadoProducto>();
    private Long productId;
    private boolean skip;
    private String email;
    private static ProductEJB productEJBProxy;
    private String firstName;
    private String lastName;
    private String numberPhone;
    private String destinate;
    private String origin;
    private Float mount;
    private String concept;
    private String keyOperations;
    private Long userDestinationId;
    private String idTranstaction;

    @PostConstruct
    public void init() {
        try {
            productEJBProxy = (ProductEJB) EJBServiceLocator.getInstance().get(EjbConstants.PRODUCT_EJB);
            msg = ResourceBundle.getBundle("com.alodiga.wallet.messages.message", Locale.forLanguageTag("es"));
            //Se obtiene el usuario de sesión
            session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            user = (Usuario) session.getAttribute("user");
//            for(int i=0 ; i<user.getRespuestaListadoProductos().length;i++){
//                listadoProductos.add((user.getRespuestaListadoProductos()[i]));
//            }

            //Se obtiene la lista de productos del usuario
            productList = productEJBProxy.getProductsByWalletUser(Long.valueOf(user.getUsuarioID()));
                    
            
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(RechargeCardController.class.getName()).log(Level.SEVERE, null, ex);
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

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public static ProductEJB getProductEJBProxy() {
        return productEJBProxy;
    }

    public static void setProductEJBProxy(ProductEJB productEJBProxy) {
        TransferWalletController.productEJBProxy = productEJBProxy;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        productId = selectedProduct.getId();
        this.selectedProduct = selectedProduct;
    }

    public ResourceBundle getMsg() {
        return msg;
    }

    public void setMsg(ResourceBundle msg) {
        this.msg = msg;
    }

    public List<RespuestaListadoProducto> getListadoProductos() {
        return listadoProductos;
    }

    public void setListadoProductos(List<RespuestaListadoProducto> listadoProductos) {

        this.listadoProductos = listadoProductos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getDestinate() {
        return destinate;
    }

    public void setDestinate(String destinate) {
        this.destinate = destinate;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Float getMount() {
        return mount;
    }

    public void setMount(Float mount) {
        this.mount = mount;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getKeyOperations() {
        return keyOperations;
    }

    public void setKeyOperations(String keyOperations) {
        this.keyOperations = keyOperations;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getUserDestinationId() {
        return userDestinationId;
    }

    public void setUserDestinationId(Long userDestinationId) {
        this.userDestinationId = userDestinationId;
    }

    public RespuestaListadoProducto getListadoProductos(long id) {
        for (RespuestaListadoProducto respuestaListadoProducto : listadoProductos) {
            if (respuestaListadoProducto.getId() == id) {
                respuestaListadoProductoId = respuestaListadoProducto.getId();
                return respuestaListadoProducto;
            }
        }
        return null;
    }

    public String getIdTranstaction() {
        return idTranstaction;
    }

    public void setIdTranstaction(String idTranstaction) {
        this.idTranstaction = idTranstaction;
    }

    public static Boolean IsNumberPhone(String value) {
        try {
            return (value.matches("[+]?\\d*") && value.equals("") == false);
        } catch (NumberFormatException e) {

            return false;
        }

    }
    
    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public String onFlowProcess(FlowEvent event) {       
        RespuestaCodigoRandom resp = new RespuestaCodigoRandom();
        RespuestaUsuario respUser = new RespuestaUsuario();
        APIRegistroUnificadoProxy unificadoProxy = new APIRegistroUnificadoProxy();
        TransactionResponse transactionResponse = null;

        switch (event.getOldStep()) {
            case "productAndEmail":        
                    try {
                if (!IsNumberPhone(email)) {
                    respUser = unificadoProxy.getUsuarioporemail("usuarioWS", "passwordWS", email);
                } else {
                    respUser = unificadoProxy.getUsuariopormovil("usuarioWS", "passwordWS", email);
                }
                System.out.println("respuesta " + respUser.getCodigoRespuesta());
                if (respUser.getCodigoRespuesta().equals("00")) {
                    firstName = respUser.getDatosRespuesta().getNombre();
                    lastName = respUser.getDatosRespuesta().getApellido();
                    numberPhone = respUser.getDatosRespuesta().getMovil();
                    destinate = respUser.getDatosRespuesta().getCuenta().getNumeroCuenta();
                    userDestinationId = Long.valueOf(respUser.getDatosRespuesta().getUsuarioID());
                } else {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Usuario", "No se encontró el usuario");                    
                    return "productAndEmail";
                }

            } catch (Exception e) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error Usuario", "No existen datos asociados al usuario");                                
                return "productAndEmail";
            }

            break;
            case "key": {
                try {
                    String pass = S3cur1ty3Cryt3r.aloDesencript(keyOperations, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
                    respUser = unificadoProxy.validarPin("usuarioWS", "passwordWS", user.getUsuarioID(), pass);

                    if (respUser.getCodigoRespuesta().equals("00")) {
                        System.out.println("paso " + respUser.getCodigoRespuesta());
                    } else {
                        addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");
                        return "key";
                    }

                } catch (NoSuchAlgorithmException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");                    
                    return "productAndEmail";
                } catch (IllegalBlockSizeException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");                    
                    return "productAndEmail";
                } catch (NoSuchPaddingException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");                    
                    return "productAndEmail";
                } catch (BadPaddingException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");                    
                    return "productAndEmail";
                } catch (KeyLongException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");                    
                    return "productAndEmail";
                } catch (Exception e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");                    
                    return "productAndEmail";
                }
            }
            break;
            case "confirmation": {
                try {
                    APIAlodigaWalletProxy walletProxy = new APIAlodigaWalletProxy();
                    transactionResponse = walletProxy.saveTransferBetweenAccount("", email, productId, mount, concept, "", userDestinationId);

                    if (transactionResponse.getCodigoRespuesta().equals("00")) {
                        System.out.println("paso " + transactionResponse.getIdTransaction());
                        idTranstaction = transactionResponse.getIdTransaction();

                    } else {
                        addMessage(FacesMessage.SEVERITY_ERROR, "Error Transferencia", "No se pudo realizar la transferencia");                    
                        return "productAndEmail";
                    }
                } catch (RemoteException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Transferencia", "No se pudo realizar la transferencia");                    
                    return "productAndEmail";
                }

            }
            break;

            case "confirmationEnd": {
                return "productAndEmail";
            }
        }
        return event.getNewStep();

    }

}
