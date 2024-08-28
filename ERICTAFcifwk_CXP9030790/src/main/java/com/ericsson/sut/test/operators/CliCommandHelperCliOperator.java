/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.sut.test.operators;

import java.util.*;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.*;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;

import org.apache.log4j.Logger;

@Operator(context = Context.CLI)
public class CliCommandHelperCliOperator extends CLICommandHelper implements CliCommandHelperOperator {

    private Map<String, Object> commandResults;
    private static int TIMEOUT = 20;
    private static String STANDARD_OUT = "stdOut";
    private static String EXIT_VALUE = "exitValue";
    private static String EXECUTION_TIME = "executionTime";
    final String cliCommandPropertyPrefix = "clicommand.";
    Logger log = Logger.getLogger(CliCommandHelperCliOperator.class);

    /**
     * Utilizes CLI executeCommand method to run a command/array of commands
     * once on a new shell instance every time
     * 
     * @param host
     * @param commands
     * @return standardOut String representation of the standard output
     */
    public String executeSingularCommandAndReturnStandardOutput(Host host, String... commands) {
        createCliInstance(host);
        return simpleExec(commands);
    }

    /**
     * Create a single connectio so multiple commands can be executed with
     * opening a new connection
     * 
     * @param host
     */
    public void createOpenConnection(Host host) {
        createCliInstance(host);
        openShell();
    }

    /**
     * Create a single connectio so multiple commands can be executed with
     * opening a new connection
     * 
     * @param host
     */
    public void disconnectConnection(Host host) {
        disconnect();
    }

    /**
     * Executes a command and returns the command details
     * 
     * @param command
     */
    public Map<String, Object> executeCommandonSingleHostInstance(String command, int TIMEOUT) {
        String cliCommandFromPropertyPrefix = getCommand(command);
        execute(cliCommandFromPropertyPrefix);
        String stdOut = getStdOut(TIMEOUT);
        int commandExitValue = getCommandExitValue();
        long cmdExecutionTime = getCommandExecutionTime();
        commandResults = new HashMap<String, Object>();
        commandResults.put(STANDARD_OUT, stdOut);
        commandResults.put(EXIT_VALUE, commandExitValue);
        commandResults.put(EXECUTION_TIME, cmdExecutionTime);
        disconnect();
        return commandResults;
    }

    /**
     * Executes a command and returns the command details
     *
     * @param host
     * @param command
     * @return commandResults Map of data containing stdOut, stdErr, exitValue &
     *         executionTime of command
     */
    @Override
    public Map<String, Object> executeCommandAndReturnCommandDetails(Host host, String cliCommandPropertyPrefix, String command, int TIMEOUT) {
        String cliCommandFromPropertyPrefix = getCommand(cliCommandPropertyPrefix);
        String fullCommand = cliCommandFromPropertyPrefix + " " + command;
        log.info("Full CLI Command: " + fullCommand);
        try{
            createCliInstance(host);
            openShell();
        }catch (Exception error){
            log.error("Issue with host connection: " + host + " Error Thrown: ");error.printStackTrace();
        }
        execute(fullCommand);
        String stdOut = getStdOut(TIMEOUT);
        int commandExitValue = getCommandExitValue();
        long cmdExecutionTime = getCommandExecutionTime();
        commandResults = new HashMap<String, Object>();
        commandResults.put(STANDARD_OUT, stdOut);
        commandResults.put(EXIT_VALUE, commandExitValue);
        commandResults.put(EXECUTION_TIME, cmdExecutionTime);
        log.info("commandResults " + commandResults.toString());
        disconnect();
        return commandResults;
    }

    /**
     * Executes a command and returns the command details
     *
     * @param host
     * @param command
     * @return commandResults Map of data containing stdOut, stdErr, exitValue &
     *         executionTime of command
     */
    @Override
    public Map<String, Object> executeCommandAndReturnCommandDetails(Host host, String command, int TIMEOUT) {
        createCliInstance(host);
        openShell();
        execute(command);
        String stdOut = getStdOut(TIMEOUT);
        int commandExitValue = getCommandExitValue();
        long cmdExecutionTime = getCommandExecutionTime();
        commandResults = new HashMap<String, Object>();
        commandResults.put(STANDARD_OUT, stdOut);
        commandResults.put(EXIT_VALUE, commandExitValue);
        commandResults.put(EXECUTION_TIME, cmdExecutionTime);
        log.info("commandResults " + commandResults.toString());
        disconnect();
        return commandResults;
    }

    /**
     * Executes a command and returns the command details
     *
     * @param host
     * @param command
     * @return commandResults Map of data containing stdOut, stdErr, exitValue &
     *         executionTime of command
     */
    @Override
    public Map<String, Object> executeCommandAndReturnCommandDetails(Host host,
            String command) {
        log.info("Full CLI Command: " + command);
        try {
            createCliInstance(host);
            openShell();
        } catch (Exception error) {
            log.error("Issue with host connection: " + host + " Error Thrown: ");
            error.printStackTrace();
        }
        execute(command);
        String stdOut = getStdOut(TIMEOUT);
        int commandExitValue = getCommandExitValue();
        long cmdExecutionTime = getCommandExecutionTime();
        commandResults = new HashMap<String, Object>();
        commandResults.put(STANDARD_OUT, stdOut);
        commandResults.put(EXIT_VALUE, commandExitValue);
        commandResults.put(EXECUTION_TIME, cmdExecutionTime);
        log.info("commandResults " + commandResults.toString());
        disconnect();
        return commandResults;
    }

    /**
     * Closes the shell instance checking it closed as expected with the correct
     * exit value
     *
     * @return true or false based on successful closure
     */
    private boolean closeShell() {
        simpleExec("exit");
        expectShellClosure();
        isClosed();
        if (isClosed()) {
            return true;
        } else {
            throw new RuntimeException(
                    "Shell did not close with exit value of 0");
        }

    }

    /**
     * Used to get the command cliCommands.properties
     *
     * @return the command from the cliCommand.properties file
     */
    public String getCommand(String command) {
        log.debug("Retrieving command for: " + command);
        String ActCommand = DataHandler.getAttribute(
                cliCommandPropertyPrefix + command).toString();
        log.info("Received: " + ActCommand);
        return ActCommand;
    }

    public Map<String, Object> executeCommandAndReturnCommandDetails(Host host,
            String commandPropertyPrefix, String command, String args,
            int timeOut) {
        String cliCommandFromPropertyPrefix = getCommand(command);
        String fullCommand = cliCommandFromPropertyPrefix + args;
        log.info("Full CLI Command: " + fullCommand);
        try {
            createCliInstance(host);
            openShell();
        } catch (Exception error) {
            log.error("Issue with host connection: " + host + " Error Thrown: ");
            error.printStackTrace();
        }
        execute(fullCommand);
        String stdOut = getStdOut(TIMEOUT);
        int commandExitValue = getCommandExitValue();
        long cmdExecutionTime = getCommandExecutionTime();
        commandResults = new HashMap<String, Object>();
        commandResults.put(STANDARD_OUT, stdOut);
        commandResults.put(EXIT_VALUE, commandExitValue);
        commandResults.put(EXECUTION_TIME, cmdExecutionTime);
        log.info("commandResults " + commandResults.toString());
        closeShell();
        disconnect();
        return commandResults;
    }

}
