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
import com.ericsson.sut.test.operators.CliCommandHelperCliOperator;
import com.ericsson.sut.test.operators.CliCommandHelperOperator;
import com.google.inject.Inject;

public class Services extends TorTestCaseHelper implements TestCase {
    public static final String CLIHOST = "ciportal";

    @Inject
    OperatorRegistry<CliCommandHelperOperator> cliToolProvider;
    private Host host = DataHandler.getHostByName(CLIHOST);
    final String commandPropertyPrefix = "cliCommands.";
    private long pause = 5000;

    @TestId(id = "CIP-6141_Func_5", title = "Verify memcached service starts / stops as expected")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional" })
    @DataDriven(name = "servicesCommands")
    public void testMemcachedService(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("command") String command,
            @Input("args") String args,
            @Output("expectedOut") String expectedOut,
            @Output("expectedErr") String expectedErr,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeOut) throws TimeoutException,
            IOException, InterruptedException {
        setTestCase(Id, testCaseDescription);
        CliCommandHelperCliOperator cmdHelper = (CliCommandHelperCliOperator) cliToolProvider
                .provide(CliCommandHelperOperator.class);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,commandPropertyPrefix, command, args, timeOut);
        setTestStep("Verifying exit value");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
        setTestStep("Verifying standard output of command");
        assertEquals(true,commandResults.get("stdOut").toString().contains(expectedOut));
    }

    @TestId(id = "CIP-1718_Func_5", title = "Verify Message Bus service starts as expected")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional" })
    public void testMessageBusServices() throws TimeoutException,
            IOException, InterruptedException {
        CliCommandHelperCliOperator cmdHelper = (CliCommandHelperCliOperator) cliToolProvider.provide(CliCommandHelperOperator.class);
        Map<String, Object> commandResults;
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,commandPropertyPrefix, "service", "message-bus-consumer status", 5);
        setTestStep("Verifying standard output of command - cifwk-rabbittmq: Running:");
        if(!commandResults.get("stdOut").toString().contains("Message Bus Consumer Service is running on")){
            commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,commandPropertyPrefix, "service", "cifwk-rabbittmq start", 5);
            setTestStep("Verifying standard output of command - Starting cifwk-rabbittmq");
            setTestStep(commandResults.get("stdOut").toString());
            assertEquals(true,commandResults.get("stdOut").toString().contains("Starting cifwk-rabbittmq"));
            try {
                Thread.sleep(pause);
           } catch (InterruptedException e) {
                e.printStackTrace();
           }
        }
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,commandPropertyPrefix, "make.dir.p", " /proj/lciadm100/cifwk/logs/messagebus", 5);
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,commandPropertyPrefix, "touch.file", " /proj/lciadm100/cifwk/logs/messagebus/messagebus.log", 5);
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,commandPropertyPrefix, "service", "message-bus-consumer restart", 5);
        setTestStep("Verifying standard output of command - message-bus-consumer restart");
        assertEquals(true,commandResults.get("stdOut").toString().contains("Starting Message Bus Consumer Service"));
        try {
             Thread.sleep(pause);
        } catch (InterruptedException e) {
             e.printStackTrace();
        }
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,commandPropertyPrefix, "service", "message-bus-consumer status", 5);
        setTestStep("Verifying standard output of command - message-bus-consumer status");
        assertEquals(true,commandResults.get("stdOut").toString().contains("Message Bus Consumer Service is running on"));
    }
}
