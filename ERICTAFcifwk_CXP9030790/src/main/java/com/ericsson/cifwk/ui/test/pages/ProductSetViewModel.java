package com.ericsson.cifwk.ui.test.pages;

import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.*;

public class ProductSetViewModel extends GenericViewModel {

    private Link link;

    @UiComponentMapping(id="generic-title")
    private Label title;

    @UiComponentMapping(id="prodset_link")
    private Link prodset_link;

    @UiComponentMapping(id="productSet_psTest_header")
    private Link productSet_psTest_header;

    @UiComponentMapping(id="productSetRelease_psRelname_header")
    private Link productSetRelease_psRelname_header;

    public String checkProductSetSummary(BrowserTab browserTab,
            String tagName, long timeOut) {
        link = this.getLink("#" + tagName);

        //Get to the Product Set Page
        prodset_link.click();
        UI.pause(2500);

        //Click on any further id/tagnames that have been supplied
        if (link.exists()){
            link.click();
            UI.pause(2500);
        }
        return browserTab.waitUntilComponentIsDisplayed(title, timeOut).getText();
    }

    public String checkProductSetRelease(BrowserTab browserTab,
            String psRelease, String tagName, long timeOut) {
        link = this.getLink("#" + tagName);
        Link psRel = this.getLink("#" + psRelease);

        //Get to the Product Set Page
        prodset_link.click();
        UI.pause(2500);

        productSet_psTest_header.click();
        UI.pause(2500);

        if (psRel.exists()){
            psRel.click();
            UI.pause(2500);
            productSetRelease_psRelname_header.click();
        }
        UI.pause(2500);
        //Click on any further id/tagnames that have been supplied
        if (link.exists()){
            link.click();
            UI.pause(2500);
        }
        UI.pause(2500);
        return browserTab.waitUntilComponentIsDisplayed(title, timeOut).getText();
    }
}