package com.ericsson.cifwk.tafscheduler.test.cases;

import java.io.IOException;

import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.sut.test.operators.GenericOperator;
import com.google.inject.Inject;

public class demoCiFwk extends TorTestCaseHelper implements TestCase {
    @Inject
    OperatorRegistry<GenericOperator> operatorRegistry;

    @TestId(id = "CIP-6552", title = "check the server is alive")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "demoCiFwk")
    public void DemoCiFwk(@Input("host") String hostname,
            @Input("simpleCommand") String simpleCommand,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        GenericOperator cliOperator = operatorRegistry
                .provide(GenericOperator.class);
        cliOperator.initializeShell(hostname);
        //Start Daemon
        cliOperator.executeCommand(simpleCommand);
        assertTrue(cliOperator.getExitCode() == expectedExitCode);
        cliOperator.executeCommand("exit");
        cliOperator.disconnectShell();
    }
}
