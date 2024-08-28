package com.ericsson.cifwk.teststeps;

import com.ericsson.cifwk.taf.TestContext;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.tools.http.constants.HttpStatus;
import com.ericsson.sut.test.operators.GenericRESTOperatorHttp;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

public class PortalRESTTestSteps extends TorTestCaseHelper {

    public static final String RUN_REST_CALLS = "CIP-7293_Func_1.0,CIP-7215_Func_1.0";
    public static final String RUN_REST_CALLS_TIMEOUT = "CIP-6572_Func_1";
    public static final String RUN_REST_CALLS_JSON_RETURN = "CIP-7454_Func_1";
    public static final String RUN_REST_CALLS_OPEN_CLOSE_DROP = "CIP-8325_Func_1";
    Logger logger = Logger.getLogger(PortalRESTTestSteps.class);
    HttpResponse result;

    @Inject
    TestContext context;

    @Inject
    GenericRESTOperatorHttp restHTTPOperator;

    @Context(context = { Context.REST })
    @TestStep(id = RUN_REST_CALLS)
    public void performRESTCommands(
            @Input("baseUrl") String baseUrl,
            @Input("path") String path,
            @Input("restParameters") String restParameters,
            @Input("type") String type,
            @Output("expected") boolean expected,
            @Output("expectedOut") String expectedOut,
            @Output("httpResponse") String httpResponse) {

        result = restHTTPOperator.executeREST(baseUrl, path, restParameters, type);

        if (httpResponse.equals("OK")) {
            assertEquals(result.getResponseCode(), HttpStatus.OK);
        } else {
            assertEquals(result.getResponseCode(), HttpStatus.NOT_FOUND);
        }
        if (expectedOut != null && !expectedOut.isEmpty()) {
            logger.info("Result: " + result.getBody().toString());
            assertEquals(result.getBody().contains(expectedOut), expected);
        }
    }

    @Context(context = { Context.REST })
    @TestStep(id = RUN_REST_CALLS_TIMEOUT)
    public void performRESTwithTimeoutCommands(
            @Input("baseUrl") String baseUrl,
            @Input("path") String path,
            @Input("restParameters") String restParameters,
            @Input("type") String type,
            @Input("timeout") int timeOut,
            @Output("expected") boolean expected,
            @Output("expectedOut") String expectedOut,
            @Output("httpResponse") String httpResponse) {

        result = restHTTPOperator.executeRESTWithTimeOut(baseUrl, path, restParameters, type, timeOut);

        if (httpResponse.equals("OK")) {
            assertEquals(result.getResponseCode(), HttpStatus.OK);
        } else {
            assertEquals(result.getResponseCode(), HttpStatus.NOT_FOUND);
        }
        if (expectedOut != null && !expectedOut.isEmpty()) {
            assertEquals(result.getBody().contains(expectedOut), expected);
        }
    }

    @Context(context = { Context.REST })
    @TestStep(id = RUN_REST_CALLS_JSON_RETURN)
    public void verifyCallRestReturnForPackages(@Input("Id") String Id,
            @Input("Description") String testCaseDescription,
            @Input("HostName") String hostName,
            @Input("GetDropBaseURL") String getDropBaseURL,
            @Input("MediaURL") String mediaURL,
            @Input("RestGetParameters") String restGetParameters,
            @Output("ExpectedMediaCategory") String expectedMediaCategory,
            @Output("ExpectedMediaPath") String expectedMediaPath,
            @Output("ExpectedName") String expectedName,
            @Output("ExpectedNumber") String expectedNumber,
            @Output("ExpectedPlatform") String expectedPlatform,
            @Output("ExpectedVersion") String expectedVersion,
            @Output("ExpectedResult") String expectedResult) {
        setTestCase(Id, testCaseDescription);
        setTestStep("Check that REST Call responses are as expected");

        result = restHTTPOperator.executeREST(hostName, getDropBaseURL, restGetParameters, "GET");
        logger.debug("Response: " + result.getResponseCode());
        String found = null;
        String restBody = result.getBody();
        String[] names = expectedName.split(",");
        String[] numbers = expectedNumber.split(",");
        String[] mediaCats = expectedMediaCategory.split(",");
        String[] mediaPaths = expectedMediaPath.split(",");
        String[] versions = expectedVersion.split(",");
        String[] platforms = expectedPlatform.split(",");
        String[] expectedResults = expectedResult.split(",");
        try {
            JSONArray responseArray = new JSONArray(restBody);
            for (int j = 0; j < names.length; j++) {
                for (int i = 0; i < responseArray.length(); i++) {
                    setTestStep("Verify Drop content is as expected");
                    if (responseArray.getJSONObject(i).getString("name")
                            .equals(names[j].toString())) {
                        found = "TRUE";
                        assertEquals(mediaCats[j].toString(), responseArray.getJSONObject(i).getString("mediaCategory"));
                        assertEquals(mediaPaths[j].toString(), responseArray.getJSONObject(i).getString("mediaPath"));
                        assertEquals(names[j].toString(), responseArray.getJSONObject(i).getString("name"));
                        assertEquals(numbers[j].toString(), responseArray.getJSONObject(i).getString("number"));
                        assertEquals(versions[j].toString(), responseArray.getJSONObject(i).getString("version"));
                        assertEquals(platforms[j].toString(), responseArray.getJSONObject(i).getString("platform"));
                    }
                }
                assertEquals(expectedResults[j], found);
                found = "FALSE";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Context(context = { Context.REST })
    @TestStep(id = RUN_REST_CALLS_OPEN_CLOSE_DROP)
    public void verifyOpenCloseDropFunctionality(@Input("id") String id,
            @Input("baseUrl") String baseUrl,
            @Input("path") String path,
            @Input("restParameters") String restParameters,
            @Input("description") String testCaseDescription,
            @Input("type") String type,
            @Output("expected") boolean expected,
            @Output("expectedOut") String expectedOut,
            @Output("httpResponse") String httpResponse) {
        setTestCase(id, testCaseDescription);
        setTestStep("Check that REST Call responses are as expected");

        result = restHTTPOperator.executeREST(baseUrl, path, restParameters, type);

        if (httpResponse.equals("OK")) {
            assertEquals(result.getResponseCode(), HttpStatus.OK);
        } else {
            assertEquals(result.getResponseCode(), HttpStatus.NOT_FOUND);
        }
        if (expectedOut != null && !expectedOut.isEmpty()) {
            logger.info("Result: " + result.getBody().toString());
            assertEquals(result.getBody().contains(expectedOut), expected);
        }
    }
}
