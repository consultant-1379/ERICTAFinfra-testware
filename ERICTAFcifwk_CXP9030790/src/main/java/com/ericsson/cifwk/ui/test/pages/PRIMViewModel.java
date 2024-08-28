package com.ericsson.cifwk.ui.test.pages;

import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.*;


public class PRIMViewModel extends GenericViewModel {


    @UiComponentMapping(name="username")
    private TextBox usernameInput;

    @UiComponentMapping(name="password")
    private TextBox passwordInput;

    @UiComponentMapping(name="ciportal_login")
    private Button loginButton;

    @UiComponentMapping(name="primLogin")
    private Button submitButton;

    @UiComponentMapping(name="writeToPrim")
    private Button writeButton;


    @UiComponentMapping(name="Next ...")
    private Button nextButton;

    @UiComponentMapping(id="id_options_1")
    private RadioButton cxpOption;

    @UiComponentMapping(id="id_drop")
    private Select dropSelect;

    @UiComponentMapping(id="id_media")
    private Select mediaSelect;

    @UiComponentMapping(id="primHeading")
    private UiComponent headingText;

    private UiComponent numberText;
    private UiComponent rstateText;
    private UiComponent missingNum;
    private UiComponent resultText;



    public String primResults(BrowserTab browserTab, String username,
            String password, String product, String cxpNumber, String rstate,
            String drop, String media, String write, String missing, long timeOut) {


        browserTab.waitUntilComponentIsDisplayed(usernameInput, timeOut);
        usernameInput.setText(username);
        passwordInput.setText(password);
        loginButton.click();

        browserTab.waitUntilComponentIsDisplayed(cxpOption, timeOut).click();
        for (Option drp: dropSelect.getAllOptions()){
            if (drp.getValue().contains(drop.replaceAll("\\s",""))){
                dropSelect.selectByValue(drop.replaceAll("\\s",""));
            }
        }
        nextButton.click();
        browserTab.waitUntilComponentIsDisplayed(usernameInput, timeOut);
        usernameInput.setText(username);
        passwordInput.setText(password);
        for (Option ver: mediaSelect.getAllOptions()){
            if (ver.getValue().contains(media.replaceAll("\\s",""))){
                mediaSelect.selectByValue(media.replaceAll("\\s",""));
            }
        }
        submitButton.click();
        browserTab.waitUntilComponentIsDisplayed(headingText, timeOut);

        numberText = this.getViewComponent("#number_" + cxpNumber);
        rstateText = this.getViewComponent("#"+ cxpNumber + "_nrstate_" + rstate);
        missingNum = this.getViewComponent("#missing_" + cxpNumber);
        resultText = this.getViewComponent("#result_" + cxpNumber);

        if (write != null){
            writeButton.click();
            browserTab.getMessageBox().clickOk();
            browserTab.waitUntilComponentIsDisplayed(resultText, timeOut);
            return resultText.getText();
        }
        else if (missing != null){
            return missingNum.getText();
        }
        else if(numberText.exists()){
        	return numberText.getText() + " " +rstateText.getText();
        }
        else{
        	return "No Change";
        }
    }

    public String primUserAccessResults(BrowserTab browserTab, String username,
            String password, long timeOut) {
        browserTab.waitUntilComponentIsDisplayed(usernameInput, timeOut);
        usernameInput.setText(username);
        passwordInput.setText(password);
        loginButton.click();
        UI.pause(timeOut);
        if(cxpOption.exists()){
            return "Has Access";
        }else{
            return "No Access";
        }
    }
}