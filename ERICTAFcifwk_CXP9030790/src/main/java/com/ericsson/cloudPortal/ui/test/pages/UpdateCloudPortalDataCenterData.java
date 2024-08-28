package com.ericsson.cloudPortal.ui.test.pages;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.*;
import com.ericsson.cloudPortal.rest.test.cases.TestGetRARestCommand;

public class UpdateCloudPortalDataCenterData extends GenericViewModel {
    
    Logger logger = Logger.getLogger(TestGetRARestCommand.class);

    @UiComponentMapping(name = "data[User][username]")
    private TextBox usernameInput;

    @UiComponentMapping(name = "data[User][password]")
    private TextBox passwordInput;

    @UiComponentMapping(name = "sppLogin")
    private Button loginButton;

    @UiComponentMapping(id = "orgvdcs_table_wrapper")
    private Select orgVDCTable;
    
    @UiComponentMapping(name = "urn:vcloud:orgvdc:51230e1f-c973-4451-9a4e-4a58ab9086b8")
    private Select orgVDC;
    
    @UiComponentMapping(id = "OrgVdcEditForm")
    private Select VDCEditForm;
    
    @UiComponentMapping(name = "data[OrgVdc][running_tb_limit]")
    private TextBox runningTextbox;
    
    @UiComponentMapping(name = "data[OrgVdc][stored_tb_limit]")
    private TextBox totalTestBox;
    
    @UiComponentMapping(name = "data[OrgVdc][mig_ra_id]")
    private Select raTextBox;

    @UiComponentMapping(name = "editOrgsVDC")
    private Button submitUpdateButton;

    public String checkActionsOptions(BrowserTab browserTab, String username, String password, String requirementsArea, String dataCenterId, long timeOut) {

        browserTab.waitUntilComponentIsDisplayed(usernameInput, timeOut);
        usernameInput.setText(username);
        passwordInput.setText(password);
        loginButton.click();

        browserTab.waitUntilComponentIsDisplayed(orgVDCTable, timeOut);
        UI.pause(250);
        orgVDC.click();
        UI.pause(250);
        return "Success";
    }
    public String updateDataCenterUI(BrowserTab browserTab, String username, String password, String requirementsArea, String dataCenterId, long timeOut) {
        
        browserTab.waitUntilComponentIsDisplayed(usernameInput, timeOut);
        usernameInput.setText(username);
        passwordInput.setText(password);
        loginButton.click();
        browserTab.waitUntilComponentIsDisplayed(VDCEditForm, timeOut);
        UI.pause(250);
        runningTextbox.setText("1");
        totalTestBox.setText("2");
        raTextBox.selectByValue(requirementsArea);
        submitUpdateButton.click();
        UI.pause(250);
        browserTab.waitUntilComponentIsDisplayed(orgVDCTable, timeOut);
        return "Success";
        
    }
}
