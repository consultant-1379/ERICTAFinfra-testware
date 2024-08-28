package com.ericsson.cifwk.ui.test.cases;

import java.io.File;
import java.util.ArrayList;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.guice.*;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.utils.FileHandling;
import com.ericsson.cifwk.utils.XMLHandler;

import org.apache.log4j.Logger;
import org.testng.annotations.*;

import javax.inject.Inject;

import com.ericsson.sut.test.operators.*;

public class CLUEstatus extends TorTestCaseHelper implements TestCase {

    private JenkinsRESTOperator jenkinsOperator = new JenkinsRESTOperator();
    private int counter = 0;
    private int timeout = 100;
    @Inject
    private OperatorRegistry<CIPortalOperator> provider;

    private HttpResponse response;
    private int sleepTime = 10;
    Logger logger = Logger.getLogger(CLUEstatus.class);
    private ArrayList<String> results = new ArrayList<String>();
    final String commandPropertyPrefix = "ciPortalJenkins.";
    private String host = DataHandler.getAttribute(commandPropertyPrefix + "host").toString();
    private String jenkinsHostName = DataHandler.getAttribute(commandPropertyPrefix + "jenkinsHost").toString();
    private String jenkinsBaseDirectory = DataHandler.getAttribute(commandPropertyPrefix + "jenkinsBaseDirectory").toString();
    private String jenkinsJobName = DataHandler.getAttribute(commandPropertyPrefix + "jobName").toString();
    private String configFile = DataHandler.getAttribute(commandPropertyPrefix + "configFile").toString();

    @BeforeSuite
    public void BeforeSuite() {
        setTestStep("Create Job");
        File jenkinsJobConfigFileName = FileHandling.getFileToDeploy(configFile);
        configFile = jenkinsJobConfigFileName.getAbsolutePath();
        jenkinsOperator.createJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, configFile);
    }

    @TestId(id = "CIP-5729", title = "Testing CLUE is Triggering the CI Portal Status of Package going through the CI Flow")
    @Context(context = { Context.UI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "clueStatusUI")
    public void testingCLUEStatus(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("location") String location,
            @Input("packageName") String packageName,
            @Input("jenkinsJobParameters") String jenkinsJobParameters,
            @Input("jenkinsStatusType") String jenkinsStatusType,
            @Input("jenkinsTimeout") int jenkinsTimeOut,
            @Output("codeReview") String codeReview,
            @Output("unit") String unit,
            @Output("acceptance") String acceptance,
            @Output("release") String release,
            @Output("Expected") String expected,
            @Input("timeoutInMillis") long timeOut) {
        setTestCase(Id, testCaseDescription);
        CIPortalOperator operator = provider.provide(CIPortalOperator.class);
        jenkinsOperator.buildJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsJobParameters);
        sleep(sleepTime);
        setTestStep("Checking if Job is in the Queue");
        while (jenkinsOperator.jenkinsJobQueueStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName) == true) {
            logger.info("Jenkins Job: " + jenkinsJobName + " is currently in Build Queue current timeout: " + (timeout - counter) + " seconds");
            counter = counter + sleepTime;
            sleep(sleepTime);
            if (counter == timeout) {
                logger.error("Jenkins Job has been in Build Queue for over expected Timeout: " + timeout + " seconds, job has not yet Built");
                counter = 0;
                setTestStep("Call the jenkinsJobQueueStatus function in the jenkinsOperator Object to remove current build for queue as timeout has been reached");
                jenkinsOperator.deleteJenkinsPendingJobFromQueue(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);
                break;
            }
        }

        setTestStep("Checking if job completes before Estimated build Time elapses, if not abort Job");
        int counter = 0;
        counter = 0;
        while (XMLHandler.getNodeValue("building", jenkinsOperator.jenkinsJobStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsStatusType).getBody()).equalsIgnoreCase("true")) {
            response = jenkinsOperator.jenkinsJobStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsStatusType);
            if (counter <= (Integer.parseInt(XMLHandler.getNodeValue("estimatedDuration", response.getBody())) + 6000)) {
                logger.info("Jenkins Job: " + jenkinsJobName + " is currently in Progress current Estimated Build time is: "
                        + XMLHandler.getNodeValue("estimatedDuration", response.getBody()) + " seconds. Current Job Running Time is: " + counter + " seconds.");
                sleep(sleepTime);
                counter = counter + sleepTime;
            } else {
                logger.error("Jenkins Job: " + jenkinsJobName + " has exceeded Estimated Build Timetherefore Aborting Job.");
                counter = 0;
                setTestStep("Call the stopBuildingJenkinsJob function in the jenkinsOperator Object to abort Job as Estimated Build Time has elapsed");
                jenkinsOperator.stopBuildingJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);
                break;
            }
        }
        setTestStep("Checking the CLUE on CI Portal");
        operator.startBrowser(host, location, packageName);
        results = operator.status(timeOut);
        if (results.size() != 0) {
            assert (results.get(0).contains(codeReview));
            assert (results.get(1).contains(unit));
            assert (results.get(2).contains(acceptance));
            assert (results.get(3).contains(release));
        } else {
            fail("CLUE Testcase Failed, Due to No Results From UI");
        }
    }

    @AfterSuite
    public void AfterSuite() {
        setTestStep("Delete Job");
        jenkinsOperator.deleteJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);
    }
}

