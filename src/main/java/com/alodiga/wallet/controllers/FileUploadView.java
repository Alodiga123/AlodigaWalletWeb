/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.controllers;

import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ltoro
 */
public class FileUploadView {

    private UploadedFile documentIden;
    private UploadedFile profile;
    private String dropZoneText = "Drop zone p:inputTextarea demo.";

    public void upload() {

        APIAlodigaWalletProxy proxy = new APIAlodigaWalletProxy();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        try {
            Object userId = session.getAttribute("userId");
            Object countryId = session.getAttribute("countryId");
            Object zipCode = session.getAttribute("zipCode");
            proxy.saveAffiliationRequestUserWallet(userId.toString(), Long.valueOf(countryId.toString()), zipCode.toString(), null, null, null, null);
            if (documentIden != null) {
                FacesMessage message = new FacesMessage("Successful", documentIden.getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(FileUploadView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Successful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

//    public void handleFileUploadTextarea(FileUploadEvent event) {
//        String jsVal = "PF('textarea').jq.val";
//        String fileName = EscapeUtils.forJavaScript(event.getFile().getFileName());
//        PrimeFaces.current().executeScript(jsVal + "(" + jsVal + "() + '\\n\\n" + fileName + " uploaded.')");
//    }
//
//    public void handleFilesUpload(FilesUploadEvent event) {
//        for (UploadedFile f : event.getFiles().getFiles()) {
//            FacesMessage message = new FacesMessage("Successful", f.getFileName() + " is uploaded.");
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
//    }

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
    

    public String getDropZoneText() {
        return dropZoneText;
    }

    public void setDropZoneText(String dropZoneText) {
        this.dropZoneText = dropZoneText;
    }

}
