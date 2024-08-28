package com.ericsson.cifwk.teststeps;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestStep;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.cli.TimeoutException;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.sut.test.operators.CliCommandHelperCliOperator;
import com.ericsson.sut.test.operators.GenericRESTOperatorHttp;

public class DynamicYumRepoTestSteps {

    @Inject
    private CliCommandHelperCliOperator cmdHelper;

    @Inject
    GenericRESTOperatorHttp restHTTPOperator;

    Logger logger = Logger.getLogger(DynamicYumRepoTestSteps.class);

    private Host host = null;
    public static final String SETUP_DIRECTORIES = "CIP-6683_Func_1.1";
    public static final String YUM_REPO_CREATION = "CIP-6683_Func_1.2";
    public static final String YUM_ADD_AND_CONTENT_CHECK = "CIP-6683_Func_1.3";
    public static final String YUM_REPO_DELETION = "CIP-6683_Func_1.4";
    public static final String DOWNLOAD_ARTIFACTS = "CIP-6683_Func_2.1";
    public static final String ADD_ARTIFACTS_TO_REPO_LOCATION = "CIP-6683_Func_2.2";
    public static final String UPDATE_YUM_CONFIG = "CIP-6683_Func_2.3";
    public static final String TESTCASE_SETUP = "CIP-6683_Func_2.4";
    final String commandPropertyPrefix = "ciPortalJenkins.";
    private Map<String, Object> commandResults;
    private String parameters;
    private String repo;

