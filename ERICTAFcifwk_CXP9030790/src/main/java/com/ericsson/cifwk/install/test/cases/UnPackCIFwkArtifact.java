package com.ericsson.cifwk.install.test.cases;

import java.io.IOException;

import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.sut.test.operators.GenericOperator;
import com.google.inject.Inject;

public class UnPackCIFwkArtifact extends TorTestCaseHelper implements TestCase {
    @Inject
    OperatorRegistry<GenericOperator> operatorRegistry;

    @TestId(id = "CIP-4067_Func_2", title = "Unpack CI FWK Artifact download from Nexus")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "unPackCIFwkArtifact")
    public void UnPackNexusCIFwkArtifact(@Input("host") String hostname,
            @Input("downloadLoc") String downloadLoc,
            @Input("artifactId") String artifactId,
            @Input("command2") String command2, @Input("args2") String args2,
            @Input("command3") String command3, @Input("stdOut") String stdOut,
            @Input("unZip") String unZip, @Input("list") String list,
            @Input("createSymlink") String createSymlink,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        GenericOperator cliOperator = operatorRegistry
                .provide(GenericOperator.class);
        cliOperator.initializeShell(hostname);
        String version = DataHandler.getAttribute("cifwk.currentVersion")
                .toString();

        //Unzip the Install File
        String verify = "[ -d " + args2 + " ] && echo 'File exists' || mkdir "
                + args2;
        cliOperator.command(verify);
        Thread.sleep(2000);
        cliOperator.executeCommand(command2, args2);
        assertTrue(cliOperator.getExitCode() == expectedExitCode);
        cliOperator.executeCommand(command3);
        assert (cliOperator.getStdOut().contains(stdOut));
        String downloadName = artifactId + "-" + version + ".zip";
        String unZipArgs = downloadLoc + downloadName + " -d " + args2 + "/";
        cliOperator.executeCommand(unZip, unZipArgs);
        assertEquals(cliOperator.getExitCode(),expectedExitCode);
        //Create symlink
        String symlinkArgs = version + " latest";
        cliOperator.executeCommand(createSymlink, symlinkArgs);
        cliOperator.executeCommand(list);
        assert (cliOperator.getStdOut().contains("latest"));
        cliOperator.executeCommand("exit");
        cliOperator.disconnectShell();
    }
}
