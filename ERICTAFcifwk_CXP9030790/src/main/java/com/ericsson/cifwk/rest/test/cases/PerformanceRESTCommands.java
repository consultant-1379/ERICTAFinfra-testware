package com.ericsson.cifwk.rest.test.cases;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.tools.http.constants.HttpStatus;
import com.ericsson.sut.test.operators.GenericRESTOperator;
import com.google.inject.Inject;

public class PerformanceRESTCommands extends TorTestCaseHelper implements
        TestCase {
    @Inject
    OperatorRegistry<GenericRESTOperator> operatorRegistry;

    @TestId(id = "CIP-6341_Func_6", title = "Verify Drop page load and refresh times are as expected")
    @Context(context = { Context.REST })
    @DataDriven(name = "dropLoadingPerformanceREST")
    @Test
    @VUsers(vusers = { 50 })
    public void shouldReturnDropPageFromGetCall(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("url") String url, @Input("directory") String path,
            @Input("timeoutOne") int firstTimeOut,
            @Input("timeoutTwo") int secondTimeOut,
            @Output("expected") String expected) {
        setTestCase(Id, testCaseDescription);
        GenericRESTOperator websiteOperator = operatorRegistry
                .provide(GenericRESTOperator.class);
        HttpResponse result;
        result = websiteOperator.executeRESTWithTimeOut(url, path, null, "GET", firstTimeOut);
        assertEquals(result.getResponseCode(), HttpStatus.OK);
        result = websiteOperator.executeRESTWithTimeOut(url, path, null, "GET", secondTimeOut);
        assertEquals(result.getResponseCode(), HttpStatus.OK);

    }

    @TestId(id = "CIP-6341_Func_7", title = "Verify Product Package page load and refresh times are as expected")
    @Context(context = { Context.REST })
    @DataDriven(name = "productPackageLoadingPerformanceREST")
    @Test
    @VUsers(vusers = { 50 })
    public void shouldReturnProductPackagePageFromGetCall(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("url") String url, 
            @Input("directory") String path,
            @Input("timeout") int timeOut, 
            @Output("expected") String expected) {
        setTestCase(Id, testCaseDescription);
        GenericRESTOperator websiteOperator = operatorRegistry
                .provide(GenericRESTOperator.class);
        HttpResponse result = websiteOperator.executeRESTWithTimeOut(url, path,
                null, "GET", timeOut);
        assertEquals(result.getResponseCode(), HttpStatus.OK);
    }

    @TestId(id = "CIP-6341_Func_8", title = "Verify Product Testware page load and refresh times are as expected")
    @Context(context = { Context.REST })
    @DataDriven(name = "testwareLoadingPerformanceREST")
    @Test
    @VUsers(vusers = { 50 })
    public void shouldReturnTestwarePageFromGetCall(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("url") String url, 
            @Input("directory") String path,
            @Input("timeout") int timeOut, 
            @Output("expected") String expected) {
        setTestCase(Id, testCaseDescription);
        GenericRESTOperator websiteOperator = operatorRegistry
                .provide(GenericRESTOperator.class);
        HttpResponse result = websiteOperator.executeRESTWithTimeOut(url, path,
                null, "GET", timeOut);
        assertEquals(result.getResponseCode(), HttpStatus.OK);
    }
}
