package com.ericsson.cifwk.rest.test.cases;

import java.io.File;
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
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.utils.*;
import com.ericsson.sut.test.operators.JenkinsRESTOperator;
import com.ericsson.sut.test.operators.JenkinsOperator;
import com.google.inject.Inject;

public class TestKGBReadyJenkinsJob extends TorTestCaseHelper implements TestCase {
    
    private JenkinsOperator jenkinsOperator;
    private HttpResponse response;
    private FileHandling fileHandler;
    private Map<String,String> details;
    private Host hostname;
    private String parameterList;
    private boolean deleteFile = false;
    Logger logger = Logger.getLogger(JenkinsRESTOperator.class);
    
    @Inject
    OperatorRegistry<JenkinsOperator> operatorRegistry;
   
    @Context(context = {Context.REST})
    @DataDriven(name = "testKGBReadyFunctionality")
    @Test(groups={"Functional","Heartbeat","vAPP","Physical"})
    @TestId(id = "CIP-6143_Func_1", title = "Verify Creation of Jenkins KGB Ready Job configuration File dynamically using KGB Ready Job Template XML")
    public void testKGBReadyJob(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("Host") String host,
            @Input("JenkinsBaseDirectory") String jenkinsBaseDirectory,
            @Input("JenkinsJobName") String jenkinsJobName,
            @Input("LocalTemplateFile") String localTemplateFile,
            @Input("JenkinsJobConfigurationFileTemp") String jenkinsJobConfigurationFileTemp,
            @Input("fileType") String fileType,
            @Input("_ARTIFACTID_") String artifactID,
            @Input("_VERSION_") String version,
            @Input("_GROUPID_") String groupID,
            @Input("_COMMENT_") String comment,
            @Input("Expected") String expected,
            @Input("ExpectedMovedResponse") String expectedMovedResponse,
            @Output("ExpectedResponse") boolean expectedResponse ){

        setTestCase(Id, testCaseDescription);
        hostname=DataHandler.getHostByName(host);
        fileHandler = new FileHandling();
        jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
        parameterList = "ARTIFACTID=" + artifactID + ",VERSION=" + version + ",GROUPID=" + groupID + ",USERNAME=" + hostname.getUser() + ",COMMENT=" + comment; 

        details = new HashMap<String, String>();
        details.put("_ARTIFACTID_", artifactID);
        details.put("_VERSION_", version);
        details.put("_GROUPID_", groupID);
        details.put("_USERNAME_", hostname.getUser());
        details.put("_COMMENT_", comment);
        details.put("_HOST_", hostname.getIp());
        details.put("_JENKINSBASEDIRECTORY_",jenkinsBaseDirectory);

        setTestStep("Call the findAndReplaceInFile function in the filehandler Object to build up Temp KGBReady JobConfiguration File");
        File jenkinsTemplate =  FileHandling.getFileToDeploy(localTemplateFile);
        jenkinsJobConfigurationFileTemp = fileHandler.copyFindReplaceInFile(jenkinsTemplate, jenkinsJobConfigurationFileTemp, fileType, details);
        sleep(10);
        assertEquals(expectedResponse,true);

        setTestStep("Call the createJenkinsJob function in the jenkinsOperator Object to create the KGBReady Job based on a Temp Config file");
        response = jenkinsOperator.createJenkinsJob(host, jenkinsBaseDirectory, jenkinsJobName, jenkinsJobConfigurationFileTemp);
        sleep(10);
        assertEquals(expected,response.getResponseCode().toString());

        setTestStep("Call the deleteFile function in the FileHandling Object to delete the temp KGBReady Job Configuration File");
        FileHandling filehandler = new FileHandling();
        try{
            deleteFile = filehandler.deleteFile(jenkinsJobConfigurationFileTemp);
        }catch (Exception error){
            System.out.println("issue deleting file");
            error.printStackTrace();
        }
        sleep(10);
        assertEquals(expectedResponse,deleteFile);

        setTestStep("Call the buildJenkinsJob function in the jenkinsOperator Object to build the Jenkins KGBReady Job based on a Temp Config file");
        response = jenkinsOperator.buildJenkinsJob(host, jenkinsBaseDirectory, jenkinsJobName, parameterList);
        sleep(15);
        assertEquals(expectedMovedResponse,response.getResponseCode().toString());

        setTestStep("Call the deleteJenkinsJob function in the jenkinsOperator Object to get a clean up created jenkins jobs");
        response = jenkinsOperator.deleteJenkinsJob(host, jenkinsBaseDirectory, jenkinsJobName);   
        sleep(10);
        assertEquals(expectedMovedResponse,response.getResponseCode().toString());
    }
}

