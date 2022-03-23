package com.alodiga.wallet.convert;

import com.alodiga.wallet.controllers.transferWallet.*;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

@ManagedBean(name = "converterController")
@ViewScoped
public class converterController {

    private Long respuestaListadoProductoId;

    private static ProductEJB productEJBProxy;
    private HttpSession session;
    private Usuario user;
    private ResourceBundle msg;
    private String idTranstaction;
    private Boolean includeComission;
    private Product selectedProduct;
    private Product selectedProductDestinate;
    private List<Product> productList = new ArrayList();
    private List<RespuestaListadoProducto> listadoProductos = new ArrayList<RespuestaListadoProducto>();
    private Float mount;
    private Float amountCommission;
    private Float valueCommission;
    private Float totalDebit;
    private Float amountConversion;
    private Float exchangeRateProductSource;
    private Float exchangeRateProductDestination;
    private int isPercentCommision;
    private String percentComission;
    private String keyOperations;
    private String date;

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
        converterController.productEJBProxy = productEJBProxy;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public Product getSelectedProductDestinate() {
        return selectedProductDestinate;
    }

    public void setSelectedProductDestinate(Product selectedProductDestinate) {
        this.selectedProductDestinate = selectedProductDestinate;
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

    public Float getMount() {
        return mount;
    }

    public void setMount(Float mount) {
        this.mount = mount;
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

    public Boolean getIncludeComission() {
        return includeComission;
    }

    public void setIncludeComission(Boolean includeComission) {
        this.includeComission = includeComission;
    }

    public Float getAmountCommission() {
        return amountCommission;
    }

    public void setAmountCommission(Float amountCommission) {
        this.amountCommission = amountCommission;
    }

    public Float getValueCommission() {
        return valueCommission;
    }

    public void setValueCommission(Float valueCommission) {
        this.valueCommission = valueCommission;
    }

    public Float getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(Float totalDebit) {
        this.totalDebit = totalDebit;
    }

    public Float getAmountConversion() {
        return amountConversion;
    }

    public void setAmountConversion(Float amountConversion) {
        this.amountConversion = amountConversion;
    }

    public Float getExchangeRateProductSource() {
        return exchangeRateProductSource;
    }

    public void setExchangeRateProductSource(Float exchangeRateProductSource) {
        this.exchangeRateProductSource = exchangeRateProductSource;
    }

    public Float getExchangeRateProductDestination() {
        return exchangeRateProductDestination;
    }

    public void setExchangeRateProductDestination(Float exchangeRateProductDestination) {
        this.exchangeRateProductDestination = exchangeRateProductDestination;
    }

    public int getIsPercentCommision() {
        return isPercentCommision;
    }

    public void setIsPercentCommision(int isPercentCommision) {
        this.isPercentCommision = isPercentCommision;
    }

    public String getPercentComission() {
        return percentComission;
    }

    public void setPercentComission(String percentComission) {
        this.percentComission = percentComission;
    }

    public String getKeyOperations() {
        return keyOperations;
    }

    public void setKeyOperations(String keyOperations) {
        this.keyOperations = keyOperations;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
        APIAlodigaWalletProxy walletProxy = new APIAlodigaWalletProxy();
        TransactionResponse transactionResponse = new TransactionResponse();
        RespuestaUsuario respUser = new RespuestaUsuario();
        APIRegistroUnificadoProxy unificadoProxy = new APIRegistroUnificadoProxy();
        switch (event.getOldStep()) {
            case "convertProduct":
                int comission = 0;
                Short num = new Short("1");
                try {
                    if (includeComission) {
                        comission = 1;
                        percentComission = "SI";
                    }else{
                        percentComission = "NO";
                    }

                    if (selectedProduct.getId().equals(selectedProductDestinate.getId())) {
                        addMessage(FacesMessage.SEVERITY_ERROR, "No se puede convertir el mismo producto", "");
                        return "convertProduct";
                    } else {
                        transactionResponse = walletProxy.previewExchangeProduct(user.getEmail(), selectedProduct.getId(), selectedProductDestinate.getId(), mount, comission);

                        if (transactionResponse.getCodigoRespuesta().equals("00")) {                      

                            amountCommission = transactionResponse.getAmountCommission();
                            valueCommission = transactionResponse.getValueCommission();
                            totalDebit = transactionResponse.getTotalDebit();
                            amountConversion = transactionResponse.getAmountConversion();
                            exchangeRateProductSource = transactionResponse.getExchangeRateProductSource();
                            exchangeRateProductDestination = transactionResponse.getExchangeRateProductDestination();
                        }
                    }

                } catch (Exception e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "No se puede hacer la conversión", "");
                    return "convertProduct";
                }

                break;
            case "key": {
                comission = 0;
                try {
                    String pass = S3cur1ty3Cryt3r.aloDesencript(keyOperations, "1nt3r4xt3l3ph0ny", null, "DESede", "0123456789ABCDEF");
                    respUser = unificadoProxy.validarPin("usuarioWS", "passwordWS", user.getUsuarioID(), pass);

                    if (respUser.getCodigoRespuesta().equals("00")) {
                        System.out.println("paso validacion pin" + respUser.getCodigoRespuesta());
                        if (includeComission) {
                            comission = 1;
                        }
                        transactionResponse = walletProxy.exchangeProduct(user.getEmail(), selectedProduct.getId(), selectedProductDestinate.getId(), mount, "", comission);
                        idTranstaction = transactionResponse.getIdTransaction();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                        Calendar calendar = Calendar.getInstance();
                        Date dateF =  calendar.getTime();                       
                        date = sdf.format(dateF);
                        addMessage(FacesMessage.SEVERITY_INFO, "Conversión satisfactoria", "");
                    } else {
                        addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");
                        return "convertProduct";
                    }

                } catch (NoSuchAlgorithmException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");
                    return "convertProduct";
                } catch (IllegalBlockSizeException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");
                    return "convertProduct";
                } catch (NoSuchPaddingException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");
                    return "convertProduct";
                } catch (BadPaddingException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");
                    return "convertProduct";
                } catch (KeyLongException e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");
                    return "convertProduct";
                } catch (Exception e) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Error Validar", "No se pudo validar el pin");
                    return "convertProduct";
                }

            }

            break;
            case "confirmation": {
                

            }
            
        }
        return event.getNewStep();

    }

//    public void submit() {
//       System.out.println("Entre");
//        if (numberAccountBank != null) {
//            try {
//               //Obtener el estatus ACTIVA de la cuenta bancaria
//               StatusAccountBank statusAccountBankActiva = businessPortalEJBProxy.loadStatusAccountBankById(StatusAccountBankE.ACTIVA.getId());
//               
//               //Crear el objeto Bank
//               Bank bank = new Bank();
//               bank.setAbaCode(selectedBank.getAbaCode());
//               bank.setCountryId(selectedCountry);
//               bank.setId(selectedBank.getId());
//               bank.setName(selectedBank.getName());
//               bank.setSwiftCode(selectedBank.getSwiftCode());
//               
//               //Creando el objeto AccountBank
//               AccountBank accountBank = new AccountBank();
//               accountBank.setBankId(bank);
//               accountBank.setAccountNumber(numberAccountBank);
//               accountBank.setAccountTypeBankId(selectedAccountTypeBank);
//               accountBank.setStatusAccountBankId(statusAccountBankActiva);
//               accountBank.setCreateDate(new Timestamp(new Date().getTime()));
//               accountBank.setUnifiedRegistryId(user.getUsuarioID());
//               //Guardar la cuenta bancaria en la BD
//               accountBank = businessPortalEJBProxy.saveAccountBank(accountBank);
//
//               if (accountBank != null) {
//                  FacesContext context = FacesContext.getCurrentInstance();
//                  context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Las respuestas del usuario se guardaron correctamente en la BD", null));
//               }else{
//                  FacesContext context = FacesContext.getCurrentInstance();
//                  context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Se presentó un problema al guardar los datos, por favor intente de nuevo", null));  
//               }
//            
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                Logger.getLogger(RechargeCardController.class.getName()).log(Level.SEVERE, null, ex);      
//            }
//
//   }
//  }
}
