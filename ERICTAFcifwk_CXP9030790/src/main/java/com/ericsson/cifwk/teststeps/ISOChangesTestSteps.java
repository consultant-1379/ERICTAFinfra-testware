package com.ericsson.cifwk.teststeps;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.sut.test.operators.GenericRESTOperatorHttp;

import javax.inject.Inject;

public class ISOChangesTestSteps extends TorTestCaseHelper {

    public static final String IS_ISO_BUILD_NEEDED = "CIP-3456_Func_1.3";

    @Inject
    GenericRESTOperatorHttp restHTTPOperator;

    @TestStep(id = IS_ISO_BUILD_NEEDED)
    public void checkIsoBuildNeeded(@Input("Id") String Id,
            @Input("Description") String testCaseDescription,
            @Input("HostName") String hostName,
            @Input("BaseURLDirectory") String baseURLDirectory,
            @Input("RestGetParameters") String restGetParameters,
            @Output("ExpectedResult") String expectedResult){

        HttpResponse response = restHTTPOperator.executeREST(hostName, baseURLDirectory, restGetParameters, "GET");
        assertEquals(expectedResult, response.getBody().toString());
    }
}
