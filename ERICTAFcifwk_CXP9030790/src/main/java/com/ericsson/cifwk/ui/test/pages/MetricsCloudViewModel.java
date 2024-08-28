/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.cifwk.ui.test.pages;

import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.CheckBox;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Select;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

public class MetricsCloudViewModel extends GenericViewModel {
    @UiComponentMapping(id="CI")
    private Select raSelect;
    @UiComponentMapping(id="alldegs")
    private CheckBox allRaSelect;
    private UiComponent raComp;

    @UiComponentMapping(id="Axis_OrgDC1")
    private Select dataCenterSelect;
    @UiComponentMapping(id="allcenters")
    private CheckBox allDataCenterSelect;
    private UiComponent dataCenterComp;

    @UiComponentMapping(id="startDate")
    private TextBox startDate;

    @UiComponentMapping(id="endDate")
    private TextBox endDate;

    @UiComponentMapping(id="start_vapp")
    private Select eventTypeSelect;
    @UiComponentMapping(id="allevents")
    private CheckBox allEventTypeSelect;
    private UiComponent eventTypeComp;

    private UiComponent submitButton;

    @UiComponentMapping(id="generic-text")
    private Select resultTable;

    public boolean checkMetricsForm(BrowserTab browserTab, String ra, String dataCenter, String startDT, String endDT, String eventType, long timeOut, String type) {
        String result = "";
        boolean found = false;
        switch(type){
        case "ra":
            browserTab.waitUntilComponentIsDisplayed(raSelect, timeOut);
            raComp = this.getViewComponent('#'+ ra);
            if(raComp.getProperty("id").contains(ra)) {
                found = true;
            }
            break;
        case "dataC":
            browserTab.waitUntilComponentIsDisplayed(raSelect, timeOut);
            raComp = this.getViewComponent('#'+ ra);
            if(raComp.getProperty("id").contains(ra)) {
                found = true;
            }
            else{
                break;
            }
            raSelect.click();
            dataCenterComp = this.getViewComponent('#'+ dataCenter);
            browserTab.waitUntilComponentIsDisplayed(dataCenterComp, timeOut);
            result = dataCenterComp.getProperty("id");
            if(result.contains(dataCenter)) {
                found = true;
            }
            break;
        case "start":
            browserTab.waitUntilComponentIsDisplayed(startDate, timeOut);
            startDate.setText(startDT);
            found = true;
            break;
        case "end":
            browserTab.waitUntilComponentIsDisplayed(endDate, timeOut);
            endDate.setText(endDT);
            found = true;
            break;
        case "event":
            eventTypeComp = this.getViewComponent('#'+ eventType);
            browserTab.waitUntilComponentIsDisplayed(eventTypeSelect, timeOut);
            if(eventTypeComp.getProperty("id").contains(eventType)) {
                found = true;
            }
            break;
        case "submit":
            browserTab.waitUntilComponentIsDisplayed(raSelect, timeOut);
            if (ra.equals("ALL")){
                allRaSelect.click();
            }
            else{
                raSelect.click();
            }
            browserTab.waitUntilComponentIsDisplayed(startDate, timeOut);
            startDate.setText(startDT);
            browserTab.waitUntilComponentIsDisplayed(eventTypeSelect, timeOut);
            if (eventType.equals("ALL")){
                allEventTypeSelect.click();
            }
            else{
                eventTypeSelect.click();
            }
            browserTab.waitUntilComponentIsDisplayed(dataCenterSelect, timeOut);
            if (dataCenter.equals("ALL")){
                allDataCenterSelect.click();
            }
            else{
                dataCenterSelect.click();
            }
            browserTab.waitUntilComponentIsDisplayed(endDate, timeOut);
            endDate.setText(endDT);
            submitButton = this.getViewComponent("#search_button");
            browserTab.waitUntilComponentIsDisplayed(submitButton, timeOut);
            submitButton.click();
            browserTab.waitUntilComponentIsDisplayed(resultTable, timeOut);
            found = true;
            break;
        }
        return found;
    }
}
