package com.ericsson.cifwk.ui.test.cases ;

import java.util.Map;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;

import org.testng.annotations.Test;

import com.ericsson.sut.test.operators.*;

public class PRIMSetup extends TorTestCaseHelper implements TestCase {


    final String commandPropertyPrefix = "ciPortalJenkins.";
    GenericRESTOperatorHttp rest = new GenericRESTOperatorHttp();
    HttpResponse response;
    CliCommandHelperCliOperator cmdHelper = new CliCommandHelperCliOperator();
    Map<String, Object> commandResults;


    @TestId(id = "CIP-2055", title = "Setting up variables required for PRIM standalone tests")
    @Context(context = {Context.CLI})
    @Test(groups={"Acceptance"})
    @DataDriven(name = "primSetup")
    public void createPackages(
            @Input("host") String hostname,
            @Input("latestR") String restLatestR,
            @Input("restVersionFromRstate") String restVersionFromR,
            @Input("username") String username,
            @Input("password") String password,
            @Input("packageName") String packageName,
            @Input("nameAttribute") String nameAttribute,
            @Input("number") String cxpNumber,
            @Input("cxpAttribute") String cxpAttribute,
            @Input("version") String version,
            @Input("versionAttribute") String versionAttribute,
            @Input("groupID") String groupID,
            @Input("increment") boolean increment,
            @Input("createPackage") boolean createPackage,
            @Input("timeoutInSeconds") int time) {
    	GenericRESTOperatorHttp rest = new GenericRESTOperatorHttp();
    	CliCommandHelperCliOperator cmdHelper = new CliCommandHelperCliOperator();
    	Host host = DataHandler.getHostByName(hostname);

    	String newVer;
    	if (createPackage){
    		setTestStep("Create Package");

    		String args = "cifwk_createpackage --name="+packageName+"_"+cxpNumber+" --number="+cxpNumber+" --resp="+username;
    		cmdHelper.executeCommandAndReturnCommandDetails(host, "manage.py", args, time);

    		setTestStep("Creating new Package Revision");
    		cmdHelper.executeCommandAndReturnCommandDetails(host, "manage.py", "cifwk_createpackagerevision --name="+packageName+"_"+cxpNumber+" --groupid="+groupID+" --ver="+version, time);
    		newVer = version;


    	}else{

    		setTestStep("Get latest rState");
    		response = rest.executeREST(hostname, restLatestR, "number="+cxpNumber+",user="+username+",password="+password, "GET");
    		String latestRState1 = response.getBody().toString().replace(" ", "");


    		setTestStep("Get Latest Version");
    		response = rest.executeREST(hostname, restVersionFromR, "version="+ latestRState1, "GET");
    		version = response.getBody().toString();

    		if (increment){
    			setTestStep("Creating new Package Revision");
    			newVer = increaseByOne(version);
        		cmdHelper.executeCommandAndReturnCommandDetails(host, "manage.py", "cifwk_createpackagerevision --name="+packageName+"_"+cxpNumber+" --groupid="+groupID+" --ver="+newVer, time);

    		}
    		else {
    			newVer = version;
    		}
    	}
        DataHandler.setAttribute(commandPropertyPrefix+nameAttribute, packageName);
        DataHandler.setAttribute(commandPropertyPrefix+cxpAttribute, cxpNumber);
        DataHandler.setAttribute(commandPropertyPrefix+versionAttribute, newVer);
        assertEquals((String)DataHandler.getAttribute(commandPropertyPrefix+nameAttribute)+DataHandler.getAttribute(commandPropertyPrefix+cxpAttribute)+DataHandler.getAttribute(commandPropertyPrefix+versionAttribute), packageName+cxpNumber+newVer);

    }

    public String increaseByOne(String version){
        String[] parts = version.split("[.]");
        int plusOne = Integer.parseInt(parts[2])+1;
        String newVer = parts[0]+"."+parts[1]+"."+plusOne;
        return newVer;

    }
}