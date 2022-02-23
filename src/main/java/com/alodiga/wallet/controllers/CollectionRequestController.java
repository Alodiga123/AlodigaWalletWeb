package com.alodiga.wallet.controllers;

import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.exception.EmptyListException;
import com.alodiga.wallet.common.model.AffiliationRequest;
import com.alodiga.wallet.common.model.CollectionsRequest;
import com.alodiga.wallet.common.model.RequestHasCollectionRequest;
import com.alodiga.wallet.common.utils.EjbConstants;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author henry
 */
@ManagedBean(name = "collectionRequestController")
@ViewScoped
public class CollectionRequestController {

    private final static String FOLDER_PATH = "/opt/alodiga/proyecto/maw/imagenes/";
    //private final static String FOLDER_PATH = "c:\\Temp\\";

    private Map<Integer, String> collectionImgMap = new HashMap();
    private Map<Integer, UploadedFile> collectionFiles = new HashMap();

    private List<CollectionsRequest> collectionRequests;
    private Map<Integer, CollectionsRequest> collectionsRequestMap = new HashMap();

    private AffiliationRequest affiliationRequest;

    private static BusinessPortalEJB proxy;

    private boolean isSaved = false;

    @PostConstruct
    public void init() {
        try {
            Properties props = new Properties();
            props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
            props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
            props.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
            props.setProperty("org.omg.CORBA.ORBInitialHost", "alodiga.wallet.admin");
            props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
            InitialContext intialContext = new InitialContext(props);
            proxy = (BusinessPortalEJB) intialContext.lookup(EjbConstants.BUSINESS_PORTAL_EJB);
            String affilitiaonId = FacesContext.getCurrentInstance().
                    getExternalContext().getRequestParameterMap().get("i");
            affiliationRequest = proxy.loadAffiliationRequestById(Long.parseLong(affilitiaonId));
            collectionRequests = proxy.getCollectionRequestsByPersonTypeId(affiliationRequest.getBusinessPersonId().getPersonTypeId().getId().longValue());
            for (CollectionsRequest collection : collectionRequests) {

                collectionsRequestMap.put(collection.getId(), collection);
                collectionFiles.put(collection.getId(), null);
                collectionImgMap.put(collection.getId(), null);
            }
        } catch (EmptyListException ex) {
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                FacesContext.getCurrentInstance().getExternalContext().redirect("register.xhtml");
            } catch (IOException ex1) {
                Logger.getLogger(CollectionRequestController.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }

    }

    public Map<Integer, String> getCollectionImgMap() {
        return collectionImgMap;
    }

    public void setCollectionImgMap(Map<Integer, String> collectionImgMap) {
        this.collectionImgMap = collectionImgMap;
    }

    public boolean isCollectionSubmited(Integer idCollection) {
        return collectionImgMap.containsKey(idCollection) && collectionImgMap.get(idCollection) != null;
    }

    public Map<Integer, UploadedFile> getCollectionFiles() {
        return collectionFiles;
    }

    public void setCollectionFiles(Map<Integer, UploadedFile> collectionFiles) {
        this.collectionFiles = collectionFiles;
    }

    public List<CollectionsRequest> getCollectionRequests() {
        return collectionRequests;
    }

    public void setCollectionRequests(List<CollectionsRequest> collectionRequests) {
        this.collectionRequests = collectionRequests;
    }

    public void uploadFile(Integer idCollection) {
        collectionImgMap.put(idCollection, null);
        UploadedFile file = collectionFiles.get(idCollection);
        String filename = file.getFileName();

        String extension = file.getContentType();

        long size = file.getSize();

        try {
            Path folder = Paths.get(FOLDER_PATH);
            Path newFile = Files.createTempFile(folder, filename, extension);
            InputStream input = file.getInputstream();
            Files.copy(input, newFile, StandardCopyOption.REPLACE_EXISTING);
            collectionImgMap.put(idCollection, newFile.toFile().getName());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean showNavBars() {
        if (collectionImgMap.size() <= 0) {
            return false;
        }
        for (String img : collectionImgMap.values()) {
            if (img == null) {
                return false;
            }
        }
        return true;
    }

    public void fileUploadListener(FileUploadEvent e) {
        UploadedFile file = e.getFile();
        try {
            int index = Integer.parseInt(e.getComponent().getId().substring(4));
            collectionFiles.put(index, file);
            String filename = file.getFileName();

            String extension = "";
            int indexExtension = filename.lastIndexOf('.');
            if (indexExtension > 0) {
                extension = filename.substring(index + 1);
                filename = filename.substring(0, indexExtension);
            }

            long size = file.getSize();

            Path folder = Paths.get(FOLDER_PATH);
            Path newFile = Files.createTempFile(folder, filename, extension);
            InputStream input = file.getInputstream();
            Files.copy(input, newFile, StandardCopyOption.REPLACE_EXISTING);
            collectionImgMap.put(index, newFile.toString());
            /*RequestHasCollectionRequest rhcr = new RequestHasCollectionRequest();
            rhcr.setCollectionsRequestId(collectionsRequestMap.get(index));
            rhcr.setCreateDate(new Date());
            rhcr.setImageFileUrl(collectionImgMap.get(index));
            rhcr.setAffiliationRequestId(affiliationRequest);
            proxy.saveRequestHasCollectionsRequest(rhcr);*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public String onFlowProcess(FlowEvent event) {
        switch (event.getOldStep()) {
            case "collectionRequest":
                if (!isSaved) {
                    for (Integer index : collectionImgMap.keySet()) {
                        try {
                            RequestHasCollectionRequest rhcr = new RequestHasCollectionRequest();
                            rhcr.setCollectionsRequestId(collectionsRequestMap.get(index));
                            rhcr.setCreateDate(new Date());
                            rhcr.setImageFileUrl(collectionImgMap.get(index));
                            rhcr.setAffiliationRequestId(affiliationRequest);
                            proxy.saveRequestHasCollectionsRequest(rhcr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    isSaved = true;
                }
        }
        return event.getNewStep();
    }

}
