package com.ericsson.cloudPortal.rest.test.cases;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.testng.annotations.*;
import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.utils.cloud.GenericCloudFunctions;
import com.ericsson.sut.test.operators.GenericRESTOperator;
import com.google.inject.Inject;

public class TestGetRARestCommand extends TorTestCaseHelper implements TestCase {
    @Inject
    final String commandPropertyPrefix = "ciPortalJenkins.";
    private GenericRESTOperator genericRESTOperator;
    Logger logger = Logger.getLogger(TestGetRARestCommand.class);
    @Inject
    private OperatorRegistry<GenericRESTOperator> genericRESTOperatorRegistry;
        
    /**
     * @throws TimeoutException 
     * @throws IOException 
     * @throws InterruptedException 
     * @DESCRIPTION Verify the functionality SPP Get RA REST functionality
     * @VUsers 1
     * @PRIORITY MEDIUM
     * 
     */
        
    @Context(context = { Context.REST })
    @DataDriven(name = "getRARestCommands")
    @Test(groups = { "Functional", "Heartbeat", "vApp", "Physical" })
    @TestId(id = "CIP-6792_Func_1", title = "Check that the Cloud Portal REST call that returns all RA's in the SPP DB and their asociated Datacenters")
    public void verifySPPGetRARestFunctionality(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("HostName") String hostName,
            @Input("BaseURLDirectory") String baseURLDirectory,
            @Input("Format") String format,
            @Input("ExpectedParentResult") String expectedParentResult,
            @Input("ExpectedChildResult") String expectedChildResult,
            @Input("InputParentElementName") String inputParentElementName,
            @Input("InputChildElementName") String inputChildElementName,
            @Input("InputGrandchildElementName") String inputGrandchildElementName,
            @Input("ExpectedReponse") String expectedReponse,
            @Input("Result") boolean result) throws TimeoutException, IOException, InterruptedException
        {
        setTestCase(Id, testCaseDescription);
        
        setTestStep("Check that REST Call responses as expected");
        genericRESTOperator = genericRESTOperatorRegistry.provide(GenericRESTOperator.class);
        
        baseURLDirectory = baseURLDirectory + "." + format;
        
        HttpResponse response = genericRESTOperator.executeREST(hostName, baseURLDirectory, null, "GET");
        logger.debug("Response: " + response.getResponseCode());
        
        assertEquals(expectedReponse, response.getResponseCode().toString());
        
        if (expectedParentResult != null || expectedChildResult != null)
        {
            boolean validateResult[] = GenericCloudFunctions.validatedCloudPortalRestRAAndDataCenterXML(inputParentElementName,inputChildElementName,inputGrandchildElementName,expectedParentResult,expectedChildResult,response);
            setTestStep("Check that REST Call returns expected RA"); 
            assertEquals(result, validateResult[0]);
            setTestStep("Check that REST Call returns expected Datacenter in given RA"); 
            assertEquals(result, validateResult[1]);

        }        
    }
    
    /**
     * @throws TimeoutException 
     * @throws IOException 
     * @throws InterruptedException 
     * @DESCRIPTION Verify the functionality of SPP get DataCenter REST functionality
     * @VUsers 1
     * @PRIORITY MEDIUM
     * 
     */
        
    @Context(context = { Context.REST })
    @DataDriven(name = "getDataCenterRestCommands")
    @Test(groups = { "Functional", "Heartbeat", "vApp", "Physical" })
    @TestId(id = "CIP-6792_Func_2", title = "Check that the Cloud Portal REST call that returns all Datacenters in the SPP DB and their asociated RA's")
    public void verifySPPGetDataCeneterRestFunctionality(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("HostName") String hostName,
            @Input("BaseURLDirectory") String baseURLDirectory,
            @Input("Format") String format,
            @Input("ExpectedParentResult") String expectedParentResult,
            @Input("ExpectedChildResult") String expectedChildResult,
            @Input("InputParentElementName") String inputParentElementName,
            @Input("InputChildElementName") String inputChildElementName,
            @Input("InputGrandchildElementName") String inputGrandchildElementName,
            @Input("ExpectedReponse") String expectedReponse,
            @Input("Result") boolean result) throws TimeoutException, IOException, InterruptedException
        {
        setTestCase(Id, testCaseDescription);
        
        setTestStep("Check that REST Call responses as expected");
        genericRESTOperator = genericRESTOperatorRegistry.provide(GenericRESTOperator.class);

        baseURLDirectory = baseURLDirectory + "." + format;
        
        HttpResponse response = genericRESTOperator.executeREST(hostName, baseURLDirectory, null, "GET");
        logger.debug("Response: " + response.getResponseCode());
        
        assertEquals(expectedReponse, response.getResponseCode().toString());
        
        if (expectedParentResult != null || expectedChildResult != null)
        {
            boolean validateResult[] = GenericCloudFunctions.validatedCloudPortalRestRAAndDataCenterXML(inputParentElementName,inputChildElementName,inputGrandchildElementName,expectedParentResult,expectedChildResult,response);
            setTestStep("Check that REST Call returns expected Datacenter"); 
            assertEquals(result, validateResult[0]);
            setTestStep("Check that REST Call returns expected RA in given Datacenter"); 
            assertEquals(result, validateResult[1]);

        }        
    } 
    
    /**
     * @throws TimeoutException 
     * @throws IOException 
     * @throws InterruptedException 
     * @DESCRIPTION Verify the functionality of SPP to get SPP cloud Events
     * @VUsers 1
     * @PRIORITY MEDIUM
     * 
     */
        
    @Context(context = { Context.REST })
    @DataDriven(name = "getCloudEventsCommands")
    @Test(groups = { "Functional", "Heartbeat", "vApp", "Physical" })
    @TestId(id = "CIP-6792_Func_3", title = "Check that the Cloud Portal REST call that returns all Events")
    public void verifySPPGetEventsRestFunctionality(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("HostName") String hostName,
            @Input("BaseURLDirectory") String baseURLDirectory,
            @Input("Format") String format,
            @Input("JSONBody") String jsonBody,
            @Input("ExpectedReponse") String expectedReponse) throws TimeoutException, IOException, InterruptedException
        {
        setTestCase(Id, testCaseDescription);
        
        setTestStep("Check that REST Call responses as expected");
        genericRESTOperator = genericRESTOperatorRegistry.provide(GenericRESTOperator.class);
        
        baseURLDirectory = baseURLDirectory + "." + format;
        HttpResponse response = null;
        if (jsonBody == null){
            response = genericRESTOperator.executeREST(hostName, baseURLDirectory, null, "GET");
        }else{
            response = genericRESTOperator.executeRESTwithJSONBody(hostName, baseURLDirectory,"POST", jsonBody);
        }
        logger.debug("Response: " + response.getResponseCode());

        assertEquals(expectedReponse, response.getResponseCode().toString());
                
        }
    
}

