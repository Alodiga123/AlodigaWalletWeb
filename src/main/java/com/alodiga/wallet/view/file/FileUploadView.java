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
package com.alodiga.wallet.view.file;

import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.AffiliationRequestResponse;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
public class FileUploadView {

    private UploadedFile documentIden;
    private UploadedFile profile;

    public void upload() {

        APIAlodigaWalletProxy proxy = new APIAlodigaWalletProxy();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        try {
            Object userId = session.getAttribute("userId");
            Object countryId = session.getAttribute("countryId");
            Object zipCode = session.getAttribute("zipCode");
            System.out.println("documentIden.getFileName() " + documentIden.getFileName());
            AffiliationRequestResponse affiliationRequestResponse = proxy.saveAffiliationRequestUserWallet(userId.toString(), Long.valueOf(countryId.toString()), zipCode.toString(), null, null, documentIden.getContents(), profile.getContents());
            System.out.println("affiliationRequestResponse" + affiliationRequestResponse.getCodigoRespuesta());
            if (documentIden != null) {
                FacesMessage message = new FacesMessage("Successful", documentIden.getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(com.alodiga.wallet.controllers.FileUploadView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {

        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public UploadedFile getDocumentIden() {
        return documentIden;
    }

    public void setDocumentIden(UploadedFile documentIden) {
        this.documentIden = documentIden;
    }

    public UploadedFile getProfile() {
        return profile;
    }

    public void setProfile(UploadedFile profile) {
        this.profile = profile;
    }

   

}
