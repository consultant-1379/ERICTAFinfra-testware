package com.ericsson.cifwk.test.cases;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.cifwk.utils.FileHandling;
import com.ericsson.sut.test.operators.CliCommandHelperCliOperator;
import com.ericsson.sut.test.operators.CliCommandHelperOperator;
import com.ericsson.sut.test.operators.GenericRESTOperator;
import com.google.inject.Inject;

import java.io.File;

public class GeneralPortalTesting extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<CliCommandHelperOperator> cliToolProvider;
    private Host host = null;
    final String commandPropertyPrefix = "ciPortalJenkins.";
    private Map<String, Object> commandResults;
    private RemoteFileHandler remote = null;
    Logger logger = Logger.getLogger(GeneralPortalTesting.class);
    @Inject
    private OperatorRegistry<GenericRESTOperator> genericRESTOperatorRegistry;

    /**
     * @throws TimeoutException
     * @throws IOException
     * @throws InterruptedException
     * @DESCRIPTION Verify the functionality of the Automated SED Generation
     * @VUsers 1
     * @PRIORITY MEDIUM
     * 
     */
    @TestId(id = "CIP-5747_Func_1", title = "Run maven build to test publish of artifacts")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "buildCLICommands")
    public void verifyMvnBuildFunctionality(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("host") String hostname,
            @Input("localPom") String localPom,
            @Input("remotePath") String remotePath,
            @Input("localMainPom") String localMainPom,
            @Input("remoteMainPath") String remoteMainPath,
            @Input("localTestPom") String localTestPom,
            @Input("remoteTestPath") String remoteTestPath,
            @Input("group") String group,
            @Input("artifact") String artifact,
            @Input("version") String version,
            @Input("drop") String drop,
            @Input("packaging") String packaging,
            @Input("repo") String repo,
            @Input("product") String product,
            @Input("mediaPath") String mediaPath,
            @Input("mediaCategory") String mediaCategory,
            @Input("isoExclude") String isoExclude,
            @Input("infra") String infra,
            @Input("remotePom") String remotePom,
            @Output("expectedOut1") String expectedOut1,
            @Output("expectedErr") String expectedErr,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeOut) throws TimeoutException,
            IOException, InterruptedException {
        setTestCase(Id, testCaseDescription);
        CliCommandHelperCliOperator cmdHelper = (CliCommandHelperCliOperator) cliToolProvider.provide(CliCommandHelperOperator.class);
        host = DataHandler.getHostByName(hostname);
        remote = new RemoteFileHandler(host);
        String command;
        setTestStep("copy test pom to SUT");
        command = "recursively.delete";
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, remotePath, timeOut);
        command = "make.dir.p";
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, remotePath, timeOut);
        File file = FileHandling.getFileToDeploy(localPom);
        Path pomFilePath = Paths.get(file.getPath());
        String remotePomLocation = remotePath + remotePom;
        remote.copyLocalFileToRemote(pomFilePath.toString(), remotePomLocation);
        if (mediaCategory.equals("testware")){
            command = "make.dir.p";
            commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, remoteMainPath, timeOut);
            file = FileHandling.getFileToDeploy(localMainPom);
            pomFilePath = Paths.get(file.getPath());
            String remoteMainPomLocation = remoteMainPath + remotePom;
            remote.copyLocalFileToRemote(pomFilePath.toString(), remoteMainPomLocation);
            command = "make.dir.p";
            commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, remoteTestPath, timeOut);
            file = FileHandling.getFileToDeploy(localTestPom);
            pomFilePath = Paths.get(file.getPath());
            String remoteTestPomLocation = remoteTestPath + remotePom;
            remote.copyLocalFileToRemote(pomFilePath.toString(), remoteTestPomLocation);
            remotePomLocation = remoteTestPomLocation;
        }
        command = "mvn";
        String args;
        args = "test -f " + remotePomLocation + " -DgroupId=" + group
                + " -DartifactId=" + artifact + " -Dversion=" + version
                + " -Ddrop=" + drop + " -Dproduct=" + product + " -Dpackaging="
                + packaging + " -Drepo=" + repo + " -DmediaCategory="
                + mediaCategory + " -DmediaPath=" + mediaPath + " -DisoExclude=" + isoExclude
                + " -Dinfra=" + infra + " -Dportal=https://" + host.getIp();
        logger.info(args);
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, args, timeOut);
        assertEquals(true, commandResults.get("stdOut").toString().contains(expectedOut1));
    }

}
