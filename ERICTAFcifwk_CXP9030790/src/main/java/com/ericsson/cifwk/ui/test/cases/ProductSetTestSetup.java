package com.ericsson.cifwk.ui.test.cases;

import java.io.File;
import java.io.IOException;
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
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.TimeoutException;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.utils.*;
import com.ericsson.sut.test.operators.CliCommandHelperCliOperator;
import com.ericsson.sut.test.operators.CliCommandHelperOperator;
import com.ericsson.sut.test.operators.JenkinsRESTOperator;
import com.ericsson.sut.test.operators.JenkinsOperator;
import com.google.inject.Inject;

public class ProductSetTestSetup extends TorTestCaseHelper implements TestCase {

    private JenkinsOperator jenkinsOperator;
    public static final String CLIHOST = "ciportal";

    private HttpResponse response;
    private FileHandling fileHandler;
    private Map<String,String> details;
    private int sleepTime = 10;
    private int counter = 0;
    private Host host = DataHandler.getHostByName(CLIHOST);
    Logger logger = Logger.getLogger(JenkinsRESTOperator.class);
    @Inject
    private OperatorRegistry<JenkinsOperator> operatorRegistry;
    @Inject
    private OperatorRegistry<CliCommandHelperOperator> cliToolProvider;

    @TestId(id = "CIP-6191_Func_2", title = "Setup the Database with relevant information to run Product Sets Test cases")
    @Context(context = {Context.CLI})
    @Test(groups={"Functional","Acceptance"})
    @DataDriven(name = "productSetMgmtCmds")
    public void productSetInformationSetUp(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("host") String hostname,
            @Input("command")String command,
            @Input("args")String args,
            @Output("expectedOut") String expectedOut,
            @Output("expectedErr") String expectedErr,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeOut) throws TimeoutException, IOException, InterruptedException{
        setTestCase(Id, testCaseDescription);
        CliCommandHelperCliOperator cmdHelper = (CliCommandHelperCliOperator) cliToolProvider.provide(CliCommandHelperOperator.class);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, args, timeOut);
        setTestStep("Verifying exit value");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
        setTestStep("Verifying standard output of command");
        assertEquals (true, commandResults.get("stdOut").toString().contains(expectedOut));
    }
    
    @Context(context = {Context.REST})
    @DataDriven(name = "productSetJenkinsJob")
    @Test(groups={"Functional","Acceptance"})
    @TestId(id = "CIP-6191_Func_2a", title = "Create Temporary Config File Based on Jenkins Job Template")
    public void testCreateJenkinsProductSetJobConfigFile(
                                    @Input("templateConfigFile") String templateConfigFile,
                                    @Input("templateOutputConfigFile") String templateOutputFile,
                                    @Input("fileType") String fileType,
                                    @Input("artifactId") String artifactId,
                                    @Input("groupId") String groupId,
                                    @Input("version") String version,
                                    @Input("clueLevelStart1") String clueStart1,
                                    @Input("clueLevelStart2") String clueStart2,
                                    @Input("clueLevelFinish1") String clueFinish1,
                                    @Input("clueLevelFinish2") String clueFinish2,
                                    @Input("clueLevelStartResult") String clueStartResult,
                                    @Input("clueLevelFinishResult") String clueFinishResult,
                                    @Input("user") String user,
                                    @Output("expectedResult") boolean expectedResult ){
        
        details = new HashMap<String, String>();
        details.put("_ARTIFACTID_", artifactId);
        details.put("_GROUPID_", groupId);
        details.put("_VERSION_", version);
        details.put("_CLUELEVELSTART1_", clueStart1);
        details.put("_CLUELEVELSTART2_", clueStart2);
        details.put("_CLUELEVELSTARTRESULT_", clueStartResult);
        details.put("_CLUELEVELFINISH1_", clueFinish1);
        details.put("_CLUELEVELFINISH2_", clueFinish2);
        details.put("_CLUELEVELFINISHRESULT_", clueFinishResult);
        details.put("_USER_", user);
        fileHandler = new FileHandling();

        setTestStep("Call the findAndReplaceInFile function in the filehandler Object to build up Temp Job Configuration File");
        File file1 =  FileHandling.getFileToDeploy(templateConfigFile);
        fileHandler.copyFindReplaceInFile(file1, templateOutputFile, fileType, details);
        assertEquals(expectedResult,true);
    }

    @Context(context = {Context.REST})
    @DataDriven(name = "productSetJenkinsJob")
    @Test(groups={"Functional","Acceptance"})
    @TestId(id = "CIP-6191_Func_2b", title = "Verify Creation of Jenkins Product Set Job using dynamically created jenkins Product Set Job configuration File")
    public void testCreateJenkinsProductSetJob(@Input("jenkinsHostName") String jenkinsHostName,
                                    @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                    @Input("jenkinsJobName") String jenkinsJobName,
                                    @Input("templateOutputConfigFile") String templateOutputConfigFile, 
                                    @Output("expectedFileDeleted") boolean expectedFileDeletedResult,
                                    @Output("expectedString") String expected ){

        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        setTestStep("Call the createJenkinsJob function in the jenkinsOperator Object to create the Jenkins Product Set Job based on a Temp Config file");
        response = jenkinsOperator.createJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, templateOutputConfigFile);
        assertEquals(expected,response.getResponseCode().toString());
        
        setTestStep("Call the deleteFile function in the FileHandling Object to delete the temp Release Job Configuration File");
        FileHandling filehandler = new FileHandling();
        boolean deleteFile = filehandler.deleteFile(templateOutputConfigFile);
        assertEquals(expectedFileDeletedResult,deleteFile);
    }
    
    @Context(context = {Context.REST})
    @DataDriven(name = "productSetJenkinsJob")
    @Test(groups={"Functional","Acceptance"})
    @TestId(id = "CIP-6191_Func_2c", title = "Verify Building of Jenkins Product Set Job using dynamically created jenkins configuration File")
    public void testBuildJenkinsProductSetJob(@Input("jenkinsHostName") String jenkinsHostName,
                                    @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                    @Input("jenkinsJobName") String jenkinsJobName,  
                                    @Input("jenkinsJobParameters") String jenkinsJobParameters,
                                    @Output("expectedBuildResult") String expectedBuildResult ){

        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        setTestStep("Call the buildJenkinsJob function in the jenkinsOperator Object to build the Jenkins Product Set Job based on a Temp Config file");
        response = jenkinsOperator.buildJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsJobParameters);
        assertEquals(expectedBuildResult,response.getResponseCode().toString());
    }
    
    @Context(context = {Context.REST})
    @DataDriven(name = "productSetJenkinsJob")
    @Test(groups={"Functional","Acceptance"})
    @TestId(id = "CIP-6191_Func_2d", title = "Check the status of the currently building Jenkins Product Set Job and Report Status")
    public void testStatusJenkinsProductSetJob(@Input("jenkinsHostName") String jenkinsHostName,
                                     @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                     @Input("jenkinsJobName") String jenkinsJobName,  
                                     @Input("jenkinsStatusType") String jenkinsStatusType,
                                     @Input("timeout") int timeout,
                                     @Output("expectedString") String expected,
                                     @Output("expectedJobResult") String expectedJobResult,
                                     @Output("expectedJobDeletedFromQueue") boolean expectedJobDeletedFromQueue,  
                                     @Output("jobAbortExpectedResult") String jobAbortExpectedResult ){
        
        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        
        setTestStep("Call the jenkinsJobQueueStatus function in the jenkinsOperator Object to check if the Product Set Job is in a build Queue");
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
    @DataDriven(name = "productSetJenkinsJob")
    @Test(groups={"Functional","Acceptance"})
    @TestId(id = "CIP-6191_Func_2e", title = "Verify that the Product Set Job has been deleted")
    public void testDeleteJenkinsProductSetJob(@Input("jenkinsHostName") String jenkinsHostName,
                                     @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                     @Input("jenkinsJobName") String jenkinsJobName,  
                                     @Output("expectedJenkinsDelete") String expectedJenkinsDelete ){
        
        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        setTestStep("Call the deleteJenkinsJob function in the jenkinsOperator Object to get a clean up created jenkins jobs");
        response = jenkinsOperator.deleteJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);          
        assertEquals(expectedJenkinsDelete,response.getResponseCode().toString());
    }
}
