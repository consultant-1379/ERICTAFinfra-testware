package com.ericsson.cifwk.rest.test.cases;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.utils.jenkinsSlaveHandler;
import com.ericsson.cifwk.utils.cloud.CloudResponseObject;
import com.ericsson.sut.test.operators.*;
import com.google.inject.Inject;

public class TestJenkinsRESTOperators extends TorTestCaseHelper implements TestCase {

    ArrayList<String> jenkinsSlaveList = new ArrayList<String>();
    private JenkinsOperator jenkinsOperator;
    private HttpResponse response;
    private int sleepTime = 10;
    private int counter = 0;
    
    Logger logger = Logger.getLogger(JenkinsRESTOperator.class);
    @Inject
    OperatorRegistry<JenkinsOperator> operatorRegistry;
    @Inject
    OperatorRegistry<CloudPortalOperator> cloudOperatorRegistry;
    
    @Context(context = { Context.REST })
    @DataDriven(name = "createJenkinsJobRestCommands")
    @Test(groups = { "Functional", "Heartbeat", "vApp", "Physical" })
    @TestId(id = "CIP-5728_Func_1", title = "Verify the functionality of jenkinsOperator createJenkinsJob method")
    public void testCreateJenkinsJob(@Input("jenkinsHostName") String jenkinsHostName,
                                        @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                        @Input("jenkinsJobName") String jenkinsJobName,
                                        @Input("jenkinsJobConfigurationFile") String jenkinsJobConfigurationFileName,
                                        @Output("Expected") String expected) {

        setTestStep("Run the rest call to create a Jenkins job on Jenkins");
        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        response = jenkinsOperator.createJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsJobConfigurationFileName);

