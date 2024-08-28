package com.ericsson.cifwk.rest.test.cases;

import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.sut.test.operators.GenericOperator;
import com.google.inject.Inject;

public class TestWarePromotionCommands extends TorTestCaseHelper implements
        TestCase {

    @Inject
    OperatorRegistry<GenericOperator> operatorRegistry;

    /**
     * @throws TimeoutException
     * @DESCRIPTION Verify the ability of CLI Shell commands to be executed
     * @PRE Connection to SUT
     * @VUsers 1
     * @PRIORITY HIGH
     */
    @TestId(id = "CIP-4384_Func_1", title = "Ensure that testware has been added successfully")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "testWarePromotionCommands")
    public void verifyCLICommandsCanBeExecuted(@Input("id") String id,
            @Input("description") String description,
            @Input("host") String host,
            @Input("wgetNoCheck") String wgetNoCheck,
            @Input("testwareArtifact") String testwareArtifact,
            @Input("testwareDescription") String testwareDescription,
            @Input("version") String version, @Input("groupId") String groupId,
            @Input("signum") String signum,
            @Input("execVer") String execVersion,
            @Input("execGroupId") String execGroupId,
            @Input("execArtifactId") String execArtifactId,
            @Input("command") String command,
            @Input("catOutput") String catOutput, @Input("args") String args,
            @Output("expectedOut") String expectedOut,
            @Output("expectedErr") String expectedErr,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws InterruptedException,
            TimeoutException {

        GenericOperator cliOperator = operatorRegistry
                .provide(GenericOperator.class);

        wgetNoCheck = cliOperator.getCommand(wgetNoCheck);

        if (testwareArtifact == null) {
            testwareArtifact = "testwareArtifact=";
        } else {
            testwareArtifact = "testwareArtifact=" + testwareArtifact;
        }
        if (testwareDescription == null) {
            testwareDescription = "description=";
        } else {
            testwareDescription = "description='" + testwareDescription + "'";
        }
        if (version == null) {
            version = "version=";
        } else {
            version = "version=" + version;
        }
        if (groupId == null) {
            groupId = "groupId=";
        } else {
            groupId = "groupId=" + groupId;
        }
        if (signum == null) {
            signum = "signum=";
        } else {
            signum = "signum=" + signum;
        }
        if (execVersion == null) {
            execVersion = "execVer=";
        } else {
            execVersion = "execVer=" + execVersion;
        }
        if (execGroupId == null) {
            execGroupId = "execGroupId=";
        } else {
            execGroupId = "execGroupId=" + execGroupId;
        }
        if (execArtifactId == null) {
            execArtifactId = "execArtifactId=";
        } else {
            execArtifactId = "execArtifactId=" + execArtifactId;
        }

        String serverURL = "http://" + cliOperator.getHostbyName(host).getIp()
                + "/cifwkTestwareImport/";
        String postData = "\"" + testwareArtifact + "&" + testwareDescription
                + "&" + version + "&" + groupId + "&" + signum + "&"
                + execVersion + "&" + execGroupId + "&" + execArtifactId
                + "\" " + serverURL + " > /tmp/output";

        cliOperator.initializeShell(host);
        cliOperator.executeCommand("testcommand", postData);
        cliOperator.executeCommand("exit");
        cliOperator.disconnectShell();

        cliOperator.initializeShell(host);
        cliOperator.executeCommand(catOutput);
        assert (cliOperator.getStdOut().contains(expectedOut));
        assertTrue(cliOperator.getExitCode() == expectedExitCode);
        cliOperator.executeCommand("exit");
        cliOperator.disconnectShell();
    }
}
