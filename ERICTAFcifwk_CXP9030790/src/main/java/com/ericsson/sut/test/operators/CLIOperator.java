package com.ericsson.sut.test.operators;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.google.inject.Singleton;

@Operator(context = Context.CLI)
@Singleton
public class CLIOperator implements GenericOperator {

    private CLI cliTool;
    private Shell shell;
    private static final String EXIT_MARKER = "command.complete";
    private static final String ECHO_EXIT_MARKER_AND_CODE = "echo \"" + EXIT_MARKER + "=$?\"";

    private int exitCode;
    private String stdOut;
    private String stdErr;
    private String currentCommand;

    Logger logger =Logger.getLogger(CLIOperator.class);

    @Override
    public String getCommand(String command) {
        logger.debug("Retrieving command for: "+command);
        String ActCommand = DataHandler.getAttribute(cliCommandPropertyPrefix+command).toString();
        logger.debug("Received: "+ActCommand);
        return ActCommand;
    }

    @Override
    public Host getHostbyName(String hostname) {
        return DataHandler.getHostByName(hostname);
    }

    @Override
    public void initializeShell(String hostname){
        Host host = getHostbyName(hostname);
        cliTool = new CLI(host);
        shell=cliTool.openShell();
    }

    @Override
    public void disconnectShell(){
        if(shell!= null){
            shell.disconnect();
        }
    }

    @Override
    public Shell executeCommand(String command, String args){
        currentCommand = getCommand(command);
        logger.debug("Full command to be executed \"" +currentCommand+ " " +args+"\n"+ECHO_EXIT_MARKER_AND_CODE);
        shell.writeln(currentCommand + " " + args+"\n"+ECHO_EXIT_MARKER_AND_CODE);
        setOutputs();
        return shell;
    }

    public Shell executeCommand(String command){
        currentCommand = getCommand(command);
        logger.debug("Full command to be executed \"" +currentCommand+"\n"+ECHO_EXIT_MARKER_AND_CODE);
        shell.writeln(currentCommand+"\n"+ECHO_EXIT_MARKER_AND_CODE);
        setOutputs();
        return shell;
    }

    public Shell executeCommandloadSql(String command){
        currentCommand = getCommand(command);
        logger.debug("Full command to be executed \"" +currentCommand+"\n"+ECHO_EXIT_MARKER_AND_CODE);
        shell.writeln(currentCommand+"\n"+ECHO_EXIT_MARKER_AND_CODE);
        try {
                Thread.sleep(120000);
                } catch (InterruptedException e) {
                logger.error("Issue connecting to Database: Error Thrown: ");
                e.printStackTrace();
                }
                setOutputs();
                return shell;
      }

    public Shell executeCompleteCommand(String currentCommand){
        logger.debug("Full command to be executed \"" +currentCommand+"\n"+ECHO_EXIT_MARKER_AND_CODE);
        shell.writeln(currentCommand+"\n"+ECHO_EXIT_MARKER_AND_CODE);
        setOutputs();
        return shell;
    }

    @Override
    public void command(String verify) {
        shell.writeln(verify);
    }

    private void setOutputs(){
        List<String> stdoutList = new ArrayList<String>();
        List<String> stderrList = new ArrayList<String>();
        exitCode = 1000;
        String out = shell.read();
        String err = shell.readErr();
        logger.debug("The original Output " + out);
        boolean isOutBegin = false;
        boolean isOutStop = false;
        for (String str : out.split("[\\n,\\r\\n]+")) {
            str = str.replaceAll("(.*)\\:\\~\\$ ", "" );
            logger.debug("Running Through the Loop String:::" + str);
            if (isOutStop && str.matches(EXIT_MARKER + "=\\d*.*")) {
                String value = str.substring(str.indexOf('=') + 1);
                exitCode = Integer.parseInt(value);
                logger.debug("EXITCODE Captured " + exitCode);
                break;
            }
            //if (!isOutBegin && currentCommand.contains(str)) {
            if (!isOutBegin) {
                logger.debug("Found Beginning of String Capture");
                isOutBegin = true;
                continue;
            }
            if (str.contains(ECHO_EXIT_MARKER_AND_CODE)) {
                logger.debug("Found FINISHING of String Capture");
                isOutStop = true;
                continue;
            }
            if (isOutBegin && !isOutStop) {
            //if (isOutBegin) {
                stdoutList.add(str);
            }
        }
        for (String str : err.split("[\\n,\\r\\n]+")) {
            if (currentCommand.contains(str)) {
                continue;
            }
            stderrList.add(str);
        }
        StringBuilder stdoutBuilder = new StringBuilder();
        for (String str : stdoutList) {
            if (currentCommand.contains(str)) {
                continue;
            }
            stdoutBuilder.append(str).append("\n");
        }
        stdOut = stdoutBuilder.toString();
        logger.debug("STD Out is: "+stdOut);

        StringBuilder stderrBuilder = new StringBuilder();
        for (String str : stderrList) {
            stderrBuilder.append(str).append("\n");
        }
        stdErr = stderrBuilder.toString();
        logger.debug("STD Err is: "+stdErr);
    }

    @Override
    public int getExitCode(){
        return exitCode;
    }

    @Override
    public String expect(String expectedText) throws TimeoutException{
        String found = shell.expect(expectedText);
        return found;
    }

    public String expect(String expectedText, Long timeout) throws TimeoutException{
        String found = shell.expect(expectedText, timeout);
        return found;
    }

    @Override
    public void expectClose() throws TimeoutException{
        shell.expectClose();
    }

    @Override
    public boolean isClosed() throws TimeoutException{
        return shell.isClosed();
    }

    @Override
    public String checkForNullError(String error){
        if (error == null){
            error = "";
            return error;
        }
        return error;
    }

    @Override
    public void writeln(String message){
        shell.writeln(message);
    }

    @Override
    public String getStdOut() {
        return stdOut;
    }

    public String getStdErr(){
        return stdErr;
    }

}

