package com.ericsson.cifwk.ui.test.cases;

import java.io.File;

import com.ericsson.cifwk.rest.test.cases.TestJenkinsJobCIFlow;
import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.ui.test.pages.TestReportLinkUIModel;
import com.ericsson.cifwk.utils.FileHandling;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ericsson.sut.test.operators.*;

public class TestReportLinksUI extends TorTestCaseHelper implements TestCase {
    private BrowserTab browserTab;
    private GenericUIOperator genericUIOperator;
    private TestReportLinkUIModel testReportModel;
    private HttpResponse response;
    private JenkinsRESTOperator jenkinsOperator = new JenkinsRESTOperator();
    private int sleepTime = 2;
    private int counter = 0;
    Logger logger = Logger.getLogger(TestJenkinsJobCIFlow.class);

    @TestId(id = "CIP-6530_Func_1", title = "Verify Test Report link for Product summary page on CI portal")
    @Context(context = {Context.REST})
    @Test(groups={"Functional"})
    @DataDriven(name = "createDummyCDB")
    public void testCreateProductWithTestReport(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("jenkinsHost") String jenkinsHostName,
            @Input("directory") String jenkinsBaseDirectory,
            @Input("jobName") String jenkinsJobName,
            @Input("configFile") String fileName,
            @Input("jobParams") String jenkinsJobParameters,
            @Input("expectedCreateResponse") String expectedCreateResponse,
            @Input("expectedBuildResponse") String expectedBuildResponse,
            @Input("timeout") int timeout) {
        setTestCase(Id, testCaseDescription);
        setTestStep("Build Jenkins job");
        File jenkinsJobConfigurationFile =FileHandling.getFileToDeploy(fileName);
        response= jenkinsOperator.createJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsJobConfigurationFile.getAbsolutePath());
        assert(response.getStatusLine().contains(expectedCreateResponse));
        response= jenkinsOperator.buildJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsJobParameters);
        assert(response.getStatusLine().contains(expectedBuildResponse));
        while(jenkinsOperator.jenkinsJobQueueStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName)==true){
            logger.info("Jenkins Job: " + jenkinsJobName  + " is currently in Build Queue current timeout: " + (timeout - counter) + " seconds");
            counter = counter + sleepTime;
            sleep(sleepTime);
            if(counter == timeout){
                logger.error("Jenkins Job has been in Build Queue for over expected Timeout: " + timeout + " seconds, job has not yet Built");
                counter = 0;
                setTestStep("Call the jenkinsJobQueueStatus function in the jenkinsOperator Object to remove current build for queue as timeout has been reached");
                jenkinsOperator.deleteJenkinsPendingJobFromQueue(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);
                break;
            }
        }
    }

    @TestId(id = "CIP-6530_Func_2", title = "Verify Test Report link for Product summary page on CI portal")
    @Context(context = {Context.UI})
    @Test(groups={"Functional"})
    @DataDriven(name = "testReportLink")
    public void testReportLink(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("testStep1") String testStep1,
            @Input("host") String host,
            @Input("expectedResult") String expectedResult,
            @Input("timeout") int timeOut) {
        setTestCase(Id, testCaseDescription);
        setTestStep("Start Browser for UI tests to Begin");
        genericUIOperator = new GenericUIOperator();
        browserTab = genericUIOperator.startBrowser(host, null, null);

        setTestStep(testStep1);
        testReportModel = browserTab.getView(TestReportLinkUIModel.class);

        String result = testReportModel.verifyTestReportLink(browserTab, timeOut);

        assertEquals(expectedResult, result);
    }

    @TestId(id = "CIP-6530_Func_3", title = "Verify Test Report link for Product summary page on CI portal")
    @Context(context = {Context.REST})
    @Test(groups={"Functional"})
    @DataDriven(name = "createDummyCDB")
    public void deleteJenkinsTestReportJob(
            @Input("jenkinsHost") String jenkinsHostName,
            @Input("directory") String jenkinsBaseDirectory,
            @Input("jobName") String jenkinsJobName,
            @Input("expectedDeleteResponse") String expectedDeleteResponse) {
        setTestStep("Delete Jenkins job");
        response= jenkinsOperator.deleteJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);
        assert(response.getStatusLine().contains(expectedDeleteResponse));
    }
}