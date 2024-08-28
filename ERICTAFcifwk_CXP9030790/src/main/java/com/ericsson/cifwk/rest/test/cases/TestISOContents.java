package com.ericsson.cifwk.rest.test.cases;


import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
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
import com.google.inject.Inject;

public class TestISOContents extends TorTestCaseHelper implements TestCase {
    private GenericRESTOperator genericRESTOperator;
    Logger logger = Logger.getLogger(TestISOContents.class);
    @Inject
    private OperatorRegistry<GenericRESTOperator> genericRESTOperatorRegistry;

    @Context(context = {Context.REST})
    @DataDriven(name = "testISOContents")
    @Test(groups = { "Functional", "vApp", "Physical" })
    @TestId(id = "CIP-3456_Func_1", title = "")
    public void verifyISOContents(@Input("Id") String Id,
                                    @Input("Description") String testCaseDescription,
                                    @Input("HostName") String hostName,
                                    @Input("GetDropBaseURL") String getDropBaseURL,
                                    @Input("PostCreateIsoUrl") String postCreateIsoUrl,
                                    @Input("MediaURL") String mediaURL,
                                    @Input("RestGetParameters") String restGetParameters,
                                    @Input("RestPostBody") String restPostBody,
                                    @Output("ExpectedMediaCategory") String expectedMediaCategory,
                                    @Output("ExpectedMediaPath") String expectedMediaPath,
                                    @Output("ExpectedName") String expectedName,
                                    @Output("ExpectedNumber") String expectedNumber,
                                    @Output("ExpectedPlatform") String expectedPlatform,
                                    @Output("ExpectedPostResponse") String expectedPostResponse,
                                    @Output("ExpectedGetSiteContains") String expectedGetSiteContains
                                    )
    {
        setTestCase(Id, testCaseDescription);
        setTestStep("Check that REST Call responses are as expected");
        genericRESTOperator = genericRESTOperatorRegistry.provide(GenericRESTOperator.class);     
        HttpResponse response = genericRESTOperator.executeREST(hostName, getDropBaseURL, restGetParameters, "GET");
        logger.debug("Response: " + response.getResponseCode());
        String restBody = response.getBody() ;
        try {
            JSONArray responseArray = new JSONArray(restBody) ;
            for(int i = 0; i < responseArray.length(); i++){
                setTestStep("Verify Drop content is as expected");
                if(responseArray.getJSONObject(i).getString("name").equals(expectedName))
                {
                    assertEquals(expectedMediaCategory,responseArray.getJSONObject(i).getString("mediaCategory")) ;
                    assertEquals(expectedMediaPath,responseArray.getJSONObject(i).getString("mediaPath")) ;
                    assertEquals(expectedName,responseArray.getJSONObject(i).getString("name")) ;
                    assertEquals(expectedNumber,responseArray.getJSONObject(i).getString("number")) ;
                    assertEquals(expectedPlatform,responseArray.getJSONObject(i).getString("platform")) ;
                }
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
            assertTrue(false);
        }
        restPostBody += "&content=" + restBody ;
        HttpResponse postResponse  = genericRESTOperator.executeRESTPOST(hostName, postCreateIsoUrl,restPostBody,"POST");
        setTestStep("Verify create iso content response code as expected");
        assertEquals(expectedPostResponse, postResponse.getResponseCode().toString());
        HttpResponse siteResponse = genericRESTOperator.executeREST(hostName, mediaURL, null, "GET");
        expectedGetSiteContains = expectedGetSiteContains.replaceAll("'", "\"") ;
        setTestStep("Verify new iso content is added to ci portal");
        assertTrue(siteResponse.getBody().contains(expectedGetSiteContains));
        }
}