        assertEquals(expected, response.getResponseCode().toString());
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "buildJenkinsJobRestCommands")
    @Test(groups = { "Functional", "Heartbeat", "vApp", "Physical" })
    @TestId(id = "CIP-5728_Func_2", title = "Verify the functionality of jenkinsOperator buildJenkinsJob method")
    public void testBuildJenkinsJob(@Input("jenkinsHostName") String jenkinsHostName,
                                        @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                        @Input("jenkinsJobName") String jenkinsJobName,
                                        @Input("jenkinsJobParameters") String jenkinsJobParameters,
                                        @Output("Expected") String expected) {

        setTestStep("Run the rest call to build a Jenkins Job");
        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        response = jenkinsOperator.buildJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsJobParameters);
        sleep(sleepTime);
        logger.debug("Response: " + response.getResponseCode());

        assertEquals(expected, response.getResponseCode().toString());
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "jenkinsJobStatusRestCommands")
    @Test(groups = { "Functional", "Heartbeat", "vApp", "Physical" })
    @TestId(id = "CIP-5728_Func_3", title = "Verify the functionality of jenkinsOperator jenkinsJobQueueStatus method and jenkinsJobStatus method")
    public void testJenkinsJobStatus(@Input("jenkinsHostName") String jenkinsHostName, 
                                        @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                        @Input("jenkinsJobName") String jenkinsJobName,
                                        @Input("jenkinsStatusType") String jenkinsStatusType,
                                        @Input("timeout") int timeout,
                                        @Output("Expected") String expected) {

        setTestStep("Run the rest call to get status of a Jenkins job");
        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);

        while (jenkinsOperator.jenkinsJobQueueStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName) == true) {
            logger.info("Jenkins Job: " + jenkinsJobName + " is currently in Build Queue current timeout: " + (timeout - counter) + " seconds");
            counter = counter + sleepTime;
            sleep(sleepTime);
            if (counter == timeout) {
                logger.error("Jenkins Job has been in Build Queue for over expected Timeout: " + timeout + " seconds, job has not yet Built");
                counter = 0;
                break;
            }
        }

        response = jenkinsOperator.jenkinsJobStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsStatusType);
        sleep(sleepTime);
        logger.debug("Response Code: " + response.getResponseCode());
        logger.debug("Response Body: " + response.getBody());
        assertEquals(expected, response.getResponseCode().toString());
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "jenkinsJobStatusRestCommands")
    @Test(groups = { "Functional", "Heartbeat", "vApp", "Physical" })
    @TestId(id = "CIP-5728_Func_4", title = "Verify the functionality of jenkinsOperator getJenkinsJobLog method")
    public void testGetJenkinsJobLog(@Input("jenkinsHostName") String jenkinsHostName,
                                        @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                        @Input("jenkinsJobName") String jenkinsJobName,
                                        @Input("jenkinsStatusType") String jenkinsStatusType,
                                        @Output("Expected") String expected) {

        setTestStep("Run the rest call to get a log from a Jenkins job");
        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        response = jenkinsOperator.getJenkinsJobLog(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsStatusType);
        logger.debug("Response: " + response.getResponseCode());

        assertEquals(response.getResponseCode().toString(), expected);
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "deleteJenkinsJobRestCommands")
    @Test(groups = { "Functional", "Heartbeat", "vApp", "Physical" })
    @TestId(id = "CIP-5728_Func_5", title = "Verify the functionality of jenkinsOperator deleteJenkinsJob method")
    public void testDeleteJenkinsJob(@Input("jenkinsHostName") String jenkinsHostName,
                                        @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                        @Input("jenkinsJobName") String jenkinsJobName,
                                        @Output("Expected") String expected) {

        setTestStep("Run the rest call to delete a job from Jenkins");
        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        response = jenkinsOperator.deleteJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);
        logger.debug("Response: " + response.getResponseCode());

        assertEquals(expected, response.getResponseCode().toString());
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "checkSlaves")
    @Test(groups = { "Functional", "Heartbeat", "vApp", "Physical" })
    @TestId(id = "CIP-5728_Func_6", title = "Verify the expected amount of slaves are in jenkins")
    public void testJenkinsSlavesExist(@Input("jenkinsHostName") String jenkinsHostName, 
                                        @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                        @Input("jenkinsLabel") String jenkinsLabel, 
                                        @Input("numberJenkinsSlavesExpected") int numberJenkinsSlavesExpected,
                                        @Input("timeoutGetSlaves") int timeoutGetSlaves,
                                        @Output("ExpectedAnswer") String expectedAnswer) {
        setTestStep("Run the rest call to get all Jenkins slaves from Jenkins");
        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        jenkinsSlaveList = jenkinsOperator.getAllJenkinsSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsLabel,timeoutGetSlaves);
        for (String slave : jenkinsSlaveList) {
            logger.debug("Slave Name: " + slave);
            jenkinsSlaveHandler.addToJenkinsSlaveHistory(slave);
        }
        jenkinsSlaveHandler.setCurrentJenkinsSlaves(jenkinsSlaveList);

        assertEquals(Boolean.parseBoolean(expectedAnswer), jenkinsSlaveList.size() >= 0);
        assertTrue(jenkinsSlaveList.size() == numberJenkinsSlavesExpected);
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "createQueuedJenkinsJobRestCommands")
    @Test(groups = { "Functional", "Heartbeat", "vApp", "Physical" })
    @TestId(id = "CIP-5728_Func_7", title = "Verify the functionality of jenkinsOperator createJenkinsJob method to create queued jobs")
    public void testCreateQueuedJenkinsJob(
                                        @Input("jenkinsHostName") String jenkinsHostName,
                                        @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                        @Input("jenkinsJobName") String jenkinsJobName,
                                        @Input("jenkinsJobConfigurationFile") String jenkinsJobConfigurationFileName,
                                        @Output("Expected") String expected) {

        setTestStep("Run the rest call to create a job in queue");
        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        response = jenkinsOperator.createJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsJobConfigurationFileName);

        assertEquals(response.getResponseCode().toString(), expected);
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "testQueue")
    @Test(groups = { "Functional", "Heartbeat", "vApp", "Physical" })
    @TestId(id = "CIP-5728_Func_8", title = "Verify that we are able to delete jobs from the queue")
    public void testQueuedItemCanBeRemoved(
                                        @Input("jenkinsHostName") String jenkinsHostName, 
                                        @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory, 
                                        @Input("jenkinsJobName") String jenkinsJobName, 
                                        @Output("Expected") String expected) {
        setTestStep("Run the rest call to delete items from queue");
        boolean deleted = false;
        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        deleted = jenkinsOperator.deleteJenkinsPendingJobFromQueue(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);
        assertEquals(Boolean.parseBoolean(expected), deleted);
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "testBuildSlaveIsGone")
    @Test(groups = { "Functional", "Heartbeat", "vApp", "Physical" })
    @TestId(id = "CIP-5728_Func_9", title = "Verify that Jenkins Slave is deleted from Jenkins")
    public void testBuildSlaveIsGoneFromJenkinsAndCloud(
                                        @Input("jenkinsHostName") String jenkinsHostName,
                                        @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                        @Input("jenkinsSlaveLabel") String jenkinsSlaveLabel, 
                                        @Input("jenkinsJobName") String jenkinsJobName,
                                        @Input("jenkinsJobType") String jenkinsJobType,
                                        @Input("cloudCatalogName") String cloudCatalogName,
                                        @Input("timeoutVappTemplates") int timeoutVappTemplates,
                                        @Input("timeoutGetSlaves") int timeoutGetSlaves,
                                        @Input("numRetries") int numRetries,
                                        @Input("retryTimeout") int retryTimeout,
                                        @Output("httpResponseVappTemplates") String httpResponseVappTemplates,
                                        @Output("expectedVappTemplateStatus") String expectedVappTemplateStatus,
                                        @Output("expectedSlaveExists") String expectedSlaveExists) {

        setTestStep("Run the rest call to get all Jenkins slaves from Jenkins");
        CloudPortalOperator cloudPortalOperator = cloudOperatorRegistry.provide(CloudPortalOperator.class);
        
        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        String lastBuildSlave = jenkinsOperator.getJenkinsJobSlaveName(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsJobType);
        assertFalse(lastBuildSlave == null);
        logger.info("Last Build Slave = : " + lastBuildSlave);
        jenkinsSlaveList = jenkinsOperator.getAllJenkinsSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel,timeoutGetSlaves);
        for (String slave : jenkinsSlaveList) {
            logger.debug("Slave Name: " + slave);
            jenkinsSlaveHandler.addToJenkinsSlaveHistory(slave);
        }

        jenkinsSlaveHandler.setCurrentJenkinsSlaves(jenkinsSlaveList);
        boolean slaveExists = jenkinsSlaveHandler.checkIfSlaveExists(lastBuildSlave);
        for (int i = 1; i <= numRetries; i++) {
            if (slaveExists) {
                sleep(retryTimeout);
                logger.info("Checking Slave is gone retry number: " + i);
                jenkinsSlaveList = jenkinsOperator.getAllJenkinsSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel,timeoutGetSlaves);
                jenkinsSlaveHandler.setCurrentJenkinsSlaves(jenkinsSlaveList);
                slaveExists = jenkinsSlaveHandler.checkIfSlaveExists(lastBuildSlave);
            }
        }
        assertEquals(slaveExists, Boolean.parseBoolean(expectedSlaveExists));
        CloudResponseObject listDatacentersResponse = new CloudResponseObject(cloudPortalOperator.listOrgVdcs(timeoutVappTemplates));

        assertEquals("OK", listDatacentersResponse.getResponseCode());
        assertFalse(listDatacentersResponse.doesTagExist(lastBuildSlave));

        setTestStep("Run the rest call to check that template exists in the cloud");

        String vappTemplateName = jenkinsOperator.getvAppTemplateNameFromConsole(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsJobType);
        CloudResponseObject listVappTemplatesResponse = new CloudResponseObject(cloudPortalOperator.listVappTemplatesInCatalog(cloudCatalogName, timeoutVappTemplates));
        assertEquals(listVappTemplatesResponse.getResponseCode(), httpResponseVappTemplates);
        assertTrue(listVappTemplatesResponse.doesTagExist("vapptemplates"));
        String vappTemplateStatus = listVappTemplatesResponse.getValueBySibling("vapptemplates", "vapptemplate_name", vappTemplateName, "status");
        assertEquals(vappTemplateStatus, expectedVappTemplateStatus);

        String vappTemplateID = listVappTemplatesResponse.getValueBySibling("vapptemplates", "vapptemplate_name", vappTemplateName, "vapptemplate_id");
        assertTrue(vappTemplateID != null);
        HttpResponse res = cloudPortalOperator.deleteVappTemplate(vappTemplateID, 600);
        assertEquals("OK", res.getResponseCode().toString());
    }
    
}
