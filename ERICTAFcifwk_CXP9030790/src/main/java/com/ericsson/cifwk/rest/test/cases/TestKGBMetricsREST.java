package com.ericsson.cifwk.rest.test.cases;


import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

public class TestKGBMetricsREST extends TorTestCaseHelper implements TestCase {
    private GenericRESTOperator genericRESTOperator;
    Logger logger = Logger.getLogger(TestKGBMetricsREST.class);
    @Inject
    private OperatorRegistry<GenericRESTOperator> genericRESTOperatorRegistry;

    @Context(context = {Context.REST})
    @DataDriven(name = "getKGBMetrics")
    @Test(groups = { "Functional", "vApp", "Physical" })
    @TestId(id = "CIP-6353_Func_1", title = "Test KGB Metrics to verify REST call,ciportal")
    public void verifyKGBMetricREST(@Input("Id") String Id,
                                    @Input("Description") String testCaseDescription,
                                    @Input("HostName") String hostName,
                                    @Input("BaseURLDirectory") String baseURLDirectory,
                                    @Input("dataBody") String dataBody,
                                    @Output("ExpectedReponse") String expectedReponse,
                                    @Output("ExpectedJSONArrayLength") int expectedJSONArrayLength,
                                    @Output("ExpectedName") String expectedName,
                                    @Output("ExpectedTotal") int expectedTotal,
                                    @Output("ExpectedPassedKGB") int expectedPassedKGB,
                                    @Output("ExpectedFailedKGB") int expectedFailedKGB,
                                    @Output("ExpectedNoKGB") int expectedNoKGB,
                                    @Output("ExpectedNumberDeliveries") int expectedNumberDeliveries,
                                    @Output("ExpectedDeliveriesFailed") int expectedDeliveriesFailed,
                                    @Output("ExpectedDeliveredPassed") int expectedDeliveredPassed,
                                    @Output("ExpectedDeliveredNoKGB") int expectedDeliveredNoKGB,
                                    @Output("ExpectedKGBResult") String expectedKGBResult,
                                    @Output("ExpectedDelivered") boolean expectedDelivered,
                                    @Output("ExpectedVersion") String expectedVersion) 
    {
        setTestCase(Id, testCaseDescription);
        setTestStep("Check that REST Call responses are as expected");
        
        genericRESTOperator = genericRESTOperatorRegistry.provide(GenericRESTOperator.class);
        
        HttpResponse response = null;
        
        response = genericRESTOperator.executeRESTPOST(hostName, baseURLDirectory,dataBody,"POST");
        
        String body = response.getBody();
        
        try {
            JSONArray responseArray = new JSONArray(body) ;
            setTestStep("Verify JSON Array is expected length");
            assertEquals(expectedJSONArrayLength, responseArray.length());
            for(int i = 0; i < responseArray.length(); i++){
                if(responseArray.getJSONObject(i).getString("name").equals(expectedName))
                {
                    JSONArray detailed = responseArray.getJSONObject(i).getJSONArray("Detailed") ;
                    for(int j = 0; j < detailed.length(); j++)
                    {
                        if(detailed.getJSONObject(j).getString("version").equals(expectedVersion))
                        {
                            setTestStep("Verify Detailed Results are as expected");
                            assertEquals(expectedKGBResult, detailed.getJSONObject(j).getString("KgbResult"));
                            assertEquals(expectedDelivered, detailed.getJSONObject(j).getBoolean("Delivered"));
                            assertEquals(expectedVersion, detailed.getJSONObject(j).getString("version"));
                            break;
                        }                 
                    }
                    
                    setTestStep("Verify Summary Results are as expected");
                    JSONObject summary = responseArray.getJSONObject(i).getJSONObject("Summary") ;
                    assertEquals(expectedTotal, summary.getInt("Total"));
                    assertEquals(expectedPassedKGB, summary.getInt("PassedKGB"));
                    assertEquals(expectedFailedKGB, summary.getInt("FailedKGB"));
                    assertEquals(expectedNoKGB, summary.getInt("NoKGBRan"));
                    assertEquals(expectedNumberDeliveries, summary.getInt("NumberOfDeliveries"));
                    assertEquals(expectedDeliveriesFailed, summary.getInt("DeliveredWithFailedKGB"));
                    assertEquals(expectedDeliveredPassed, summary.getInt("DeliveredWithPassedKGB"));
                    assertEquals(expectedDeliveredNoKGB, summary.getInt("DeliveredWithoutKGB"));
                }
            }
            
        } 
        catch (JSONException e) {
            e.printStackTrace();
            assertTrue(false);
        }

        setTestStep("Verify response is as expected");
        assertEquals(expectedReponse, response.getResponseCode().toString());
    	    	
    	
    }

}
