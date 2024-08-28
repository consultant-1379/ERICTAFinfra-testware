package com.ericsson.cifwk.install.test.cases ;

import java.io.IOException;

import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.sut.test.operators.GenericOperator;
import com.google.inject.Inject;

public class CreateCIFwkDB extends TorTestCaseHelper implements TestCase {
	@Inject
	OperatorRegistry<GenericOperator> operatorRegistry;
	
	@TestId(id = "CIP-4067_Func_3", title = "Create CI FWK DB")
	@Context(context = {Context.CLI})
	@Test(groups={"Acceptance"})
	@DataDriven(name = "createCIFwkDB")
	public void InitialiseCIFwkDB(
			@Input("host") String hostname,
    		@Input("createDB")String createDB,
    		@Output("expectedExit") int expectedExitCode,
			@Input("timeout") int timeout) throws TimeoutException, IOException, InterruptedException{     
		GenericOperator cliOperator = operatorRegistry.provide(GenericOperator.class);
    	cliOperator.initializeShell(hostname);
        //Set up database
        cliOperator.command(createDB);
    	cliOperator.command("CREATE DATABASE IF NOT EXISTS cireports;");
    	cliOperator.command("GRANT ALL PRIVILEGES ON cireports.* TO 'cireports'@'localhost' IDENTIFIED BY '_cirep';");
    	cliOperator.command("GRANT ALL PRIVILEGES ON cireports.* TO 'cireports'@'%' IDENTIFIED BY '_cirep';");
    	cliOperator.command("exit");
    	assertTrue(cliOperator.getExitCode() == expectedExitCode);
    	cliOperator.executeCommand("exit");
    	cliOperator.disconnectShell();
	}
}
	
