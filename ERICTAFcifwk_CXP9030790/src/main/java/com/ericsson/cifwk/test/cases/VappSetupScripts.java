package com.ericsson.cifwk.test.cases ;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;
import com.ericsson.sut.test.operators.CliCommandHelperOperator;
import com.google.inject.Inject;

public class VappSetupScripts extends TorTestCaseHelper implements TestCase {
    public static final String CIPORTALHOST = "ciportal";
    public static final String GATEWAYHOST = "gateway";

    Logger logger = Logger.getLogger(CliCommandHelperOperator.class);

    @Inject
    private OperatorRegistry<CliCommandHelperOperator> cliToolProvider;
    private Host ciPortalHost = DataHandler.getHostByName(CIPORTALHOST);
    private Host gatewayHost = DataHandler.getHostByName(GATEWAYHOST);

    /**
     * @DESCRIPTION vApp Start Script Verification
     * @VUsers 1
     * @PRIORITY MEDIUM
     *
     */
    @TestId(id = "CIP-7482_Func_1", title = "vApp Start Script Verification")
    @Context(context = {Context.CLI})
    @Test(groups={"Acceptance"})
    @DataDriven(name = "vAppSetupScript")
    public void runSetupVapp(
            @Input("nexusDeployUrl") String nexusDeployUrl,
            @Input("nexusDeployRepositoryId") String nexusDeployRepositoryId) {

        CLICommandHelper cmdHelper = new CLICommandHelper(gatewayHost);
        cmdHelper.openShell();

        setTestStep("Cleaning up previously created files");
        cmdHelper.execute("cd /home/lciadm100/");
        cmdHelper.execute("rm -rf setupvapp.sh");
        cmdHelper.execute("rm -rf testupload.txt");
        cmdHelper.execute("rm -rf .m2/settings.xml");
        cmdHelper.execute("rm -rf .m2/settings-security.xml");
        cmdHelper.execute("rm -rf downloaded_pom.xml");

        setTestStep("Downloading the setupvapp.sh script");
        String ciPortalUrl = "https://" + ciPortalHost.getIp();
        cmdHelper.execute("wget --no-check-certificate " + ciPortalUrl + "/static/setupvapp/scripts/setupvapp.sh");
        assertEquals(cmdHelper.getCommandExitValue(), 0);

        setTestStep("Verifying the script executes");
        cmdHelper.execute("chmod +x ./setupvapp.sh");
        assertEquals(cmdHelper.getCommandExitValue(), 0);
        cmdHelper.execute("./setupvapp.sh " + ciPortalUrl);
        assertEquals(cmdHelper.getCommandExitValue(), 0);

        setTestStep("Verifying maven commands to download from nexus works");
        cmdHelper.execute("mvn -B org.apache.maven.plugins:maven-dependency-plugin:2.5.1:get -Dartifact=com.ericsson.cifwk.taf:taf-run-maven-plugin:RELEASE -Dpackaging=pom -Ddest=downloaded_pom.xml");
        assertEquals(cmdHelper.getCommandExitValue(), 0);

        setTestStep("Verifying maven commands to upload to nexus works");
        cmdHelper.execute("echo testupload > testupload.txt");
        assertEquals(cmdHelper.getCommandExitValue(), 0);
        cmdHelper.execute("mvn deploy:deploy-file -DgroupId=com.ericsson.cifwk -DartifactId=testvappupload -Dversion=1.0.1 -Dfile=testupload.txt -DrepositoryId=" + nexusDeployRepositoryId + " -Durl=" + nexusDeployUrl);
        assertEquals(cmdHelper.getCommandExitValue(), 0);

        cmdHelper.disconnect();
    }
}