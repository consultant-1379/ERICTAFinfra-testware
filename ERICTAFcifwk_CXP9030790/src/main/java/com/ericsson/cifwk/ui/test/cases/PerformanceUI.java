package com.ericsson.cifwk.ui.test.cases;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.*;
import org.testng.annotations.Test;
import javax.inject.Inject;
import com.ericsson.sut.test.operators.*;

public class PerformanceUI extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<CIPortalOperator> provider;
    /* Commented TC as no longer working in overall Suite - To be investigated
    @TestId(id = "CIP-6141_Func_1", title = "Verify The Enable and Disable Auto refresh options on the CI Portal")
    @Context(context = { Context.UI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "autoRefreshPerformanceUI")
    public void testAutoRefresh(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("host") String host, 
            @Input("directory") String directory,
            @Input("username") String username,
            @Output("expected") String expected,
            @Input("timeoutInMillis") long timeOut) {
        setTestCase(Id, testCaseDescription);
        CIPortalOperator operator = provider.provide(CIPortalOperator.class);
        operator.startBrowser(host, directory, null);
        String result = operator.checkAutoRefresh(username, timeOut);
        assert (result.contains(expected));
    }*/

    @TestId(id = "CIP-6341_Func_2", title = "Verify Drop page load and refresh times are as expected")
    @Context(context = { Context.UI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "dropLoadingPerformanceUI")
    public void testDropPageLoad(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("host") String host, 
            @Input("directory") String directory,
            @Input("product") String product,
            @Input("dropName") String dropName,
            @Output("expected") String expected,
            @Input("timeoutInMillisOne") long firstTimeOut,
            @Input("timeoutInMillisTwo") long secondTimeOut) {
        setTestCase(Id, testCaseDescription);
        CIPortalOperator operator = provider.provide(CIPortalOperator.class);
        operator.startBrowser(host, directory, null);
        String result = operator.checkDropPageLoad(product, dropName,
                firstTimeOut, secondTimeOut);
        assert (result.contains(expected));
    }

    @TestId(id = "CIP-6341_Func_3", title = "Verify Product Package page load and refresh times are as expected")
    @Context(context = { Context.UI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "productPackageLoadingPerformanceUI")
    public void testProductPackagePageLoad(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("host") String host, 
            @Input("directory") String directory,
            @Input("product") String product,
            @Output("expected") String expected,
            @Input("timeoutInMillis") long timeOut) {
        setTestCase(Id, testCaseDescription);
        CIPortalOperator operator = provider.provide(CIPortalOperator.class);
        operator.startBrowser(host, directory, null);
        String result = operator.checkProductPackagePageLoad(product, timeOut);
        assertEquals(expected, result);
    }

    @TestId(id = "CIP-6341_Func_4", title = "Verify Product Testware page load and refresh times are as expected")
    @Context(context = { Context.UI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "testwareLoadingPerformanceUI")
    public void testTestwarePageLoad(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("host") String host, 
            @Input("directory") String directory,
            @Input("product") String product,
            @Output("expected") String expected,
            @Input("timeoutInMillis") long timeOut) {
        setTestCase(Id, testCaseDescription);
        CIPortalOperator operator = provider.provide(CIPortalOperator.class);
        operator.startBrowser(host, directory, null);
        String result = operator.checkTestwarePageLoad(product, timeOut);
        assertEquals(expected, result);
    }
}
