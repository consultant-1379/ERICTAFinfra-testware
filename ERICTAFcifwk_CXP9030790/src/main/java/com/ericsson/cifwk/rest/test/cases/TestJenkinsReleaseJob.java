package com.ericsson.cifwk.rest.test.cases;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.utils.*;
import com.ericsson.sut.test.operators.JenkinsRESTOperator;
import com.ericsson.sut.test.operators.JenkinsOperator;
import com.google.inject.Inject;

public class TestJenkinsReleaseJob extends TorTestCaseHelper implements TestCase {

    private JenkinsOperator jenkinsOperator;
    private HttpResponse response;
    private FileHandling fileHandler;
    private Map<String,String> details;
    private int sleepTime = 10;
    private int counter = 0;
    public String jenkinsJobConfigurationFileName = "";
    Logger logger = Logger.getLogger(JenkinsRESTOperator.class);
    @Inject
    OperatorRegistry<JenkinsOperator> operatorRegistry;

    @Context(context = {Context.REST})
    @DataDriven(name = "testJenkinsReleaseJob")
    @Test(groups={"Functional","Heartbeat","vAPP","Physical"})
    @TestId(id = "CIP-5680_Func_1", title = "Verify Creation of Jenkins Release Job configuration File dynamically using Release Job Template XML")
    public void testCreateJenkinsReleaseJobConfigFile(
                                    @Input("FileName") String fileName,
                                    @Input("JenkinsJobConfigurationFile") String jenkinsJobConfigurationFile,
                                    @Input("fileType") String fileType,
                                    @Input("_MIRROR_") String mirror,
                                    @Input("_REPO_") String repo,
                                    @Input("_GERRIT_") String gerrit,
                                    @Input("_ARTIFACTID_") String artifact,
                                    @Input("_GROUPID_") String groupId,
                                    @Input("_GERRITNAME_") String gerritName,
                                    @Input("_BUILDCOMMAND_") String buildCommand,
                                    @Input("_BRANCH_") String branch,
                                    @Input("_USER_") String user,
                                    @Output("ExpectedFileDeleted") boolean expectedConfFileCreation ){

        details = new HashMap<String, String>();
        details.put("_MIRROR_", mirror);
        details.put("_REPO_", repo);
        details.put("_ARTIFACTID_", artifact);
        details.put("_GROUPID_", groupId);
        details.put("_GERRIT_", gerrit);
        details.put("_GERRITNAME_", gerritName);
        details.put("_BUILDCOMMAND_", buildCommand);
        details.put("_BRANCH_", branch);
        details.put("_USER_", user);
        fileHandler = new FileHandling();
        setTestStep("Call the findAndReplaceInFile function in the filehandler Object to build up Temp Release Job Configuration File");
        File file1 =  FileHandling.getFileToDeploy(fileName);
        jenkinsJobConfigurationFileName =fileHandler.copyFindReplaceInFile(file1, jenkinsJobConfigurationFile, fileType, details);
        assertEquals(expectedConfFileCreation,(!jenkinsJobConfigurationFileName.equals("")));
    }

    @Context(context = {Context.REST})
    @DataDriven(name = "testJenkinsReleaseJob")
    @Test(groups={"Functional","Heartbeat","vAPP","Physical"})
    @TestId(id = "CIP-5680_Func_2", title = "Verify Creation of Jenkins Release Job using dynamically created jenkins Release Job configuration File")
    public void testCreateJenkinsReleaseJob(@Input("JenkinsHostName") String jenkinsHostName,
                                    @Input("JenkinsBaseDirectory") String jenkinsBaseDirectory,
                                    @Input("JenkinsJobName") String jenkinsJobName,
                                    @Output("ExpectedFileDeleted") boolean expectedFileDeletedResult,
                                    @Output("Expected") String expected ){

        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        setTestStep("Call the createJenkinsJob function in the jenkinsOperator Object to create the Jenkins Release Job based on a Temp Config file");
        response = jenkinsOperator.createJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsJobConfigurationFileName);
        assertEquals(expected,response.getResponseCode().toString());

