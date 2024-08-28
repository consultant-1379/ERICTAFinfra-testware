package com.ericsson.cifwk.ui.test.pages;

import java.util.ArrayList;

import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.*;
import com.google.common.collect.UnmodifiableIterator;

public class SummaryPageConfigurationViewModel extends GenericViewModel {

    @UiComponentMapping(name="username")
    private TextBox usernameInput;

    @UiComponentMapping(name="password")
    private TextBox passwordInput;

    @UiComponentMapping(name="ciportal_login")
    private Button loginButton;

    private Link formLink;
    private Link headerLink;

    private ArrayList<Link> dropLinks = new ArrayList<>();

    @UiComponentMapping(name="1_reldrop")
    private Select relDropOneSelect;

    @UiComponentMapping(name="2_reldrop")
    private Select relDropTwoSelect;

    @UiComponentMapping(name="3_reldrop")
    private Select relDropThreeSelect;

    @UiComponentMapping(name="4_reldrop")
    private Select relDropFourSelect;

    @UiComponentMapping(name="1_number")
    private Select numberOneSelect;

    @UiComponentMapping(name="2_number")
    private Select numberTwoSelect;

    @UiComponentMapping(name="3_number")
    private Select numberThreeSelect;

    @UiComponentMapping(name="4_number")
    private Select numberFourSelect;

    private Select selectItem;

    @UiComponentMapping(name="defbehav")
    private CheckBox defaultCheckBox;

    @UiComponentMapping(id="generic-title")
    private Label title;

    @UiComponentMapping(name="Submit")
    private Button submitButton;

    @UiComponentMapping(name="Cancel")
    private Button cancelButton;

    public String formTesting(BrowserTab browserTab, String username, String password, String value, String tagName, long timeOut){
        browserTab.waitUntilComponentIsDisplayed(usernameInput, timeOut);
        usernameInput.setText(username);
        passwordInput.setText(password);
        loginButton.click();
        String element = "." + tagName;
        selectItem = this.getSelect(element);
        browserTab.waitUntilComponentIsDisplayed(selectItem, timeOut);
        return "Item Found";
    }

    public String formTestingResults(BrowserTab browserTab,
            String username, String password, String itemName, ArrayList<String> values, String action,
            String defaultDrop, long timeOut){
        dropLinks.clear();
        if (action.equals("submit") || action.equals("default") ||  action.equals("error")){
            browserTab.waitUntilComponentIsDisplayed(formLink, timeOut).click();
            browserTab.waitUntilComponentIsDisplayed(usernameInput, timeOut);
            usernameInput.setText(username);
            passwordInput.setText(password);
            loginButton.click();
            browserTab.waitUntilComponentIsDisplayed(relDropOneSelect, timeOut);
            if (values.get(0) != null) {
                relDropOneSelect.selectByValue(values.get(0).replaceAll("\\s",""));
                if (values.get(1) != null) {
                    numberOneSelect.selectByValue(values.get(1).replaceAll("\\s",""));
                }
                if (!values.get(0).contains(itemName)) {
                    dropLinks.add(getLink("#" + itemName + "_"
                            + values.get(0).replace(".", "_")));
                }
            }
            if (values.get(2) != null) {
                relDropTwoSelect.selectByValue(values.get(2).replaceAll("\\s", ""));
                if (values.get(3) != null) {
                    numberTwoSelect.selectByValue(values.get(3).replaceAll("\\s", ""));
                }
                if (!values.get(2).contains(itemName)) {
                    dropLinks.add(getLink("#" + itemName + "_" + values.get(2).replace(".", "_")));
                }
            }
            if (values.get(4) != null) {
                relDropThreeSelect.selectByValue(values.get(4).replaceAll("\\s", ""));
                if (values.get(5) != null) {
                    numberThreeSelect.selectByValue(values.get(5).replaceAll("\\s", ""));
                }
                if (!values.get(4).contains(itemName)) {
                    dropLinks.add(getLink("#" + itemName + "_" + values.get(4).replace(".", "_")));
                }
            }
            if (values.get(6) != null) {
                relDropFourSelect.selectByValue(values.get(6).replaceAll("\\s", ""));
                if (values.get(7) != null) {
                    numberFourSelect.selectByValue(values.get(7).replaceAll("\\s", ""));
                }
                if (!values.get(6).contains(itemName)) {
                    dropLinks.add(getLink("#" + itemName + "_" + values.get(6).replace(".", "_")));
                }
            }
            submitButton.click();
            if (action.equals("submit")){
                browserTab.waitUntilComponentIsDisplayed(title,timeOut);
                if (defaultDrop != null){
                    String[] drops = defaultDrop.split(",");
                    for(String drop : drops){
                        dropLinks.add(getLink("#"+ itemName + "_" + drop.replace(".", "_")));
                    }
                }
                for (Link item : dropLinks) {
                    System.out.println(item);
                    if (!item.isDisplayed()) {
                        return "Error Drop Not Found";
                    }
                } 
                return "Drops Found";
            }else if (action.equals("default")){
                browserTab.waitUntilComponentIsDisplayed(title,timeOut).getText();
                browserTab.waitUntilComponentIsDisplayed(formLink, timeOut).click();
                browserTab.waitUntilComponentIsDisplayed(defaultCheckBox, timeOut);
                defaultCheckBox.click();
                submitButton.click();
                browserTab.waitUntilComponentIsDisplayed(title,timeOut);
                if (defaultDrop != null){
                    String[] drops = defaultDrop.split(",");
                    for(String drop : drops){
                        dropLinks.add(getLink("#"+ itemName + "_" + drop.replace(".", "_")));
                    }
                }
                for (Link item : dropLinks) {
                    if (!item.isDisplayed()) {
                        return "Error Drop Not Found";
                    }
                } 
                return "Drops Found";
            }else{
                return browserTab.waitUntilComponentIsDisplayed(title,timeOut).getText();
            }
       }else if (action.equals("cancel")){
           browserTab.waitUntilComponentIsDisplayed(formLink, timeOut).click();
           browserTab.waitUntilComponentIsDisplayed(usernameInput, timeOut);
           usernameInput.setText(username);
           passwordInput.setText(password);
           loginButton.click();
           browserTab.waitUntilComponentIsDisplayed(relDropOneSelect, timeOut);
           cancelButton.click();
           return browserTab.waitUntilComponentIsDisplayed(title,timeOut).getText();
       }
      return "Error";
    }

