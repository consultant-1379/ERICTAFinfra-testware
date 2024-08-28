package com.ericsson.sut.test.operators;

import java.util.Map;

import com.ericsson.cifwk.taf.data.Host;

/**
 * CliCommandHelperOperator is a Command Line Helper TAF Operator interface.
 * 
 */
public interface CliCommandHelperOperator {
    /**
     * 
     * This executes a CLI command and returns the output from the command
     * 
     * @param host
     *            The host to run the command on
     * @param cliCommandPropertyPrefix
     *            The cli prefix for the command
     * @param command
     *            The command to be run
     * @param TIMEOUT
     *            The timeout for the command in seconds
     * @return commandResults The results of running the command including
     *         Standard Out, Exit Values and Execution Time
     */
    public Map<String, Object> executeCommandAndReturnCommandDetails(Host host,
            String cliCommandPropertyPrefix, String command, int TIMEOUT);

    /**
     * 
     * This executes a CLI command and returns the output from the command
     * 
     * @param host
     *            The host to run the command on
     * @param cliCommandPropertyPrefix
     *            The cli prefix for the command
     * @param command
     *            The command to be run
     * @param args
     *            add ons to command
     * @param TIMEOUT
     *            The timeout for the command in seconds
     * @return commandResults The results of running the command including
     *         Standard Out, Exit Values and Execution Time
     * */

    public Map<String, Object> executeCommandAndReturnCommandDetails(Host host,
            String commandPropertyPrefix, String command, String args,
            int timeOut);

    /**
     * 
     * This executes a CLI command and returns the output from the command
     * 
     * @param host
     *            The host to run the command on
     * @param command
     *            The command to be run
     * @return commandResults The results of running the command including
     *         Standard Out, Exit Values and Execution Time
     */
    public Map<String, Object> executeCommandAndReturnCommandDetails(Host host,
            String command);

    /**
     * 
     * This executes a CLI command and returns the output from the command
     * 
     * @param host
     *            The host to run the command on
     * @param command
     *            The command to be run
     * @param TIMEOUT
     *            The timeout for the command in seconds
     * @return commandResults The results of running the command including
     *         Standard Out, Exit Values and Execution Time
     */
    public Map<String, Object> executeCommandAndReturnCommandDetails(Host host,
            String command, int TIMEOUT);
}
