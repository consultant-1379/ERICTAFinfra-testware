package com.ericsson.cifwk.ui.test.pages;

import java.util.ArrayList;

import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.*;

public class CLUEstatusViewModel extends GenericViewModel {

    @UiComponentMapping("#codeReview")
    private UiComponent codeReview;

    @UiComponentMapping("#unit")
    private UiComponent unit;

    @UiComponentMapping("#acceptance")
    private UiComponent acceptance;

    @UiComponentMapping("#release")
    private UiComponent release;

    private ArrayList<String> results = new ArrayList<String>();

    public ArrayList<String> status(BrowserTab browserTab, long timeOut) {
        browserTab.waitUntilComponentIsDisplayed(codeReview, timeOut);
        results.add(codeReview.getProperty("alt"));
        browserTab.waitUntilComponentIsDisplayed(unit, timeOut);
        results.add(unit.getProperty("alt"));
        browserTab.waitUntilComponentIsDisplayed(acceptance, timeOut);
        results.add(acceptance.getProperty("alt"));
        browserTab.waitUntilComponentIsDisplayed(release, timeOut);
        results.add(release.getProperty("alt"));
        return results;
    }

}