    public String checkProductSummaryPageForm(BrowserTab browserTab, String username, String password,
                String productName, String value, String tagName, long timeOut, String header) {
        if (productName.equals("OSS-RC")){
            headerLink = this.getLink("#" + header);
            browserTab.waitUntilComponentIsDisplayed(headerLink, timeOut).click();
        }
        formLink = this.getLink("#product_form_" + productName);
        browserTab.waitUntilComponentIsDisplayed(formLink, timeOut).click();
        return formTesting(browserTab, username, password, value,tagName, timeOut);
    }

    public String checkProductSetSummaryPageForm(BrowserTab browserTab,
            String username, String password, String productSetName,
            String value, String tagName, long timeOut, String header) {
        if (productSetName.equals("TOR")){
            headerLink = this.getLink("#" + header);
            browserTab.waitUntilComponentIsDisplayed(headerLink, timeOut).click();
        }
        formLink = this.getLink("#productSet_form_" + productSetName);
        browserTab.waitUntilComponentIsDisplayed(formLink, timeOut).click();
        return formTesting(browserTab, username, password, value,tagName, timeOut);
    }

    public String checkProductSummaryPageFormResult(BrowserTab browserTab,
            String username, String password, String productName, ArrayList<String> values, String action,
            String defaultDrop, long timeOut, String header) {
        if (productName.equals("OSS-RC") || productName.equals("test")){
            headerLink = this.getLink("#" + header);
            browserTab.waitUntilComponentIsDisplayed(headerLink, timeOut).click();
        } 
        formLink = this.getLink("#product_form_" + productName);
        return formTestingResults(browserTab, username, password, productName, values, action, defaultDrop, timeOut);
        
     }

    public String checkProductSetSummaryPageFormResult(BrowserTab browserTab,
            String username, String password, String productSetName, ArrayList<String> values, String action,
            String defaultDrop, long timeOut, String header) {
        if (productSetName.equals("OSS-RC")){
            headerLink = this.getLink("#" + header);
            browserTab.waitUntilComponentIsDisplayed(headerLink, timeOut).click();
        }
        formLink = this.getLink("#productSet_form_" + productSetName);
        return formTestingResults(browserTab, username, password, productSetName, values, action, defaultDrop, timeOut);
     } 
}
