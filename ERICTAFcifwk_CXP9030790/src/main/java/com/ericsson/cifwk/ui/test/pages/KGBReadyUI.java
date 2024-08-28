package com.ericsson.cifwk.ui.test.pages;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.*;
import com.ericsson.cloudPortal.rest.test.cases.TestGetRARestCommand;

public class KGBReadyUI extends GenericViewModel {
    
    Logger logger = Logger.getLogger(TestGetRARestCommand.class);
    private String product;
    private String release;
    private String version;
    private String productSetRA;
    private String productSetRelease;
    
    
    public KGBReadyUI() {
        super();
        // TODO Auto-generated constructor stub
    }


    public KGBReadyUI(String product, String release, String version) {
        this.product=product;
        this.release=version;
        this.release=release;
    }
    
    public KGBReadyUI(String productSetRA, String productSetRelease) {
        this.productSetRA=productSetRA;
        this.productSetRelease=productSetRelease;
    }

    public void setProductSetRA(String productSetRA) {
        this.productSetRA = productSetRA;
    }

    public void setProductSetRelease(String productSetRelease) {
        this.productSetRelease = productSetRelease;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    private final String finalProduct = this.product;
    private final String finalRelease = this.release;
    private final String finalVersion = this.version;
    
    private final String kgbReadyISOClickString = finalProduct + "_" + finalRelease + "_" + finalVersion + "_KGBreadyISO";
    private final String kgbReadyISOContentString = finalProduct + "_" + finalRelease + "_" + finalVersion + "_ISOContent";
    private final String kgbReadyISOLinkString = finalProduct + "_" + finalRelease + "_" + finalVersion + "_ISOLink";
    
    private String result = "Success";
    private int localUIPause = 10000;
   
    @UiComponentMapping(id="product_release-table")
    private Select productReleaseTable;
    
    @UiComponentMapping(id="release-table")
    private Select releaseTable;
    
    @UiComponentMapping(id="TOR_3.0.S_3.17.21_KGBreadyISO")
    private Select kgbReadyISOClick;
    
    @UiComponentMapping(id="TOR_3.0.S_3.17.21_ISOLink")
    private Link ISOLink;
    
    @UiComponentMapping(id="iso-table")
    private Select ISOTable;
    
    @UiComponentMapping(id="iso-content-table")
    private Select ISOContentTable;
    
    @UiComponentMapping(id="passed_manual")
    private Select passedMaually;
    
    @UiComponentMapping(id="TOR_3.0.S_isoClick")
    private Link isoMainPageLink;
    
    @UiComponentMapping(id="TOR_3.0.S_3.17.21_ISOContent")
    private Link mediaContent;
    
    
    public String verifyKGBReadyISO(BrowserTab browserTab, long timeOut) {
        
        browserTab.waitUntilComponentIsDisplayed(productReleaseTable, timeOut);
        kgbReadyISOClick.click();
        UI.pause(localUIPause);
        browserTab.waitUntilComponentIsDisplayed(productReleaseTable, timeOut);
        browserTab.back();
        UI.pause(localUIPause);
        ISOLink.click();
        UI.pause(localUIPause);
        browserTab.waitUntilComponentIsDisplayed(ISOContentTable, timeOut); 
        kgbReadyISOClick.click();
        UI.pause(localUIPause);
        browserTab.waitUntilComponentIsDisplayed(productReleaseTable, timeOut);
        browserTab.back();
        UI.pause(localUIPause);
        browserTab.waitUntilComponentIsDisplayed(ISOContentTable, timeOut);
        
        if(!passedMaually.exists()){
            return "Failure";
        }
        
        browserTab.back();
        UI.pause(localUIPause);
        browserTab.waitUntilComponentIsDisplayed(productReleaseTable, timeOut);
        isoMainPageLink.click();
        UI.pause(localUIPause);
        browserTab.waitUntilComponentIsDisplayed(ISOTable, timeOut);
        mediaContent.click();
        UI.pause(localUIPause);
        browserTab.waitUntilComponentIsDisplayed(ISOContentTable, timeOut);

        return result;
    }
    
    
    private final String finalProductSetRA = this.productSetRA;
    private final String finalProductSetRelease = this.productSetRelease;
    
    private final String productSetClick = "productSet_" + finalProductSetRA;
    private final String productSetReleaseClick = "productSet_" + finalProductSetRelease + "_IconLink";
    
    @UiComponentMapping(id="prodset_link")
    private Select productSet;
    
    @UiComponentMapping(id="productSet_OSS-RC")
    private Select productSetLink;
    
    @UiComponentMapping(id="OSS-RC_15.0.3_IconLink")
    private Select statusLink;
    
    @UiComponentMapping(id="productSet-content")
    private Select productSetContent;
    
    @UiComponentMapping(id="KGB-Ready_KGBProductSetreadyISO")
    private Select kgbReadyProductSetStatus;
    
    public String verifyKGBReadyProductSet(BrowserTab browserTab, long timeOut) {
        
        browserTab.waitUntilComponentIsDisplayed(productReleaseTable, timeOut);
        productSet.click();
        UI.pause(localUIPause);
        browserTab.waitUntilComponentIsDisplayed(productReleaseTable, timeOut);
        productSetLink.click();
        UI.pause(localUIPause);
        browserTab.waitUntilComponentIsDisplayed(releaseTable, timeOut);
        statusLink.click();
        UI.pause(localUIPause);
        browserTab.waitUntilComponentIsDisplayed(productSetContent, timeOut);
        kgbReadyProductSetStatus.click();
        UI.pause(localUIPause);
        browserTab.waitUntilComponentIsDisplayed(productReleaseTable, timeOut);
        browserTab.back();
        browserTab.waitUntilComponentIsDisplayed(productSetContent, timeOut);
        UI.pause(localUIPause);
        if(!passedMaually.exists()){
            return "Failure";
        }
        return result;
    }
}

