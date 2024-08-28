package com.ericsson.cifwk.rest.test.cases;

import org.testng.annotations.Test;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.tools.http.constants.HttpStatus;
import com.ericsson.sut.test.operators.GenericRESTOperator;

import com.google.inject.Inject;

public class DeliveryRESTCommand extends TorTestCaseHelper implements TestCase {

    @Inject
    OperatorRegistry<GenericRESTOperator> operatorRegistry;

    @Context(context = { Context.REST })
    @DataDriven(name = "deliveryRESTCommands")
    @Test
    public void shouldReturnWebPageFromGetCall(
            @Input("baseUrl") String baseUrl,
            @Input("path") String path,
            @Input("restParameters") String restParameters,
            @Input("type") String type,
            @Output("expected") boolean expected,
            @Output("expectedOut") String expectedOut,
            @Output("httpResponse") String httpResponse) {
        GenericRESTOperator websiteOperator = operatorRegistry.provide(GenericRESTOperator.class);
        HttpResponse result = websiteOperator.executeREST(baseUrl, path, restParameters, type);
        if (httpResponse.equals("OK")) {
            assertEquals(result.getResponseCode(), HttpStatus.OK);
        } else {
            assertEquals(result.getResponseCode(), HttpStatus.NOT_FOUND);
        }
        if (expectedOut != null && !expectedOut.isEmpty()) {
            assertEquals(result.getBody().contains(expectedOut), expected);
        }
    }
}
