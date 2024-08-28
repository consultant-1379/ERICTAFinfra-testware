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
import com.ericsson.sut.test.operators.GenericOperator;
import com.google.inject.Inject;

public class PopulateCIFwkDB extends TorTestCaseHelper implements TestCase {
    public static final String CLIHOST = "ciportal";

    @Inject
    private OperatorRegistry<CliCommandHelperOperator> cliToolProvider;
    private Host host = DataHandler.getHostByName(CLIHOST);

    @Inject
    OperatorRegistry<GenericOperator> operatorRegistry;

    @TestId(id = "CIP-4067_Func_4", title = "Populate CI FWK DB")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "populateCIFwkDB")
    public void PopulateCIFwkDb(@Input("loadSql") String loadSql,
            @Input("mkdirDBTmp") String mkdirDBTmp,
            @Input("copyDb") String copyDb,
            @Input("extractDb") String extractDb,
            @Input("rootDir") String rootDir,
            @Input("tmpDBDir") String tmpDBDir,
            @Input("renameSqlFile") String renameSqlFile,
            @Input("rmDb") String rmDb,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {

        CliCommandHelperCliOperator cmdHelper = (CliCommandHelperCliOperator) cliToolProvider
                .provide(CliCommandHelperOperator.class);
        cmdHelper.createOpenConnection(host);
        Map<String, Object> commandResults = cmdHelper
                .executeCommandonSingleHostInstance(mkdirDBTmp, 5);
        setTestStep("Verifying exit value of make temp DB Directory");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));

        commandResults = cmdHelper
                .executeCommandonSingleHostInstance(copyDb, 5);
        setTestStep("Verifying exit value of copy of DB tar file");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));

        commandResults = cmdHelper.executeCommandonSingleHostInstance(tmpDBDir,
                5);
        setTestStep("Verifying exit value of cd to tmp Dir");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));

        commandResults = cmdHelper.executeCommandonSingleHostInstance(
                extractDb, 20);
        setTestStep("Verifying exit value of extraction of DB tar file");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));

        commandResults = cmdHelper.executeCommandonSingleHostInstance(
                renameSqlFile, 20);
        commandResults = cmdHelper.executeCommandonSingleHostInstance(loadSql,
                380);
        setTestStep("Verifying exit value sql load");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));

        commandResults = cmdHelper.executeCommandonSingleHostInstance(rootDir,
                5);
        setTestStep("Verifying exit value of cd to root dir");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));

        commandResults = cmdHelper.executeCommandonSingleHostInstance(rmDb, 5);
        setTestStep("Verifying exit value of removal of db tar file");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));

        cmdHelper.disconnect();
    }
}