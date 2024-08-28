package com.ericsson.cifwk.install.test.cases ;

import java.io.IOException;

import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.sut.test.operators.GenericOperator;
import com.google.inject.Inject;

public class PopulateSSLCerts extends TorTestCaseHelper implements TestCase {
	@Inject
	OperatorRegistry<GenericOperator> operatorRegistry;

	@TestId(id = "CIP-4067_Func_8", title = "Populate CI FWK SSL Certs")
	@Context(context = {Context.CLI})
	@Test(groups={"Acceptance"})
	@DataDriven(name = "populateSSLCert")
	public void PopulateSSLCert(
			@Input("host") String hostname,
    		@Input("mkdir") String mkdir,
    		@Input("changedir") String changedir,
    		@Input("sslDir") String sslDir,
    		@Input("copyssl") String copyssl,
    		@Input("extractssl") String extractssl,
    		@Output("expectedExit") int expectedExitCode,
			@Input("timeout") int timeout) throws TimeoutException, IOException, InterruptedException{     
		GenericOperator cliOperator = operatorRegistry.provide(GenericOperator.class);
    	cliOperator.initializeShell(hostname);
        cliOperator.executeCommand(mkdir, sslDir);
        assertTrue(cliOperator.getExitCode() == expectedExitCode);
        cliOperator.executeCommand(copyssl, sslDir);
        assertTrue(cliOperator.getExitCode() == expectedExitCode);
        cliOperator.executeCommand(changedir, sslDir);
        assertTrue(cliOperator.getExitCode() == expectedExitCode);
        cliOperator.executeCommand(extractssl);
        cliOperator.executeCommand("exit");
    	cliOperator.disconnectShell();
   	}
}
	
