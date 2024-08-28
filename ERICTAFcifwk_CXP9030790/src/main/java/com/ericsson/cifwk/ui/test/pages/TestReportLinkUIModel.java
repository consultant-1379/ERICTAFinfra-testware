package com.ericsson.cifwk.ui.test.pages;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.ui.*;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.*;
import com.ericsson.cloudPortal.rest.test.cases.TestGetRARestCommand;

public class TestReportLinkUIModel extends GenericViewModel {

    Logger logger = Logger.getLogger(TestGetRARestCommand.class);

    @UiComponentMapping(id = "generic-title")
    private Select page;

    @UiComponentMapping(id = "Physical_test_1_0_1")
    private Select testId;

    @UiComponentMapping(id = "Hello")
    private Select result;

    public String verifyTestReportLink (BrowserTab browserTab, int timeOut) {
        browserTab.waitUntilComponentIsDisplayed(page,timeOut);
        browserTab.open(testId.getProperty("href"));
        browserTab.waitUntilComponentIsDisplayed(result,timeOut);
        return result.getId();
    }
}