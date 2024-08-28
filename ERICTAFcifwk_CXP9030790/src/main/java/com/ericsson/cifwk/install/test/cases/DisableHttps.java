package com.ericsson.cifwk.install.test.cases;

import java.io.IOException;

import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.sut.test.operators.GenericOperator;
import com.google.inject.Inject;

public class DisableHttps extends TorTestCaseHelper implements TestCase {

    @Inject
    OperatorRegistry<GenericOperator> operatorRegistry;

    @TestId(id = "CIS-16828_Func_1", title = "Disable Https")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "disableHttps")
    public void disableHttps(@Input("host") String hostname,
            @Input("command") String command,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        GenericOperator cliOperator = operatorRegistry
                .provide(GenericOperator.class);
        cliOperator.initializeShell(hostname);
        cliOperator.executeCommand(command);
        assertTrue(cliOperator.getExitCode() == expectedExitCode);
        cliOperator.executeCommand("exit");
        cliOperator.disconnectShell();
    }
}