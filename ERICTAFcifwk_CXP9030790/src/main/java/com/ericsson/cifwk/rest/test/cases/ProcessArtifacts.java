package com.ericsson.cifwk.rest.test.cases ;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.sut.test.operators.GenericOperator;
import com.google.inject.Inject;


public class ProcessArtifacts extends TorTestCaseHelper implements TestCase {
    
	Map<String, String> artifactNameKeyValueMap = null;
	
	private String serverURL;
	private String postData;
	private List<String> list;
	@Inject
	OperatorRegistry<GenericOperator> operatorRegistry;
	
	
    /**
     * @throws TimeoutException 
     * @DESCRIPTION Verify the ability of CLI Shell commands to be executed
     * @PRE Connection to SUT
     * @VUsers 1
     * @PRIORITY HIGH
     */
    @TestId(id = "CIP-4597_Func_1", title = "Ensure that Artifacts Depentency Mapping Build Flag updated")
    @Context(context = {Context.CLI})
    @Test(groups={"Acceptance"})
    @DataDriven(name = "processArtifacts")
   
    public void verifyCLICommandsCanBeExecuted(
    		@Input("id") String id,
    		@Input("description") String description,
    		@Input("host") String host,
    		@Input("wgetNoCheck") String wgetNoCheck,
    		
    		@Input("groupId") String groupId,
    		@Input("artifactName") String artifactName,
    		@Input("version") String version,
    		@Input("m2Type") String m2Type,

    		@Input("command")String command,
    		@Input("catOutput")String catOutput,
    		@Input("args")String args,
			@Output("expectedOut") String expectedOut,
			@Output("expectedErr") String expectedErr,
			@Output("expectedExit") int expectedExitCode,
			@Input("timeout") int timeout) throws InterruptedException, TimeoutException {
    	
    	GenericOperator cliOperator = operatorRegistry.provide(GenericOperator.class);
    	wgetNoCheck = cliOperator.getCommand(wgetNoCheck);
    	list = new ArrayList<String>();
    	list.add(groupId + ":" + artifactName + ":" + m2Type + ":" + version);
    	
    	serverURL = "https://" + cliOperator.getHostbyName(host).getIp() + "/processArtifactsList/";
    	postData = serverURL + " --data " + "\"" + "artifacts=" + list + "\" > /tmp/output";
    	
    	cliOperator.initializeShell(host);
    	cliOperator.executeCommand("curlNoneSecure", postData);
    	cliOperator.executeCommand("exit");
    	cliOperator.disconnectShell();
    	
    	cliOperator.initializeShell(host);
        cliOperator.executeCommand(catOutput);
        assert (cliOperator.getStdOut().contains(expectedOut));
        assertTrue(cliOperator.getExitCode() == expectedExitCode);
    	cliOperator.executeCommand("exit");
    	cliOperator.disconnectShell();
    }
}
	
