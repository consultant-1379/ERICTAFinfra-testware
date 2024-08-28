package com.ericsson.cifwk.test.cases;

import java.io.IOException;
import java.util.Map;
import org.testng.annotations.Test;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.TimeoutException;
import com.ericsson.sut.test.operators.CliCommandHelperOperator;
import com.google.inject.Inject;

public class ServerCheck extends TorTestCaseHelper implements TestCase {
    @Inject
    OperatorRegistry<CliCommandHelperOperator> cliToolProvider;

    @TestId(id = "CIP-5679-Funct-13", title = "Verify the Directory Exists")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP" })
    @DataDriven(name = "serverCheck")
    public void checkDirectoryExists(@Input("host") String hostname,
            @Input("directory") String directory,
            @Input("minSize") int minSize,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) {
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command : cd " + directory);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "cd " + directory, timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"), assertValue);
    }

    @TestId(id = "CIP-5679-Funct-14", title = "Verify a Directory Exists and has appropriate space available")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP" })
    @DataDriven(name = "serverCheck")
    public void checkDirectorySize(@Input("host") String hostname,
            @Input("directory") String directory,
            @Input("minSize") int minSize,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) {
        int availableSize;
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command : ls " + directory);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "ls " + directory, timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"), assertValue);
        setTestStep("Execute Command : df -k /" + directory + " | awk '{print $3}' | tail -1");
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "df -k " + directory + " | awk '{print $3}' | tail -1");
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"), assertValue);
        String stdOutLines[] = commandResults.get("stdOut").toString().split("\\r?\\n");
        if (stdOutLines.length > 0) {
            availableSize = Integer.parseInt(stdOutLines[1]);
        } else {
            availableSize = Integer.parseInt(commandResults.get("stdOut").toString());
        }
        setTestStep("Verifying exit value");
        assertEquals(availableSize >= minSize, assertValue);
    }

    @TestId(id = "CIP-5679-Funct-15", title = "Verify a servers response")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP" })
    @DataDriven(name = "serverCheck")
    public void checkServerResponse(@Input("host") String hostname,
            @Input("directory") String directory,
            @Input("minSize") int minSize,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command : ls ");
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "ls", timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"), assertValue);
    }

    @TestId(id = "CIP-5679-Funct-16", title = "Verify Server Service")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP" })
    @DataDriven(name = "serviceCheck")
    public void serviceCheck(@Input("host") String hostname,
            @Input("service") String service,
            @Input("execute") String executeCmd,
            @Input("expectedOut") String expectedOut,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command : service " + service + " " + executeCmd);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "service " + service + " " + executeCmd, timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"), assertValue);
        setTestStep("Verifying standard output of command");
        assertEquals(commandResults.get("stdOut").toString().contains(expectedOut), assertValue);
    }
}
