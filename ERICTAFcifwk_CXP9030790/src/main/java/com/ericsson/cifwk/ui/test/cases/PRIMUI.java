package com.ericsson.cifwk.ui.test.cases ;

import java.util.Map;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.guice.*;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;

import org.testng.annotations.Test;

import javax.inject.Inject;

import com.ericsson.sut.test.operators.*;

public class PRIMUI extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<CIPortalOperator> provider;


    final String commandPropertyPrefix = "ciPortalJenkins.";
    GenericRESTOperatorHttp rest = new GenericRESTOperatorHttp();
    CliCommandHelperCliOperator cmdHelper = new CliCommandHelperCliOperator();
    Map<String, Object> commandResults;



    @TestId(id = "CIP-2055", title = "Testing PRIM User Access On CI Portal")
    @Context(context = {Context.UI})
    @Test(groups={"Acceptance"})
    @DataDriven(name = "primUserAccessui")
    public void testingPRIMUserAccess(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("hostname") String hostname,
            @Input("args") String args,
            @Input("username") String username,
            @Input("password") String password,
            @Input("url") String url,
            @Input("product") String product,
            @Output("expected") String expected,
            @Output("expectedOut") String expectedOut,
            @Output("expectedExitCode") int expectedExitCode,
            @Input("restTimeout") long restTimeOut,
            @Input("cliTimeout") int cliTimeOut) {
        setTestCase(Id, testCaseDescription);
        Host host = DataHandler.getHostByName(hostname);
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "manage.py", args, cliTimeOut);
        setTestStep("Verifying exit value");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
        setTestStep("Verifying standard output of command");
        assertEquals (true, commandResults.get("stdOut").toString().contains(expectedOut));
        CIPortalOperator operator = provider.provide(CIPortalOperator.class);
        url = "/"+product+url;
        operator.startBrowser(hostname, url, null);
        String result = operator.checkPRIMUserAccess(username, password, restTimeOut);
        assertEquals(result, expected);
    }



    @TestId(id = "CIP-2055", title = "Testing PRIM Update CXP Numbers")
    @Context(context = {Context.UI})
    @Test(groups={"Acceptance"})
    @DataDriven(name = "primCXPui")
    public void testingPRIMCXPUpdate(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("hostname") String hostname,
            @Input("username") String username,
            @Input("password") String password,
            @Input("url") String url,
            @Input("rStateURL") String rstateURL,
            @Input("restType") String restType,
            @Input("product") String product,
            @Input("numberAttribute") String numberAttribute,
            @Input("versionAttribute") String versionAttribute,
            @Input("media") String media,
            @Input("drop") String drop,
            @Input("write") String write,
            @Input("missing") String missing,
            @Output("expected") String expected,
            @Input("restTimeout") long restTimeOut) {
        setTestCase(Id, testCaseDescription);
        setTestStep("Getting RState");
        String restParameters = "version="+DataHandler.getAttribute(commandPropertyPrefix+versionAttribute).toString();
        HttpResponse response = rest.executeREST(hostname, rstateURL, restParameters, "GET");
        String rstate = response.getBody().toString();

        CIPortalOperator operator = provider.provide(CIPortalOperator.class);
        url = "/"+product+url;
        operator.startBrowser(hostname, url, null);

        String cxpNumber = DataHandler.getAttribute(commandPropertyPrefix+numberAttribute).toString();

        String result = operator.checkPRIMresults(username, password, product, cxpNumber, rstate, drop, media, write, missing, restTimeOut);
        if (expected != null){
        	if(expected.contains("CXP")){
            	expected = DataHandler.getAttribute(commandPropertyPrefix+expected).toString();
            }
            assert (result.contains(expected));
        }
        else{
            assert (result.contains(rstate));
        }
    }
}