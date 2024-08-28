package com.ericsson.cifwk.install.test.cases;

import java.io.IOException;
import java.util.Map;

import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.sut.test.operators.CliCommandHelperCliOperator;
import com.ericsson.sut.test.operators.CliCommandHelperOperator;
import com.google.inject.Inject;

public class DownloadCIFwkArtifact extends TorTestCaseHelper implements
        TestCase {
    public static final String CLIHOST = "ciportal";

    @Inject
    private OperatorRegistry<CliCommandHelperOperator> cliToolProvider;
    private Host host = DataHandler.getHostByName(CLIHOST);

    /**
     * @throws TimeoutException
     * @throws IOException
     * @throws InterruptedException
     * @DESCRIPTION Verify the functionality of manage.py commands
     * @VUsers 1
     * @PRIORITY MEDIUM
     * 
     */
    @TestId(id = "CIP-4067_Func_1", title = "Download CI Fwk Artifact from Nexus")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "downloadCIFwkArtifact")
    public void verifyManagePyFunctionality(@Input("host") String hostname,
            @Input("downloadLoc") String downloadLoc,
            @Input("groupId") String groupId,
            @Input("artifactId") String artifactId,
            @Input("nexusUrl") String nexusUrl,
            @Input("downloadFile") String downloadFile,
            @Input("list") String list,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeOut) throws TimeoutException,
            IOException, InterruptedException {
        //setTestCase(Id, testCaseDescription);
        String nexusHostName = DataHandler.getAttribute(
                "cifwk.hubNexus.hyperText").toString();
        String version = DataHandler.getAttribute("cifwk.currentVersion")
                .toString();
        //Download CI Fwk Install File from Nexus
        groupId = groupId.replace(".", "/");
        String downloadName = artifactId + "-" + version + ".zip";
        String fileToGet = nexusHostName + nexusUrl + "/releases/" + groupId
                + "/" + artifactId + "/" + version + "/" + downloadName;
        String downloadFileArgs = downloadLoc + " " + fileToGet;
        CliCommandHelperCliOperator cmdHelper = (CliCommandHelperCliOperator) cliToolProvider
                .provide(CliCommandHelperOperator.class);
        Map<String, Object> commandResults = cmdHelper
                .executeCommandAndReturnCommandDetails(host, downloadFile,
                        downloadFileArgs, timeOut);
        setTestStep("Verifying exit value");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));

    }

}
