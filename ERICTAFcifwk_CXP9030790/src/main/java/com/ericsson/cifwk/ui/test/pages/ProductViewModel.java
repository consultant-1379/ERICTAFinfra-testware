package com.ericsson.cifwk.ui.test.pages;

import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.*;

public class ProductViewModel extends GenericViewModel {

    private Link link;

    @UiComponentMapping(id="generic-title")
    private Label title;

    @UiComponentMapping(id="product_link")
    private Link product_link;

    @UiComponentMapping(id="product_test_header")
    private Link product_header_link;

    @UiComponentMapping(id="productRelease_testrelname_header")
    private Link productRelease_testrelname_header;

    public String checkProductSummary(BrowserTab browserTab,
            String tagName, long timeOut) {
        link = this.getLink("#" + tagName);

        //Get to the Product Set Page
        product_link.click();
        UI.pause(2500);

        //Click on any further id/tagnames that have been supplied
        if (link.exists()){
            link.click();
            UI.pause(2500);
        }
        return browserTab.waitUntilComponentIsDisplayed(title, timeOut).getText();
    }

    public String checkProductRelease(BrowserTab browserTab,
            String prodRelease, String tagName, long timeOut) {
        link = this.getLink("#" + tagName);
        Link prodRel = this.getLink("#" + prodRelease);

        //Get to the Product Set Page
        product_link.click();
        UI.pause(2500);

        product_header_link.click();
        UI.pause(2500);

        if (prodRel.exists()){
            prodRel.click();
            UI.pause(2500);
            productRelease_testrelname_header.click();
            UI.pause(2500);
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