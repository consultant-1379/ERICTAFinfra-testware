package com.ericsson.cifwk.ui.test.cases ;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.*;

import org.testng.annotations.Test;

import javax.inject.Inject;

import com.ericsson.sut.test.operators.*;

public class MetricsCloudUI extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<CIPortalOperator> provider;
    
    @TestId(id = "CIP-6201_Func_1", title = "Check the metrics form that it displays the correct info and submits for a request")
    @Context(context = {Context.UI})
    @Test(groups={"Acceptance"})
    @DataDriven(name = "metricsCloudFormUI")
    public void testingMetricsCloudForm(
            @Input("Id") String Id,
            @Input("Description") String testCaseDescription,
            @Input("Host") String host,
            @Input("Url") String url,
            @Input("RA") String ra,
            @Input("DataCenter") String dataCenter,
            @Input("StartDateTime") String startDT,
            @Input("EndDateTime") String endDT,
            @Input("EventType") String eventType,
            @Output("Expected") boolean expected,
            @Input("TimeOut") long timeOut,
            @Input("Type") String type) {
        setTestCase(Id, testCaseDescription);
        CIPortalOperator operator = provider.provide(CIPortalOperator.class);
        operator.startBrowser(host, url, null);
        boolean result = operator.checkCloudMetricsForm(ra, dataCenter, startDT, endDT, eventType, timeOut, type);
        assertTrue(result == expected);
    }
}