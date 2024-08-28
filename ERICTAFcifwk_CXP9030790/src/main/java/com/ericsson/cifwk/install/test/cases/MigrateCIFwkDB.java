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

public class MigrateCIFwkDB extends TorTestCaseHelper implements TestCase {
    @Inject
    OperatorRegistry<GenericOperator> operatorRegistry;

    @TestId(id = "CIP-4067_Func_4", title = "Populate CI FWK DB")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "migrateCIFwkDB")
    public void MigrateCIFwkDb(@Input("host") String hostname,
            @Input("migrateSql") String migrateSql,
            @Input("generateSqlFile") String generateSqlFile,
            @Input("curl") String curl, @Input("cifWkUrl") String cifWkUrl,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        GenericOperator cliOperator = operatorRegistry
                .provide(GenericOperator.class);
        String portalUrl = DataHandler.getAttribute("cifwk.portal.hyperText")
                .toString();
        cliOperator.initializeShell(hostname);
        cliOperator.executeCommand(curl, portalUrl + cifWkUrl);
        String cifwkCurrentVersion = cliOperator.getStdOut();
        cifwkCurrentVersion = cifwkCurrentVersion.replace("\n", "");
        cifwkCurrentVersion = cifwkCurrentVersion.replaceAll(".*echo", "");
        String latestNexusversion = DataHandler.getAttribute("cifwk.version")
                .toString();
        String currentVersion = cifwkCurrentVersion.replace(".", "");
        String nexusVersion = latestNexusversion.replace(".", "");
        Integer intcurrentVersion = Integer.valueOf(currentVersion);
        Integer intnexusVersion = Integer.valueOf(nexusVersion);
        if (intcurrentVersion < intnexusVersion) {
            String generatedSedSql = cliOperator.getCommand(generateSqlFile);
            generatedSedSql = generatedSedSql.replace("<VERSION>", "-- END "
                    + cifwkCurrentVersion);
            cliOperator.executeCompleteCommand(generatedSedSql);
            cliOperator.executeCommand(migrateSql);
            assertTrue(cliOperator.getExitCode() == expectedExitCode);
        }
        cliOperator.executeCommand("exit");
        cliOperator.disconnectShell();
    }
}
