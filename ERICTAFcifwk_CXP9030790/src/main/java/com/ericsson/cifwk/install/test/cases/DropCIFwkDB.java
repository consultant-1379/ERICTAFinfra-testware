package com.ericsson.cifwk.install.test.cases;

import java.io.IOException;

import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.sut.test.operators.GenericOperator;
import com.google.inject.Inject;

public class DropCIFwkDB extends TorTestCaseHelper implements TestCase {
    @Inject
    OperatorRegistry<GenericOperator> operatorRegistry;

    @TestId(id = "CIP-4067_Func_3", title = "Drop CI FWK DB")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "dropCIFwkDB")
    public void deleteCIFwkDB(@Input("host") String hostname,
            @Input("deleteDB") String deleteDB,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        GenericOperator cliOperator = operatorRegistry
                .provide(GenericOperator.class);
        cliOperator.initializeShell(hostname);
        //Set up database
        cliOperator.command(deleteDB);
        cliOperator.command("DROP DATABASE IF EXISTS cireports;");
        cliOperator.command("exit");
        assertTrue(cliOperator.getExitCode() == expectedExitCode);
        cliOperator.executeCommand("exit");
        cliOperator.disconnectShell();
    }
}
