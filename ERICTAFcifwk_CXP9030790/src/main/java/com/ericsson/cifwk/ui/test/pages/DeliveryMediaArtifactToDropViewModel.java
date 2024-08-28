package com.ericsson.cifwk.ui.test.pages;

import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.*;

public class DeliveryMediaArtifactToDropViewModel extends GenericViewModel {

    private Link deliverLink;

    private Link isoLink;
    
    private Link header;

    @UiComponentMapping(name = "username")
    private TextBox usernameInput;

    @UiComponentMapping(name = "password")
    private TextBox passwordInput;

    @UiComponentMapping(name = "ciportal_login")
    private Button loginButton;

    @UiComponentMapping(id = "id_productSet")
    private Select productSetSelect;

    @UiComponentMapping(name = "prodSetSubmit")
    private Button submitButton;

    @UiComponentMapping(id = "id_email")
    private TextBox emailInput;

    @UiComponentMapping(id = "id_drop")
    private Select dropSelect;

    @UiComponentMapping(name = "mediaDeliver")
    private Button deliverButton;

    @UiComponentMapping(id = "text-result")
    private UiComponent resultText;

    public String deliveryMedia(BrowserTab browserTab, String username,
            String password, String dropName, String productName,
            String productSetName, String rstate, String email,
            String deliverDrop, long timeOut) {
        isoLink = this.getLink("#" + productName + "_" + dropName + "_iso");
        deliverLink = this.getLink("#mediaDeliver_" + rstate);
        header = this.getLink("#product_test_header");

        browserTab.waitUntilComponentIsDisplayed(header, timeOut).click();

        browserTab.waitUntilComponentIsDisplayed(isoLink, timeOut).click();

        browserTab.waitUntilComponentIsDisplayed(deliverLink, timeOut).click();

        browserTab.waitUntilComponentIsDisplayed(usernameInput, timeOut);
        usernameInput.setText(username);
        passwordInput.setText(password);
        loginButton.click();

        browserTab.waitUntilComponentIsDisplayed(productSetSelect, timeOut);
        productSetSelect.selectByValue(productSetName.replaceAll("\\s", ""));
        UI.pause(500);
        browserTab.waitUntilComponentIsDisplayed(submitButton, timeOut).click();

        browserTab.waitUntilComponentIsDisplayed(emailInput, timeOut);
        emailInput.setText(email);
        dropSelect.selectByValue(deliverDrop.replaceAll("\\s", ""));
        UI.pause(500);
        deliverButton.click();

        return browserTab.waitUntilComponentIsDisplayed(resultText, timeOut).getText();
    }

}
