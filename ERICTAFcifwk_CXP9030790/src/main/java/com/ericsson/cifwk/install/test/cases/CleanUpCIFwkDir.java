package com.ericsson.cifwk.install.test.cases;

import java.util.Map;

import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.sut.test.operators.CliCommandHelperCliOperator;
import com.ericsson.sut.test.operators.CliCommandHelperOperator;
import com.google.inject.Inject;

public class CleanUpCIFwkDir extends TorTestCaseHelper implements TestCase {
    public static final String CLIHOST = "ciportal";

    @Inject
    private OperatorRegistry<CliCommandHelperOperator> cliToolProvider;
    private Host host = DataHandler.getHostByName(CLIHOST);

    @TestId(id = "CIP-4067_Func_3", title = "Clean UP CI FWK DB")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "cleanUpCIFwkDir")
    public void cleanCIFwkDir(@Input("command") String command,
            @Input("directory") String directory,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) {
        CliCommandHelperCliOperator cmdHelper = (CliCommandHelperCliOperator) cliToolProvider.provide(CliCommandHelperOperator.class);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, directory, timeout);
        setTestStep("Verifying exit value");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
        setTestStep("Execuition Time " + commandResults.get("executionTime"));
    }
}
