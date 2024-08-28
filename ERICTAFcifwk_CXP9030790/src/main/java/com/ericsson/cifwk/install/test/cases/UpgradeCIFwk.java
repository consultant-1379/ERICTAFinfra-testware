package com.ericsson.cifwk.install.test.cases ;

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
import org.apache.log4j.Logger;

public class UpgradeCIFwk extends TorTestCaseHelper implements TestCase {
    public static final String CLIHOST = "ciportal";

    Logger logger = Logger.getLogger(CliCommandHelperOperator.class);

    @Inject
    private OperatorRegistry<CliCommandHelperOperator> cliToolProvider;
    private Host host = DataHandler.getHostByName(CLIHOST);

    /**
     * @throws TimeoutException
     * @throws IOException
     * @throws InterruptedException
     * @DESCRIPTION Upgrade CI portal
     * @VUsers 1
     * @PRIORITY MEDIUM
     *
     */
    @TestId(id = "CIP-4067_Func_4a", title = "Upgrade CI portal")
    @Context(context = {Context.CLI})
    @Test(groups={"Acceptance"})

    public void upgradePortal() throws TimeoutException, IOException, InterruptedException{
        CliCommandHelperCliOperator cmdHelper = (CliCommandHelperCliOperator) cliToolProvider.provide(CliCommandHelperOperator.class);
        String command = "manage.py";
        String latestVersion = DataHandler.getAttribute("cifwk.latestVersion").toString();
        String currentVersion = DataHandler.getAttribute("cifwk.currentVersion").toString();

        if (latestVersion.equalsIgnoreCase(currentVersion)){
            logger.info("No upgrade required, current and latest version are the same");
            assertEquals (true,true);
        }
        else{
            String args = "cifwk_upgrade " + latestVersion;
            int timeOut = 600;
            int expectedExitCode = 0;
            String expectedOut = "CIFWK has completed Upgrade to new version";
            Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, args, timeOut);
            setTestStep("Verifying exit value");
            assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
            setTestStep("Verifying standard output of command");
            assertEquals (true, commandResults.get("stdOut").toString().contains(expectedOut));
        }
    }
}
