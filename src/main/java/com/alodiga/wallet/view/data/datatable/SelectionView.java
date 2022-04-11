/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alodiga.wallet.view.data.datatable;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import com.alodiga.wallet.domain.Car;
import com.alodiga.wallet.service.CarService;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.Maw_transaction;
import com.alodiga.wallet.ws.TransactionListResponse;
import com.ericsson.alodiga.ws.RespuestaListadoProducto;
import com.ericsson.alodiga.ws.Usuario;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean(name="dtSelectionView")
@ViewScoped
public class SelectionView implements Serializable {

    
    private List<Maw_transaction> maw_transactions;
    private Usuario user;
    private String userName;
    private List<RespuestaListadoProducto> respuestaListadoProductos;
    

    @PostConstruct
    public void init() {
        try {
            
            APIAlodigaWalletProxy proxy = new APIAlodigaWalletProxy();
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpSession session = request.getSession(false);
            Object userId= session.getAttribute("userId");
            user = (Usuario) session.getAttribute("user");
            userName = user.getNombre().concat(user.getApellido());
            TransactionListResponse transactionListResponse = proxy.getTransactionsByUserIdApp(userId.toString(), "50");
            maw_transactions = Arrays.asList(transactionListResponse.getTransactions());
            respuestaListadoProductos = Arrays.asList(user.getRespuestaListadoProductos());
            
        } catch (RemoteException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "No se pudo obtener la lista",
                                "Intente nuevamente"));
        } catch (Exception ex) {
            
        }
    }


    public List<Maw_transaction> getMaw_transactions() {
        return maw_transactions;
    }

    public void setMaw_transactions(List<Maw_transaction> maw_transactions) {
        this.maw_transactions = maw_transactions;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<RespuestaListadoProducto> getRespuestaListadoProductos() {
        return respuestaListadoProductos;
    }

    public void setRespuestaListadoProductos(List<RespuestaListadoProducto> respuestaListadoProductos) {
        this.respuestaListadoProductos = respuestaListadoProductos;
    }
    
    
    public String logout() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "login.xhtml";
    }
    
    
    
    
    
}

