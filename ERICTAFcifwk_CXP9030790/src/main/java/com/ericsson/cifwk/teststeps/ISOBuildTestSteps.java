package com.ericsson.cifwk.teststeps;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.ericsson.cifwk.taf.TestContext;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.utils.FileHandling;
import com.ericsson.sut.test.operators.CliCommandHelperCliOperator;

import javax.inject.Inject;

import org.apache.log4j.Logger;

public class ISOBuildTestSteps extends TorTestCaseHelper {

    public static final String SETUP_DIRECTORIES = "CIP-3456_Func_2.3";
    public static final String MAVEN_ISO_BUILD = "CIP-3456_Func_2.4";
    public static final String MOUNT_ISO = "CIP-3456_Func_2.5";
    public static final String VERIFY_OUTPUT = "CIP-3456_Func_2.6";
    public static final String VERIFY_OUTPUT_CI_PORTAL = "CIP-3456_Func_2.7";
    public static final String COPYCONFIGFILES_TO_TMPDIRECTORY = "CIP-8245_Func_1";

    @Inject
    CliCommandHelperCliOperator cmdHelper;
    private Host host = null;
    final String commandPropertyPrefix = "ciPortalJenkins.";
    private Map<String, Object> commandResults;
    private RemoteFileHandler remote = null;
    Logger logger = Logger.getLogger(ISOBuildTestSteps.class);

    @Inject
    TestContext context;

    @TestStep(id = SETUP_DIRECTORIES)
    public void setUp(@Input("hostname") String hostname,
            @Input("command") String command,
            @Input("remotePath") String remotePath,
            @Output("expectedExitCode") int expectedExitCode,
            @Input("timeout") int timeOut) {
        host = DataHandler.getHostByName(hostname);
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, remotePath, timeOut);
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
    }
    
    @TestStep(id = COPYCONFIGFILES_TO_TMPDIRECTORY)
    public void copyISOConfigFiles(
            @Input("hostname") String hostname,
            @Input("localFile") String localFile,
            @Input("remoteFile") String remoteFile,
            @Input("remotePath") String remotePath,
            @Input("timeout") int timeOut,
            @Output("expectedOut") String expectedOut) {
        host = DataHandler.getHostByName(hostname);
        remote = new RemoteFileHandler(host);
        File file = FileHandling.getFileToDeploy(localFile);
        Path pomFilePath = Paths.get(file.getPath());
        String remotePomLocation = remotePath + remoteFile;
        remote.copyLocalFileToRemote(pomFilePath.toString(), remotePomLocation);
        assert(true);
    }

    @TestStep(id = MAVEN_ISO_BUILD)
    public void buildIso(@Input("hostname") String hostName,
            @Input("command") String command,
            @Input("args") String args,
            @Input("localPom") String localPom,
            @Input("remotePom") String remotePom,
            @Input("remotePath") String remotePath,
            @Input("timeout") int timeOut,
            @Output("expectedOut") String expectedOut) {
        host = DataHandler.getHostByName(hostName);
        remote = new RemoteFileHandler(host);
        
        File file = FileHandling.getFileToDeploy(localPom);
        Path pomFilePath = Paths.get(file.getPath());
        String remotePomLocation = remotePath + remotePom;
        remote.copyLocalFileToRemote(pomFilePath.toString(), remotePomLocation);
        args = args + " " + remotePomLocation;
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, args, timeOut);
        assertEquals(true, commandResults.get("stdOut").toString().contains(expectedOut));

    }

    @TestStep(id = MOUNT_ISO)
    public void mntIso(@Input("hostname") String hostName,
            @Input("command") String command,
            @Input("args") String args,
            @Output("expectedExitCode") int expectedExitCode,
            @Input("timeout") int timeOut) {
        host = DataHandler.getHostByName(hostName);
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,
                command, args, timeOut);
        if (commandResults.get("stdOut").toString().contains("umount: /mnt/testIso: not found") || commandResults.get("stdOut").toString().contains("umount: /mnt/testIso: not mounted") || commandResults.get("stdOut").toString().contains("cannot remove")) {
        } else {
            assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
        }
        context.setAttribute("output", commandResults);
    }

    @TestStep(id = VERIFY_OUTPUT)
    public void check(@Output("expectedOut") String expectedOut,
            @Output("expected") boolean expected) {
        commandResults = context.getAttribute("output");
        assertEquals(expected, commandResults.get("stdOut").toString().contains(expectedOut));
    }

    @TestStep(id = VERIFY_OUTPUT_CI_PORTAL)
    public void checkPortalContent(@Input("hostname") String hostName,
            @Input("command") String command,
            @Input("args") String args,
            @Input("timeout") int timeOut,
            @Output("expectedOutput") String expectedOut,
            @Output("expected") boolean expected) {
        host = DataHandler.getHostByName(hostName);
        args = "http://" + host.getIp() + args;
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, args, timeOut);
        assertEquals(expected, commandResults.get("stdOut").toString().contains(expectedOut));
    }
}
