package com.ericsson.cifwk.install.test.cases;

import java.io.IOException;

import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.sut.test.operators.GenericOperator;
import com.google.inject.Inject;

public class StartCIFwkDaemon extends TorTestCaseHelper implements TestCase {

    @Inject
    OperatorRegistry<GenericOperator> operatorRegistry;

    @TestId(id = "CIP-4067_Func_5", title = "Start CI FWK Daemon")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "startCIFwkDaemon")
    public void CIFwkDaemonStart(@Input("host") String hostname,
            @Input("startHttpDaemon") String startHttpDaemon,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        GenericOperator cliOperator = operatorRegistry
                .provide(GenericOperator.class);
        cliOperator.initializeShell(hostname);
        //Start Daemon
        cliOperator.executeCommand(startHttpDaemon);
        assertTrue(cliOperator.getExitCode() == expectedExitCode);
        cliOperator.executeCommand("exit");
        cliOperator.disconnectShell();
    }
}