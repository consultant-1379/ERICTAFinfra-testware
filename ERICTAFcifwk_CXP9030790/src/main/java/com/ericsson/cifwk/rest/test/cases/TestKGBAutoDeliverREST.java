package com.ericsson.cifwk.rest.test.cases;

import java.io.File;
import java.util.Arrays;

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
import com.ericsson.cifwk.utils.FileHandling;
import com.ericsson.sut.test.operators.GenericRESTOperator;
import com.ericsson.sut.test.operators.JenkinsRESTOperator;
import com.google.inject.Inject;

public class TestKGBAutoDeliverREST extends TorTestCaseHelper implements TestCase{


    private GenericRESTOperator genericRESTOperator;
    Logger logger = Logger.getLogger(TestKGBAutoDeliverREST.class);
    @Inject
    private OperatorRegistry<GenericRESTOperator> genericRESTOperatorRegistry;
    private HttpResponse response;
    private JenkinsRESTOperator jenkinsOperator = new JenkinsRESTOperator();
    private String found;

    @Context(context = {Context.REST})
    @DataDriven(name = "createJenkinsJobRestAutoDeliver")
    @Test(groups = { "Functional" })
    @TestId(id = "CIP-1718_Func_1", title = "Create Jenkins Jobs for the KGB Auto Deliver")
    public void testingAutoDeliverJenkinsJobCreate(
            @Input("Id") String Id,
            @Input("Description") String testCaseDescription,
            @Input("JenkinsHostName") String jenkinsHostName,
            @Input("JenkinsBaseDirectory") String jenkinsBaseDirectory,
            @Input("JenkinsJobName") String jenkinsJobName,
            @Input("JenkinsJobConfigurationFile") String jenkinsJobConfigurationFileName,
            @Output("Expected") String expectedResult){
        setTestCase(Id, testCaseDescription);
        File jenkinsJobConfigFileName =  FileHandling.getFileToDeploy(jenkinsJobConfigurationFileName);
        jenkinsJobConfigurationFileName = jenkinsJobConfigFileName.getAbsolutePath();
        setTestStep("Call the buildJenkinsJob function in the jenkinsOperator Object to create the Jenkins Job");
        response = jenkinsOperator.createJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsJobConfigurationFileName);
        assertEquals(expectedResult,response.getResponseCode().toString());
    }


    @Context(context = {Context.REST})
    @DataDriven(name = "testAutoDeliver")
    @Test(groups = { "Functional" })
    @TestId(id = "CIP-1718_Func_2", title = "Testing the deliver of package to single drop")
    public void testingAutoDeliveryREST(@Input("Id") String Id,
                                    @Input("Description") String testCaseDescription,
                                    @Input("HostName") String hostName,
                                    @Input("BaseURLDirectory") String baseURLDirectory,
                                    @Input("Parameters") String parameters,
                                    @Input("JenkinsJobName") String jenkinsJobName,
                                    @Output("ExpectedMediaCategory") String expectedMediaCategory,
                                    @Output("ExpectedMediaPath") String expectedMediaPath,
                                    @Output("ExpectedName") String expectedName,
                                    @Output("ExpectedNumber") String expectedNumber,
                                    @Output("ExpectedVersion") String expectedVersion,
                                    @Output("ExpectedPlatform") String expectedPlatform,
                                    @Output("ExpectedResult") String expectedResult,
                                    @Input("Type") String type,
                                    @Input("Pause") long pause){
        setTestCase(Id, testCaseDescription);
        setTestStep("Check that REST Call responses are as expected");
        genericRESTOperator = genericRESTOperatorRegistry.provide(GenericRESTOperator.class);
        found = "FALSE";
        if (type.equals("GET")){
            try {
                Thread.sleep(pause);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            response = genericRESTOperator.executeREST(hostName, baseURLDirectory, parameters, "GET");
            logger.debug("Response: " + response.getResponseCode());
            String restBody = response.getBody();
            try {
                JSONArray responseArray = new JSONArray(restBody) ;
                for(int i = 0; i < responseArray.length(); i++){
                    setTestStep("Verify Drop content is as expected");
                    if(responseArray.getJSONObject(i).getString("name").equals(expectedName)){
                        found = "TRUE";
                        assertEquals(expectedMediaCategory,responseArray.getJSONObject(i).getString("mediaCategory")) ;
                        assertEquals(expectedMediaPath,responseArray.getJSONObject(i).getString("mediaPath")) ;
                        assertEquals(expectedName,responseArray.getJSONObject(i).getString("name"));
                        assertEquals(expectedNumber,responseArray.getJSONObject(i).getString("number"));
                        assertEquals(expectedVersion,responseArray.getJSONObject(i).getString("version"));
                        assertEquals(expectedPlatform,responseArray.getJSONObject(i).getString("platform")) ;
                    }
                }
            }
            catch(JSONException e)
            {
              e.printStackTrace();
              assertTrue(false);
            }
            assertEquals(expectedResult, found);
            found = "FALSE";
        }else{
            setTestStep("Call the buildJenkinsJob function in the jenkinsOperator Object to build the Jenkins Job");
            response = jenkinsOperator.buildJenkinsJob(hostName, baseURLDirectory, jenkinsJobName, parameters);
            assertEquals(expectedResult,response.getResponseCode().toString());
        }
    }

    @Context(context = {Context.REST})
    @DataDriven(name = "testAutoDeliverMulti")
    @Test(groups = { "Functional" })
    @TestId(id = "CIP-1718_Func_3", title = "Testing the auto delivery of multiple packages to delivered to multiple drops")
    public void testingAutoDeliveryMultiREST(@Input("Id") String Id,
                                    @Input("Description") String testCaseDescription,
                                    @Input("HostName") String hostName,
                                    @Input("BaseURLDirectory") String baseURLDirectory,
                                    @Input("Parameters") String parameters,
                                    @Input("JenkinsJobName") String jenkinsJobName,
                                    @Output("ExpectedMediaCategory") String expectedMediaCategory,
                                    @Output("ExpectedMediaPath") String expectedMediaPath,
                                    @Output("ExpectedName") String expectedName,
                                    @Output("ExpectedNumber") String expectedNumber,
                                    @Output("ExpectedVersion") String expectedVersion,
                                    @Output("ExpectedPlatform") String expectedPlatform,
                                    @Output("ExpectedResult") String expectedResult,
                                    @Input("Type") String type,
                                    @Input("Pause") long pause){
        setTestCase(Id, testCaseDescription);
        setTestStep("Check that REST Call responses are as expected");
        genericRESTOperator = genericRESTOperatorRegistry.provide(GenericRESTOperator.class);
        found = "FALSE";
        if (type.equals("GET")){
            try {
                Thread.sleep(pause);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            String[] names = expectedName.split(",");
            String[] numbers = expectedNumber.split(",");
            String[] mediaCats = expectedMediaCategory.split(",");
            String[] mediaPaths = expectedMediaPath.split(",");
            String[] versions = expectedVersion.split(",");
            String[] platforms = expectedPlatform.split(",");
            String[] expectedResults = expectedResult.split(",");
            response = genericRESTOperator.executeREST(hostName, baseURLDirectory, parameters, "GET");
            logger.debug("Response: " + response.getResponseCode());
            String restBody = response.getBody();
            try {
                JSONArray responseArray = new JSONArray(restBody) ;
                for (int j = 0; j < names.length; j++){
                    for (int i = 0; i < responseArray.length(); i++) {
                        setTestStep("Verify Drop content is as expected");
                        if (responseArray.getJSONObject(i).getString("name").equals(names[j].toString())) {
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
            }
            catch(JSONException e)
            {
              e.printStackTrace();
              assertTrue(false);
            }
        }else{
            setTestStep("Call the buildJenkinsJob function in the jenkinsOperator Object to build the Jenkins Job");
            response = jenkinsOperator.buildJenkinsJob(hostName, baseURLDirectory, jenkinsJobName, parameters);
            assertEquals(expectedResult,response.getResponseCode().toString());
        }
    }

    @Context(context = {Context.REST})
    @DataDriven(name = "deleteJenkinsJobRestAutoDeliver")
    @Test(groups={"Functional"})
    @TestId(id = "CIP-1718_Func_4", title = "Delete KGB Jenkins Jobs for the Auto Deliver")
    public void testingAutoDeliverJenkinsJobDelete(
            @Input("Id") String Id,
            @Input("Description") String testCaseDescription,
            @Input("JenkinsHostName") String jenkinsHostName,
            @Input("JenkinsBaseDirectory") String jenkinsBaseDirectory,
            @Input("JenkinsJobName") String jenkinsJobName,
            @Output("Expected") String [] expectedJenkinsDelete ){
        setTestCase(Id, testCaseDescription);
        setTestStep("Call the deleteJenkinsJob function in the jenkinsOperator Object to get a clean up created jenkins jobs");
        response = jenkinsOperator.deleteJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);
        assertTrue(Arrays.asList(expectedJenkinsDelete).contains(response.getResponseCode().toString()));
    }
}