        setTestStep("Call the deleteFile function in the FileHandling Object to delete the temp Release Job Configuration File");
        FileHandling filehandler = new FileHandling();
        boolean deleteFile = filehandler.deleteFile(jenkinsJobConfigurationFileName);
        assertEquals(expectedFileDeletedResult,deleteFile);
    }

    @Context(context = {Context.REST})
    @DataDriven(name = "testJenkinsReleaseJob")
    @Test(groups={"Functional","Heartbeat","vAPP","Physical"})
    @TestId(id = "CIP-5680_Func_3", title = "Verify Building of Jenkins Release Job using dynamically created jenkins Release Job configuration File")
    public void testBuildJenkinsReleaseJob(@Input("JenkinsHostName") String jenkinsHostName,
                                    @Input("JenkinsBaseDirectory") String jenkinsBaseDirectory,
                                    @Input("JenkinsJobName") String jenkinsJobName,
                                    @Input("JenkinsJobParameters") String jenkinsJobParameters,
                                    @Output("ExpectedBuildResult") String expectedBuildResult ){

        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        setTestStep("Call the buildJenkinsJob function in the jenkinsOperator Object to build the Jenkins Release Job based on a Temp Config file");
        response = jenkinsOperator.buildJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsJobParameters);
        assertEquals(expectedBuildResult,response.getResponseCode().toString());
    }

    @Context(context = {Context.REST})
    @DataDriven(name = "testJenkinsReleaseJob")
    @Test(groups={"Functional","Heartbeat","vAPP","Physical"})
    @TestId(id = "CIP-5680_Func_4", title = "Check the status of the currently building Jenkins Release Job and Report Status")
    public void testJenkinsReleaseJobStatus(@Input("JenkinsHostName") String jenkinsHostName,
                                     @Input("JenkinsBaseDirectory") String jenkinsBaseDirectory,
                                     @Input("JenkinsJobName") String jenkinsJobName,
                                     @Input("JenkinsStatusType") String jenkinsStatusType,
                                     @Input("Timeout") int timeout,
                                     @Output("Expected") String expected,
                                     @Output("ExpectedJobResult") String expectedJobResult,
                                     @Output("ExpectedJobDeletedFromQueue") boolean expectedJobDeletedFromQueue,
                                     @Output("JobAbortExpectedResult") String jobAbortExpectedResult ){

        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);

        setTestStep("Call the jenkinsJobQueueStatus function in the jenkinsOperator Object to check if the Release Job is in a build Queue");
        while(jenkinsOperator.jenkinsJobQueueStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName)==true){
            logger.debug("Jenkins Job: " + jenkinsJobName  + " is currently in Build Queue current timeout: " + (timeout - counter) + " seconds");
            counter = counter + sleepTime;
            sleep(sleepTime);
            if(counter == timeout){
                logger.error("Jenkins Job has been in Build Queue for over expected Timeout: " + timeout + " seconds, job has not yet Built");
                counter = 0;
                setTestStep("Call the jenkinsJobQueueStatus function in the jenkinsOperator Object to remove current build for queue as timeout has been reached");
                boolean deleteFromQueue = jenkinsOperator.deleteJenkinsPendingJobFromQueue(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);
                assertEquals(expectedJobDeletedFromQueue,deleteFromQueue);
                break;
            }
        }

        setTestStep("Call the jenkinsJobStatus function in the jenkinsOperator Object to check if job completes before Estimated build Time elapses, if not abort Job");
        int counter = 0;
        while(XMLHandler.getNodeValue("building", jenkinsOperator.jenkinsJobStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsStatusType).getBody()).equalsIgnoreCase("true")){
            response = jenkinsOperator.jenkinsJobStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsStatusType);
            if (counter <= (Integer.parseInt(XMLHandler.getNodeValue("estimatedDuration", response.getBody())) + 6000)){
                logger.debug("Jenkins Job: " + jenkinsJobName  + " is currently in Progress current Estimated Build time is: " + XMLHandler.getNodeValue("estimatedDuration", response.getBody()) + " seconds. Current Job Running Time is: " + counter + " seconds.");
                sleep(sleepTime);
                counter = counter + sleepTime;
            }else{
                logger.error("Jenkins Job: " + jenkinsJobName  + " has exceeded Estimated Build Time therefore Aborting Job.");
                counter = 0;
                setTestStep("Call the stopBuildingJenkinsJob function in the jenkinsOperator Object to abort Job as Estimated Build Time has elapsed");
                response = jenkinsOperator.stopBuildingJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);
                assertEquals(jobAbortExpectedResult,response.getResponseCode().toString());
                break;
            }
        }
        setTestStep("Call the getNodeValue function in the XMLHandler Object to get the Build Result");
        String jobResult = XMLHandler.getNodeValue("result", jenkinsOperator.jenkinsJobStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsStatusType).getBody());
        assertEquals(expectedJobResult,jobResult);
    }

    @Context(context = {Context.REST})
    @DataDriven(name = "testJenkinsReleaseJob")
    @Test(groups={"Functional","Heartbeat","vAPP","Physical"})
    @TestId(id = "CIP-5680_Func_5", title = "Get the Release Job Log File for later parsing")
    public void testGetJenkinsReleaseJobLog(@Input("JenkinsHostName") String jenkinsHostName,
                                 @Input("JenkinsBaseDirectory") String jenkinsBaseDirectory,
                                 @Input("JenkinsJobName") String jenkinsJobName,
                                 @Input("JenkinsStatusType") String jenkinsStatusType,
                                 @Output("Expected") String expected,
                                 @Input("InventoryPluginError") String inventoryPluginError,
                                 @Input("CIFWKMavenPluginError") String cifwkMavenPluginError,
                                 @Output("PluginExpectedOut") String pluginExpectedOut){

        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        setTestStep("Call the getJenkinsJobLog function in the jenkinsOperator Object to get a Build Job Log");
        response = jenkinsOperator.getJenkinsJobLog(jenkinsHostName,jenkinsBaseDirectory, jenkinsJobName, jenkinsStatusType);
        assertEquals(expected,response.getResponseCode().toString());
        DataHandler.setAttribute("Version", jenkinsSlaveHandler.parseTextForExpectedString(response.getBody(), "Version is ", "\n"));

        setTestStep("Verify that the Build Job Log does not contain any ERRORS resulting from a CI AXIS developed maven plugin");
        if (response.getBody().contains(inventoryPluginError) || response.getBody().contains(cifwkMavenPluginError)) {
            logger.error("Failure in CIFWk Plugin in Release building Release job");
            pluginExpectedOut = "NOK";
        }
        assertEquals(expected,pluginExpectedOut);
    }

    @Context(context = {Context.REST})
    @DataDriven(name = "testJenkinsReleaseJob")
    @Test(groups={"Functional","Heartbeat","vAPP","Physical"})
    @TestId(id = "CIP-5680_Func_6", title = "Verify the functionality of jenkinsOperator deleteJenkinsJob method")
    public void testDeleteJenkinsReleaseJob(@Input("JenkinsHostName") String jenkinsHostName,
                                     @Input("JenkinsBaseDirectory") String jenkinsBaseDirectory,
                                     @Input("JenkinsJobName") String jenkinsJobName,
                                     @Input("ExpectedJenkinsDelete") String [] expectedJenkinsDelete ){

        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        setTestStep("Call the deleteJenkinsJob function in the jenkinsOperator Object to get a clean up created jenkins jobs");
        response = jenkinsOperator.deleteJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);
        assertTrue(Arrays.asList(expectedJenkinsDelete).contains(response.getResponseCode().toString()));
    }
}
