package com.ericsson.cifwk.rest.test.cases;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.sut.test.operators.GenericRESTOperator;
import com.ericsson.cifwk.utils.JSONMethods;
import com.google.inject.Inject;

public class TestJSONHostProperties extends TorTestCaseHelper implements
        TestCase {
    private GenericRESTOperator genericRESTOperator;
    Logger logger = Logger.getLogger(TestJSONHostProperties.class);
    @Inject
    private OperatorRegistry<GenericRESTOperator> genericRESTOperatorRegistry;

    @Context(context = { Context.REST })
    @DataDriven(name = "testJSONHostProperties")
    @Test(groups = { "Functional", "vApp", "Physical" })
    @TestId(id = "CIP-6359_Func_1", title = "JSON host properties test")
    public void verifyISOContents(@Input("Id") String Id,
            @Input("Description") String testCaseDescription,
            @Input("HostName") String hostName,
            @Input("GetDropBaseURL") String getDropBaseURL,
            @Input("RestGetParameters") String restGetParameters,
            @Output("IsValidJSON") boolean isValidJSON,
            @Output("ExpectedOutput") String expectedOutput

    ) {
        setTestCase(Id, testCaseDescription);
        setTestStep("Check that REST Call responses are as expected");
        genericRESTOperator = genericRESTOperatorRegistry
                .provide(GenericRESTOperator.class);
        HttpResponse response = genericRESTOperator.executeREST(hostName,
                getDropBaseURL, restGetParameters, "GET");
        logger.debug("Response: " + response.getResponseCode());
        String restBody = response.getBody();
        boolean validJSONReturned;
        validJSONReturned = JSONMethods.isJSONValid(restBody);
        assertEquals(isValidJSON, validJSONReturned);
        assertEquals(response.getBody().contains(expectedOutput), true);
    }

   

}
