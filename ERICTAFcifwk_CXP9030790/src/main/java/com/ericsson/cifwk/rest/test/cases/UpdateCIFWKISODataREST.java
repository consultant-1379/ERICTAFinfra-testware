package com.ericsson.cifwk.rest.test.cases;

import org.testng.annotations.*;
import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.sut.test.operators.GenericRESTOperatorHttp;
import com.google.inject.Inject;

public class UpdateCIFWKISODataREST extends TorTestCaseHelper implements
        TestCase {

    @Inject
    GenericRESTOperatorHttp restHTTPOperator;

    /**
     * @throws TimeoutException
     * @PRE Connection to SUT
     * @VUsers 1
     * @PRIORITY HIGH
     */

    @TestId(id = "CIP-4912_Func_1", title = "Ensure that ISO Artifact Data can be stored in CIFWK Database when delivery ISO Artifact via REST")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "updatecifwkisodatarest")
    public void verifyCLICommandsCanBeExecuted(@Input("id") String id,
            @Input("description") String description,
            @Input("host") String hostname,
            @Input("parameters") String parameters,
            @Output("expectedOut") String expectedOut,
            @Input("timeout") int timeout) throws InterruptedException,
            TimeoutException {

        String path = "/CIFWKMediaImport/";
        HttpResponse result = restHTTPOperator.executeRESTPOST(hostname, path,
                parameters, "POST", timeout);
        assertEquals(true, result.getBody().contains(expectedOut));
    }

}
