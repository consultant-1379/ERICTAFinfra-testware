package com.ericsson.cifwk.install.test.cases;

import java.io.IOException;
import java.util.Map;

import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.sut.test.operators.CliCommandHelperCliOperator;
import com.ericsson.sut.test.operators.CliCommandHelperOperator;
import com.google.inject.Inject;

public class ManagementCommandsDelivery extends TorTestCaseHelper implements
        TestCase {
    public static final String CLIHOST = "ciportal";
    @Inject
    private OperatorRegistry<CliCommandHelperOperator> cliToolProvider;
    private Host host = DataHandler.getHostByName(CLIHOST);
    final String commandPropertyPrefix = "ciPortalJenkins.";

    /**
     * @throws TimeoutException
     * @throws IOException
     * @throws InterruptedException
     * @DESCRIPTION Verify the functionality of manage.py commands
     * @VUsers 1
     * @PRIORITY MEDIUM
     *
     */
    @TestId(id = "CIP-4217_Func_1", title = "Verify the functionality of manage.py commands")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "buildCLICommands")
    public void verifyManagePyFunctionalityDelivery(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("host") String hostname, @Input("command") String command,
            @Input("args") String args,
            @Output("expectedOut") String expectedOut,
            @Output("expectedErr") String expectedErr,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeOut) throws TimeoutException,
            IOException, InterruptedException {
        setTestCase(Id, testCaseDescription);
        CliCommandHelperCliOperator cmdHelper = (CliCommandHelperCliOperator) cliToolProvider
                .provide(CliCommandHelperOperator.class);
        Map<String, Object> commandResults = cmdHelper
                .executeCommandAndReturnCommandDetails(host, command, args,
                        timeOut);
        setTestStep("Verifying exit value");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
        setTestStep("Verifying standard output of command");
        assertEquals(true,
                commandResults.get("stdOut").toString().contains(expectedOut));
    }

}