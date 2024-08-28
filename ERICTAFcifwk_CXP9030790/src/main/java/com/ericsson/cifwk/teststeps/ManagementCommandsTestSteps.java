package com.ericsson.cifwk.teststeps;

import java.util.Map;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestStep;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.sut.test.operators.CliCommandHelperCliOperator;

public class ManagementCommandsTestSteps extends TorTestCaseHelper {
    
    @Inject
    CliCommandHelperCliOperator cmdHelper;
    
    private Map<String, Object> commandResults;
    final String commandPropertyPrefix = "ciPortalJenkins.";
    private Host host = DataHandler.getHostByName("ciportal");
    Logger logger = Logger.getLogger(ImageBuildTestSteps.class);
    public static final String MANAGEMENT_COMMANDS = "CIP-4217_Func_1";
    public static final String PRODUCT_MANAGEMENT_COMMANDS = "CIP-4217_Func_2";

    @TestStep(id = MANAGEMENT_COMMANDS)
    public void verifyManagePyFunctionality(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("host") String hostname,
            @Input("command")String command,
            @Input("args")String args,
            @Output("expectedOut") String expectedOut,
            @Output("expectedErr") String expectedErr,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeOut)
    {
        setTestCase(Id, testCaseDescription);   
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, args, timeOut);
        setTestStep("Verifying exit value");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
        setTestStep("Verifying standard output of command");
        assertEquals (true, commandResults.get("stdOut").toString().contains(expectedOut));
    }

    
    @TestStep(id = PRODUCT_MANAGEMENT_COMMANDS)
    public void verifyManagementCommandsForAProduct(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("command") String command,
            @Input("args") String args,
            @Input("pkgName") String pkgName,
            @Input("pkgNameAttribute") String pkgNameAttribute,
            @Input("cxpAttribute") String cxpAttribute,
            @Input("pkgVersion") String pkgVersion,
            @Input("versionAttribute") String versionAttribute,
            @Output("expectedOut") String expectedOut,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeOut,
            @Input("deliverPackage") String deliverPackage
            )
    {
        setTestCase(Id, testCaseDescription);
        if (deliverPackage != null){
            String version = DataHandler.getAttribute(commandPropertyPrefix+versionAttribute).toString();
            String packageName = DataHandler.getAttribute(commandPropertyPrefix+pkgNameAttribute).toString()+"_"+DataHandler.getAttribute(commandPropertyPrefix+cxpAttribute).toString();
            args = args+" "+pkgName+"="+packageName+" "+pkgVersion+"="+version;
        }

        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, args, timeOut);
        setTestStep("Verifying exit value");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
        setTestStep("Verifying standard output of command");
        assertEquals (true, commandResults.get("stdOut").toString().contains(expectedOut));
    }

}
