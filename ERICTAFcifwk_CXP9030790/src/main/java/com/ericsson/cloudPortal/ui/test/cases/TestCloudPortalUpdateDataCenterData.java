package com.ericsson.cloudPortal.ui.test.cases;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.guice.*;
import com.ericsson.cifwk.utils.XMLHandler;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;

import org.testng.annotations.Test;
import org.w3c.dom.Document;

import javax.inject.Inject;

import com.ericsson.sut.test.operators.*;

public class TestCloudPortalUpdateDataCenterData extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<CloudPortalOperator> CloudPortalOperatorProvider;
    private XMLHandler xmlHandler = new XMLHandler();
    private Host hostname;
    @Context(context = {Context.UI})
    @DataDriven(name = "updateCloudPortalDataCenter")
    @Test(groups = { "Functional", "Heartbeat", "vApp", "Physical" })
    @TestId(id = "CIP-6792_Func_4", title = "Check that the Datacenter can be updated with a RA using Cloud Portal")
  
    public void testCloudPortalUpdateDataCenterWithRA(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("host") String host,
            @Input("Location") String location,
            @Input("RequirementsArea") String requirementsArea,
            @Input("DataCenterName") String dataCenterName,
            @Output("expected") String expected,
            @Input("timeoutInMillis") int timeOut) {
        
        setTestCase(Id, testCaseDescription);
        CloudPortalOperator cloudPortalOperator = CloudPortalOperatorProvider.provide(CloudPortalOperator.class);
        
        setTestStep("Run the rest call to get datacenter id from its name");
        CloudPortalRESTOperator cloudPortalRESTOperator = new CloudPortalRESTOperator();
        HttpResponse response = cloudPortalRESTOperator.listOrgVdcs(timeOut);
        Document document = XMLHandler.createDocument(response.getBody());
        String datacenterId = xmlHandler.getValueBySibling(document,"OrgVdc", "name", dataCenterName, "vcd_id");

        hostname = DataHandler.getHostByName(host);

        if(location == null){
            setTestStep("Run the UI test case to log onto Cloud Portal and Select Actions Option on Data Center");
            cloudPortalOperator.startBrowser(host, null, null);
            String result = cloudPortalOperator.checkActionsOptions(hostname.getUser(), hostname.getPass(), requirementsArea, datacenterId, timeOut);
            assert (result.contains(expected));
        }else{
            setTestStep("Run the UI test case to log onto Cloud Portal and Select Actions Option on Data Center");
            cloudPortalOperator.startBrowser(host, location+datacenterId, null);
            String result2 = cloudPortalOperator.updateDataCenter(hostname.getUser(), hostname.getPass(), requirementsArea, datacenterId, timeOut);
            assert (result2.contains(expected));
        }
        
    }
}
    

