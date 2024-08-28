package com.ericsson.cifwk.test.cases ;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.utils.FileHandling;
import com.ericsson.sut.test.operators.CliCommandHelperCliOperator;
import com.ericsson.sut.test.operators.CliCommandHelperOperator;
import com.ericsson.sut.test.operators.GenericRESTOperator;
import com.google.inject.Inject;

import java.io.File;

public class TestSEDCommands extends TorTestCaseHelper implements TestCase {
    public static String CLIHOST = "";

    @Inject
    private OperatorRegistry<CliCommandHelperOperator> cliToolProvider;
    private Host host = null;
    final String commandPropertyPrefix = "ciPortalJenkins.";
    private Map<String, Object> commandResults;
    private boolean status;
    private RemoteFileHandler remote = null;
    private GenericRESTOperator genericRESTOperator;
    private HttpResponse response;
    Logger logger = Logger.getLogger(TestSEDCommands.class);
    @Inject
    private OperatorRegistry<GenericRESTOperator> genericRESTOperatorRegistry;

    /**
     * @throws TimeoutException 
     * @throws IOException 
     * @throws InterruptedException 
     * @DESCRIPTION Verify the functionality of the Automated SED Generation
     * @VUsers 1
     * @PRIORITY MEDIUM
     * 
     */
    @TestId(id = "CIP-5648_Func_1", title = "Verify the functionality of the Automated SED Generation")
    @Context(context = {Context.CLI})
    @Test(groups={"Acceptance"})
    @DataDriven(name = "sedCLICommands")
    public void verifySEDCLIFunctionality(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("host") String hostname,
            @Input("command")String command,
            @Input("localSedFile")String localSedFile,
            @Input("remoteSedFile")String remoteSedFile,
            @Input("args")String args,
            @Output("expectedOutCopyLocalFileToRemote") boolean expectedOutCopyLocalFileToRemote,
            @Output("expectedOut") String expectedOut,
            @Output("expectedErr") String expectedErr,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeOut) throws TimeoutException, IOException, InterruptedException{
        setTestCase(Id, testCaseDescription);
        CliCommandHelperCliOperator cmdHelper = (CliCommandHelperCliOperator) cliToolProvider.provide(CliCommandHelperOperator.class);
        CLIHOST = hostname;
        host = DataHandler.getHostByName(CLIHOST);
        remote = new RemoteFileHandler(host);
        
        if(localSedFile != null && remoteSedFile != null && expectedOutCopyLocalFileToRemote != false){
            setTestStep("Verifying Path to Local SED template file and transfer Local SED file to remote host");
            File file =  FileHandling.getFileToDeploy(localSedFile);
            Path sedFilePath = Paths.get(file.getPath());
            args = args + remoteSedFile;
            status = remote.copyLocalFileToRemote(sedFilePath.toString(), remoteSedFile);
            assertEquals(expectedOutCopyLocalFileToRemote, status);     
        }
        
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, args, timeOut);
        setTestStep("Verifying exit value is as expected");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
        setTestStep("Verifying standard output of command is as expected");
        assertEquals (true, commandResults.get("stdOut").toString().contains(expectedOut));
      
        if(localSedFile != null && remoteSedFile != null && expectedOutCopyLocalFileToRemote != false){     
            setTestStep("Verify the delete of the remote File");
            status = remote.deleteRemoteFile(remoteSedFile);
            assertEquals(expectedOutCopyLocalFileToRemote, status);
        }
    }
    
    
    @Context(context = { Context.REST })
    @DataDriven(name = "sedRESTCommands")
    @Test(groups = { "Functional", "Heartbeat", "vApp", "Physical" })
    @TestId(id = "CIP-6137_Func_1", title = "Verify the functionality of REST call for generating SED")
    public void verifySEDRESTFunctionality(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("HostName") String hostName,
            @Input("Directory") String directory,
            @Input("Parameters") String parameters,
            @Output("Expected") String expected) {
        setTestCase(Id, testCaseDescription);
        
        setTestStep("Run the rest call to Generate SED");
        genericRESTOperator = genericRESTOperatorRegistry.provide(GenericRESTOperator.class);
        
        response = genericRESTOperator.executeREST(hostName, directory, parameters, "GET");
        logger.debug("Response: " + response.getResponseCode());

        assertEquals(expected, response.getResponseCode().toString());
    }
    
}