    @TestStep(id = SETUP_DIRECTORIES, description = "Repo Location Creation")
    public void TestRepoLocationCreation(@Input("hostname") String hostname,
            @Input("parameters") String parameters,
            @Input("commandOne") String commandOne,
            @Input("commandTwo") String commandTwo,
            @Output("expectedExitCode") int expectedExitCode,
            @Output("expected") String expected, @Input("timeout") int timeOut)
            throws TimeoutException, IOException, InterruptedException {
        String CLIHOST = hostname;
        host = DataHandler.getHostByName(CLIHOST);
        logger.info("Creating Repo - nexusreadonly");
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,
                commandOne, parameters, timeOut);
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
        parameters = parameters + " ]&&echo ¨exists¨||echo ¨not exists¨";
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,
                commandTwo, parameters, timeOut);
        assertEquals(true,
                commandResults.get("stdOut").toString().contains(expected));
    }

    @TestStep(id = YUM_REPO_CREATION, description = "Yum Repo Creation")
    public void TestRepoCreation(@Input("hostname") String hostname,
            @Input("path") String path, @Input("parameters") String parameters,
            @Output("httpStatus") String httpStatus,
            @Input("timeout") int timeOut) throws TimeoutException,
            IOException, InterruptedException {
        logger.info("Creating yum Repo");
        HttpResponse result = restHTTPOperator.executeRESTPOST(hostname, path,
                parameters, "POST");
        logger.info("Yum Repo Create Result - " + result.getBody().toString());
        DataHandler.setAttribute(commandPropertyPrefix + "yumRepo", result
                .getBody().toString());
        logger.info("Yum Repo from the DataHandler -"
                + DataHandler.getAttribute(commandPropertyPrefix + "yumRepo")
                        .toString());
        assertEquals(true, result.getBody().contains(httpStatus));
    }

    @TestStep(id = YUM_ADD_AND_CONTENT_CHECK, description = "Yum Add And Content Check")
    public void TestRepoYumAddAndContent(@Input("hostname") String hostname,
            @Input("addCommand") String addCommand,
            @Input("contentCommand") String contentCommand,
            @Output("expectedOutput") String expectedOutput,
            @Output("expectedExitCode") int expectedExitCode,
            @Input("timeout") int timeOut) throws TimeoutException,
            IOException, InterruptedException {
        String portalUrl = DataHandler.getAttribute("cifwk.portal.link")
                .toString();
        repo = DataHandler.getAttribute(commandPropertyPrefix + "yumRepo")
                .toString();
        String CLIHOST = hostname;
        host = DataHandler.getHostByName(CLIHOST);
        logger.info("Add Yum Repo");
        parameters = repo.replace(portalUrl, host.getIp());
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,
                addCommand, parameters, timeOut);
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
        logger.info("Yum Repo Content Check");
        parameters = "--enablerepo='"
                + repo.replace("https://" + portalUrl, host.getIp()).replace(
                        "/", "_") + "' list available";
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,
                contentCommand, parameters, timeOut);
        String[] expectedResult = expectedOutput.split(",");
        for (int i = 0; i < expectedResult.length; i++) {
            assert (commandResults.get("stdOut").toString()
                    .contains(expectedResult[i].toString()));
        }
    }

    @TestStep(id = YUM_REPO_DELETION, description = "Yum Repo Deletion")
    public void TestRepoDeletetion(@Input("hostname") String hostname,
            @Input("deletePath") String path,
            @Input("restParameters") String parameters,
            @Output("deletehttpStatus") String httpStatus,
            @Input("timeout") int timeOut) throws TimeoutException,
            IOException, InterruptedException {
        logger.info("Delete Yum Repo");
        String portalUrl = DataHandler.getAttribute("cifwk.portal.link")
                .toString();
        if (parameters.equals("repo=")) {
            if (!DataHandler.getAttribute(commandPropertyPrefix + "yumRepo")
                    .toString().contains("ERROR")) {
                logger.info("Using DataHandler.getAttribute for repo");
                parameters = parameters
                        + DataHandler
                                .getAttribute(commandPropertyPrefix + "yumRepo")
                                .toString().replace(portalUrl, host.getIp());
            } else {
                logger.info("Taking parameter from CSV");
            }
        } else {
            logger.info("Taking parameter from CSV");
            parameters = parameters.replace(portalUrl, host.getIp());
        }
        HttpResponse result = restHTTPOperator.executeRESTPOST(hostname, path,
                parameters, "POST");
        logger.info("Yum Repo Delete Result - " + result.getBody().toString());
        assertEquals(true, result.getBody().contains(httpStatus));
    }

    @TestStep(id = DOWNLOAD_ARTIFACTS, description = "Download Artifacts")
    public void TestDowloadArtifacts(@Input("host") String hostname,
            @Input("downloadLoc") String downloadLoc,
            @Input("groupId") String groupId,
            @Input("artifactId") String artifactId,
            @Input("version") String version,
            @Input("nexusUrl") String nexusUrl,
            @Input("downloadFile") String downloadFile,
            @Input("list") String list,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeOut) throws TimeoutException,
            IOException, InterruptedException {
        String nexusHostName = DataHandler.getAttribute(
                "cifwk.hubNexus.hyperText").toString();
        groupId = groupId.replace(".", "/");
        String downloadName = artifactId + "-" + version + ".rpm";
        String fileToGet = nexusHostName + nexusUrl + "/releases/" + groupId
                + "/" + artifactId + "/" + version + "/" + downloadName;
        String downloadFileArgs = downloadLoc + " " + fileToGet;
        host = DataHandler.getHostByName(hostname);
        Map<String, Object> commandResults = cmdHelper
                .executeCommandAndReturnCommandDetails(host, downloadFile,
                        downloadFileArgs, timeOut);
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
    }

    @TestStep(id = ADD_ARTIFACTS_TO_REPO_LOCATION, description = "Add Artifacts To Repo Location")
    public void TestAddArtifactsToRepoLocation(
            @Input("hostname") String hostname,
            @Input("location") String location,
            @Input("desLocation") String destination,
            @Input("command") String command,
            @Output("expectedExitCode") int expectedExitCode,
            @Input("timeout") int timeOut) throws TimeoutException,
            IOException, InterruptedException {
        String CLIHOST = hostname;
        host = DataHandler.getHostByName(CLIHOST);
        logger.info("Adding Artifacts");
        parameters = location + " " + destination;
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,
                command, parameters, timeOut);
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
    }

    @TestStep(id = UPDATE_YUM_CONFIG, description = "Update yum config")
    public void TestYumConfUpdate(@Input("hostname") String hostname,
            @Input("mainCommand") String commandMain,
            @Input("argsOne") String argsMain,
            @Input("checkCommand") String commandCheck,
            @Input("argsTwo") String argsCheck, @Input("file") String file,
            @Output("expectedExitCode") int expectedExitCode,
            @Input("timeout") int timeOut) throws TimeoutException,
            IOException, InterruptedException {
        String CLIHOST = hostname;
        host = DataHandler.getHostByName(CLIHOST);
        logger.info("checking yum.conf");
        argsCheck = "'" + argsCheck + "'" + " " + file;
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,
                commandCheck, argsCheck, timeOut);
        if (1 == (Integer) commandResults.get("exitValue")) {
            logger.info("Updating - yum.conf");
            argsMain = "'" + argsMain + "'" + " " + file;
            commandResults = cmdHelper.executeCommandAndReturnCommandDetails(
                    host, commandMain, argsMain, timeOut);
            assert (expectedExitCode == (Integer) commandResults
                    .get("exitValue"));
        } else {
            logger.info("No Update to yum.conf");
        }
    }

    @TestStep(id = TESTCASE_SETUP, description = "Setup for Testcases")
    public void TestSetUp(@Input("hostname") String hostname,
            @Input("command") String command, @Input("args") String args,
            @Output("expectedOut") String expectedOut,
            @Output("expectedErr") String expectedErr,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeOut) throws TimeoutException,
            IOException, InterruptedException {
        String CLIHOST = hostname;
        host = DataHandler.getHostByName(CLIHOST);
        logger.info("Setup for Testcases");
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,
                command, args, timeOut);
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
        assertEquals(true,
                commandResults.get("stdOut").toString().contains(expectedOut));
    }
}
