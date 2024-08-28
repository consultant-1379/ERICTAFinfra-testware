package com.ericsson.cifwk.rest.test.cases;

import java.io.File;
import java.util.*;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.utils.FileHandling;
import com.ericsson.cifwk.utils.XMLHandler;
import com.ericsson.sut.test.operators.CIPortalUIOperator;
import com.ericsson.sut.test.operators.JenkinsOperator;
import com.google.inject.Inject;

public class TestJenkinsJobCIFlow extends TorTestCaseHelper implements TestCase {

	private JenkinsOperator jenkinsOperator;
	private ArrayList<String> results = new ArrayList<String>();
	private HttpResponse response;
	private FileHandling fileFunction = new FileHandling(); 
	private int sleepTime = 10;
	private int counter = 0;
	Logger logger = Logger.getLogger(TestJenkinsJobCIFlow.class);
	@Inject
	OperatorRegistry<JenkinsOperator> operatorRegistry;
	CIPortalUIOperator operator = new CIPortalUIOperator();

	@TestId(id = "CIP-5729", title = "Testing That Jenkins Jobs for CI Flow is Created")
	@Context(context = {Context.REST})
	@DataDriven(name = "createJenkinsJobRestCommandsCIFlow")
	@Test(groups={"Acceptance"})
	public void testCreateJenkinsJobs(
			@Input("id") String Id,
			@Input("description") String testCaseDescription,
			@Input("jenkinsHostName") String jenkinsHostName,
			@Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
			@Input("jenkinsJobName") String jenkinsJobName,  
			@Input("jenkinsJobConfigurationFile") String jenkinsJobConfigurationFile,
			@Input("FileName") String fileName,
			@Input("fileType") String fileType,
			@Input("TempFile") String tempFile,
			@Input("_MIRROR_") String mirror,
			@Input("_REPO_") String repo,
			@Input("_GERRIT_") String gerrit,
			@Input("_GERRITBRANCH_") String gerritBranch,
			@Input("_GITBRANCH_") String gitBranch,
			@Input("_PUSHTOBRANCH_") String pushToBranch,
			@Input("_ARTIFACTID_") String artifact,
			@Input("_GROUPID_") String groupId,
			@Input("_SERVERNAME_") String server,
			@Output("Expected") String expected){
		setTestCase(Id, testCaseDescription);
		File file1 =  FileHandling.getFileToDeploy(fileName);
		Map<String,String> details = new HashMap<String, String>();
		details.put("_MIRROR_", mirror);
		details.put("_REPO_", repo);
		details.put("_GITBRANCH_", gitBranch);
		details.put("_ARTIFACTID_", artifact);
		details.put("_GROUPID_", groupId);
		details.put("_GERRIT_", gerrit);
		details.put("_GERRITBRANCH_", gerritBranch);
		details.put("_PUSHTOBRANCH_", pushToBranch);
		details.put("_SERVERNAME_", server);
		jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
		setTestStep("Creating the Config File for Jenkins Job");
		fileFunction.copyFindReplaceInFile(file1, jenkinsJobConfigurationFile, fileType, details);
		setTestStep("Creating the Jenkins Job");
		response = jenkinsOperator.createJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsJobConfigurationFile);
		setTestStep("Delete Temp Config File for Jenkins Job");
		fileFunction.deleteFile(jenkinsJobConfigurationFile);
		sleep(sleepTime);
		logger.debug("Response: " + response.getResponseCode());
		setTestStep("Result");
		assertEquals(expected, response.getResponseCode().toString());
	}


	@Context(context = {Context.REST})
	@DataDriven(name = "jenkinsJobStatusRestCommandsCIFlow")
	@Test(groups={"Acceptance"})
	@TestId(id = "CIP-5729", title = "Testing checking the status of Jenkins Jobs in CI Flow")
	public void testJenkinsJobStatus(@Input("jenkinsHostName") String jenkinsHostName,
			@Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
			@Input("jenkinsJobName") String jenkinsJobName,  
			@Input("jenkinsStatusType") String jenkinsStatusType,
			@Input("host") String host,
			@Input("location") String location,
			@Input("packageName") String packageName,
			@Input("timeoutInMillis") long timeOutUI,
			@Input("timeout") int timeout,
			@Output("codeReview") String codeReview,
			@Output("unit") String unit,
			@Output("acceptance") String acceptance,
			@Output("Expected") String expected, 
			@Output("ExpectedJobResult") String expectedJobResult,	
			@Output("ExpectedJobDeletedFromQueue") boolean expectedJobDeletedFromQueue,  
			@Output("ExpectedJobAbortResult") String expectedjobAbortResult){

		jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
		setTestStep("Checking if Jenkins Job is in Queue");
		while(jenkinsOperator.jenkinsJobQueueStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName)==true){	
			logger.info("Jenkins Job: " + jenkinsJobName  + " is currently in Build Queue current timeout: " + (timeout - counter) + " seconds");	
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
		while(XMLHandler.getNodeValue("building", jenkinsOperator.jenkinsJobStatus(jenkinsHostName, 
				jenkinsBaseDirectory, jenkinsJobName, jenkinsStatusType).getBody()).equalsIgnoreCase("true")){	
			response = jenkinsOperator.jenkinsJobStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsStatusType);

			if (counter <= (Integer.parseInt(XMLHandler.getNodeValue("estimatedDuration", response.getBody())) + 6000)){	
				logger.info("Jenkins Job: " + jenkinsJobName  + " is currently in Progress current Estimated Build time is: " 
						+ XMLHandler.getNodeValue("estimatedDuration", response.getBody()) + " seconds. Current Job Running Time is: " + counter + " seconds.");	
				sleep(sleepTime);	
				counter = counter + sleepTime;	
			}else{	
				logger.error("Jenkins Job: " + jenkinsJobName  + " has exceeded Estimated Build Timetherefore Aborting Job.");	
				counter = 0;	
				setTestStep("Call the stopBuildingJenkinsJob function in the jenkinsOperator Object to abort Job as Estimated Build Time has elapsed");
				response = jenkinsOperator.stopBuildingJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);	
				assertEquals(expectedjobAbortResult,response.getResponseCode().toString());	
				break;	
			}
			assertEquals(expected, response.getResponseCode().toString());
		}	
		setTestStep("Get the Build Result");	
		String jobResult = XMLHandler.getNodeValue("result", jenkinsOperator.jenkinsJobStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsStatusType).getBody());	
		setTestStep("CLUE Results");
		assertEquals(expectedJobResult,jobResult);	
		operator.startBrowser(host, location, packageName);
		results = operator.status(timeOutUI);
		if (results.size() != 0){
			if (codeReview != null){
				assert (results.get(0).contains(codeReview));
			}
			if (unit != null){
				assert (results.get(1).contains(unit));
			}
			if (acceptance != null){
				assert (results.get(2).contains(acceptance));
			}
		}
	}



	@TestId(id = "CIP-5729", title = "Testing That Jenkins Jobs for CI Flow is Deleted")
	@Context(context = {Context.REST})
	@DataDriven(name = "deleteJenkinsJobRestCommandsCIFlow")
	@Test(groups={"Acceptance"})
	public void testDeleteJenkinsJobs(
			@Input("id") String Id,
			@Input("description") String testCaseDescription,
			@Input("jenkinsHostName") String jenkinsHostName,
			@Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
			@Input("jenkinsJobName") String jenkinsJobName,  
			@Output("Expected") String expected ){
		jenkinsOperator = operatorRegistry.provide(JenkinsOperator.class);
		setTestStep("Deleting Jenkins Job");
		response = jenkinsOperator.deleteJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);
		logger.debug("Response: " + response.getResponseCode()); 
		setTestStep("Result");
		assertEquals(expected, response.getResponseCode().toString());
	}


}
