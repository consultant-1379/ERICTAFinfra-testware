package com.ericsson.cifwk.ui.test.pages;

import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.*;

public class CIPortalPerformanceViewModel extends GenericViewModel {

    @UiComponentMapping(id = "release_menu")
    private Link relMenu_link;

    @UiComponentMapping(id = "releases_link")
    private Link releases_link;

    @UiComponentMapping(id = "refreshButton")
    private Button refreshButton;

    @UiComponentMapping(name = "username")
    private TextBox usernameInput;

    @UiComponentMapping(name = "password")
    private TextBox passwordInput;

    @UiComponentMapping(id = "package")
    private UiComponent dropTable;

    @UiComponentMapping(id = "package")
    private UiComponent packageTable;

    @UiComponentMapping(id = "testware")
    private UiComponent testwareTable;;

    private UiComponent productDrop;

    private Link dropLink;
    
    private Link header;

    private String result;

    private long pageWait = 10000;

    public String checkAutoRefresh(BrowserTab browserTab, String username,
            long timeOut) {
        browserTab.waitUntilComponentIsDisplayed(usernameInput, timeOut);
        browserTab.waitUntilComponentIsDisplayed(refreshButton, timeOut)
                .click();
        usernameInput.setText(username);
        UI.pause(timeOut);
        result = browserTab.waitUntilComponentIsDisplayed(usernameInput,
                timeOut).getText();
        if (!result.isEmpty()) {
            return "Auto Refresh Enable Not Working";
        } else {
            browserTab.waitUntilComponentIsDisplayed(refreshButton, timeOut)
                    .click();
        }
        browserTab.waitUntilComponentIsDisplayed(usernameInput, timeOut);
        usernameInput.setText(username);
        UI.pause(timeOut);
        result = browserTab.waitUntilComponentIsDisplayed(usernameInput,
                timeOut).getText();
        if (!result.isEmpty()) {
            return "Auto Refresh Disable Working";
        } else {
            return "Auto Refresh Disable Not Working";
        }
    }

    public String checkDropPageLoad(BrowserTab browserTab, String product,
            String dropName, long firstTimeOut, long secondTimeOut) {
        dropName = dropName.replace(".", "_");
        header = this.getLink("#productRelease_testrelname_header");
        dropLink = this.getLink("#" + product + "_" + dropName);
        browserTab.waitUntilComponentIsDisplayed(header, pageWait).click();
        browserTab.waitUntilComponentIsDisplayed(dropLink, pageWait).click();
        UI.pause(firstTimeOut);
        if (dropTable.isDisplayed()) {
            relMenu_link.click();
            browserTab.waitUntilComponentIsDisplayed(releases_link, pageWait)
                    .click();
        } else {
            return "Drop Page Not Loaded";
        }
        browserTab.waitUntilComponentIsDisplayed(header, pageWait).click();
        browserTab.waitUntilComponentIsDisplayed(dropLink, pageWait).click();
        UI.pause(secondTimeOut);
        if (dropTable.isDisplayed()) {
            return "Drop Page Loaded";
        } else {
            return "Drop Page Not Loaded";
        }
    }

    public String checkProductPackagePageLoad(BrowserTab browserTab,
            String product, long timeOut) {
        UI.pause(timeOut);
        if (packageTable.isDisplayed()) {
            return "Product Package Page Loaded";
        } else {
            return "Product Package Page Not Loaded";
        }

    }

    public String checkTestwarePageLoad(BrowserTab browserTab, String product,
            long timeOut) {
        UI.pause(timeOut);
        if (testwareTable.isDisplayed()) {
            return "Testware Page Loaded";
        } else {
            return "Testware Page Not Loaded";
        }
    }
}