package com.ericsson.cifwk.install.test.cases;

import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.sut.test.operators.GenericOperator;
import com.google.inject.Inject;

public class CLITest extends TorTestCaseHelper implements TestCase {

    private Shell shell;
    @Inject
    OperatorRegistry<GenericOperator> operatorRegistry;

    /**
     * @throws TimeoutException
     * @DESCRIPTION Verify the ability of CLI Shell commands to be executed
     * @PRE Connection to SUT
     * @VUsers 1
     * @PRIORITY HIGH
     */
    @TestId(id = "CIP-3884_Func_3", title = "Verify CLI Commands can be executed")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "commandData")
    public void verifyCLICommandsCanBeExecuted(@Input("host") String hostname,
            @Input("command") String command, @Input("args") String args,
            @Output("expectedOut") String expectedOut,
            @Output("expectedErr") String expectedErr,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws InterruptedException,
            TimeoutException {

        GenericOperator cliOperator = operatorRegistry
                .provide(GenericOperator.class);
        cliOperator.initializeShell(hostname);
        cliOperator.executeCommand(command, args);
        Thread.sleep(1000);
        assert (cliOperator.getStdOut().contains(expectedOut));

        assertTrue(cliOperator.isClosed());
        assert (cliOperator.getExitCode() == expectedExitCode);
    }

    @TestId(id = "CIP-3884_Func_4", title = "Verify prompt details from bash script are correct")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "executeScriptData")
    @VUsers(vusers = { 0 })
    public void verifyExpectedPromptDetails(@Input("host") String hostname,
            @Input("command") String command, @Input("args") String args,
            @Output("prompt1") String firstPrompt,
            @Output("prompt1result") String firstPromptResult,
            @Output("prompt2") String secondPrompt,
            @Output("prompt2result") String secondPromptResult,
            @Output("expectedExit") int expectedExitCode)
            throws TimeoutException {

        GenericOperator cliOperator = operatorRegistry
                .provide(GenericOperator.class);
        cliOperator.initializeShell(hostname);

        cliOperator.executeCommand(command, args);
        cliOperator.expect(firstPrompt);
        cliOperator.writeln(firstPromptResult);
        cliOperator.expect(secondPrompt);
        cliOperator.writeln(secondPromptResult);
        System.out.print(cliOperator.getStdOut());

        cliOperator.expectClose();
        assert (cliOperator.getExitCode() == expectedExitCode);
        assertTrue(cliOperator.isClosed());
    }

    @AfterSuite
    public void cleanup() {
        Host host = DataHandler.getHostByName("unknown");

        RemoteFileHandler remoteFileHandler = new RemoteFileHandler(host);
        String remoteFileLocation = "/root";
        remoteFileHandler.deleteRemoteFile(remoteFileLocation
                + "/UserDetails.sh");

        if (shell != null) {
            shell.disconnect();
        }
    }
}
