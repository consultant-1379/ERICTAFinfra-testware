package com.ericsson.sut.test.operators;

import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.cli.Shell;
import com.ericsson.cifwk.taf.tools.cli.TimeoutException;

public interface GenericOperator {
    final String cliCommandPropertyPrefix = "clicommand.";

    /**
     * Retrieves the command specified in cliCommands.properties file
     *
     * @param command
     * @return CLI command
     */
    String getCommand(String command);

    /**
     * Method that searches and returns the host by name
     *
     * @param hostname
     * @return Specific host from hostname
     */
    Host getHostbyName(String hostname);

    /**
     * Createss CLI Shell instance using hostname provided
     *
     * @param hostname
     */
    void initializeShell(String hostname);

    /**
     * Reads from the standard output
     */
    String getStdOut();

    /**
     * Reads from the standard error
     */
    String getStdErr();

    /**
     * Execute a command with args on the host
     *
     * @param command
     * @param args
     * @param hostname
     * @return
     */

    Shell executeCommand(String command, String args);
    Shell executeCommand(String command);
    Shell executeCompleteCommand(String command);
    Shell executeCommandloadSql(String loadSql);

    /**
     * Writes a string to standard input
     *
     * @param message
     */
    void writeln(String message);

    /**
     * Converts a null error to a blank string if no standard error found
     *
     * @param error
     */
    String checkForNullError(String error);

    /**
     * Checks open/close status of shell
     *
     * @return
     * @throws TimeoutException
     */
    boolean isClosed() throws TimeoutException;

    /**
     * Wait for the spawned process to finish.
     *
     * @throws TimeoutException
     */
    void expectClose() throws TimeoutException;

    /**
     * Method that waits for pattern to appear on standard out
     *
     * @param expectedText
     * @return read standard out
     * @throws TimeoutException
     */
    String expect(String expectedText) throws TimeoutException;

    String expect(String expectedText, Long timeout) throws TimeoutException;

    /**
     * Get the exitCode of the most recent command
     *
     * @return Integer representing exit value
     */
    int getExitCode();

    /**
     *
     */
    void disconnectShell();

    void command(String verify);

}
