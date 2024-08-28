package com.ericsson.cifwk.test.cases;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.utils.cloud.CloudResponseObject;
import com.ericsson.cifwk.utils.cloud.GenericCloudFunctions;
import com.ericsson.sut.test.operators.CloudPortalCliOperator;
import com.ericsson.sut.test.operators.CloudPortalOperator;
import com.ericsson.sut.test.operators.GenericRESTOperator;
import com.google.inject.Inject;

public class CloudPortal extends TorTestCaseHelper implements TestCase {
    @Inject
    OperatorRegistry<CloudPortalOperator> operatorRegistry;
    @Inject
    OperatorRegistry<GenericRESTOperator> genericOperatorRegistry;
    @Inject
    CloudPortalCliOperator cloudPortalCLIOperator;



    @Context(context = { Context.REST })
    @DataDriven(name = "cloudIsAlive")
    @Test(groups = { "Functional", "Heartbeat" })
    @TestId(id = "CIP-5721_Func_1", title = "Verify the cloud is alive")
    public void isTheCloudAlive(
            @Input("datacenterName") String datacenterName,
            @Output("httpResponseDatacenter") String httpResponseDatacenter,
            @Output("expectedDatacenter")String expectedDatacenter,
            @Output("timeoutDatacenter") int timeoutDatacenter,
            @Output("httpResponseVapps") String httpResponseVapps,
            @Output("expectedVapps") String expectedVapps,
            @Output("timeoutVapps") int timeoutVapps) {

        CloudPortalOperator cloudPortalOperator = operatorRegistry.provide(CloudPortalOperator.class);

        setTestStep("Run the rest call to get datacenter id from its name");
        CloudResponseObject listDatacentersResponse = new CloudResponseObject(cloudPortalOperator.listOrgVdcs(timeoutDatacenter));
        assertEquals(httpResponseDatacenter, listDatacentersResponse.getResponseCode());
        assertTrue(listDatacentersResponse.doesTagExist(expectedDatacenter));

        String datacenterId = listDatacentersResponse.getValueBySibling("OrgVdc", "name", datacenterName, "vcd_id");
        assertNotNull(datacenterId);

        setTestStep("Run the rest call to get vApp names from specific cloud");
        CloudResponseObject listVappsResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, timeoutVapps));
        assertEquals(httpResponseVapps, listVappsResponse.getResponseCode());
        assertTrue(listVappsResponse.doesTagExist(expectedVapps));
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "vappShouldAddToCatalogAsExpected")
    @Test(groups = { "Functional", "Heartbeat" })
    @TestId(id = "CIP-5721_Func_2", title = "Verify the functionality of adding a vapp to a catalog")
    public void vappShouldAddToCatalogAsExpected(
            @Input("localFile") String localFile,
            @Input("remoteLocation") String remoteLocation,
            @Input("vappName") final String vappName,
            @Input("datacenterName1") String datacenterName1,
            @Input("datacenterName2") String datacenterName2,
            @Input("catalogName") String catalogName,
            @Input("vappTemplateName") String vappTemplateName,
            @Output("httpResponseDatacenter") String httpResponseDatacenter,
            @Output("expectedDatacenter") String expectedDatacenter,
            @Output("httpResponseVapp") String httpResponseVapp,
            @Output("expectedVapp") String expectedVapp,
            @Output("httpResponseAdd") String httpResponseAdd,
            @Output("expectedAdd") String expectedAdd,
            @Output("timeoutAdd") int timeoutAdd,
            @Output("httpResponseVappTemplates") String httpResponseVappTemplates,
            @Output("expectedVappTemplates") String expectedVappTemplates,
            @Output("expectedVappTemplateStatus") String expectedVappTemplateStatus,
            @Output("expectedvAppBecameBusy") boolean expectedvAppBecameBusy,
            @Output("expectedVappName") String expectedVappName,
            @Output("expectedHttpStatusSync") String expectedHttpStatusSync,
            @Output("expectedvAppPowerStatus") String expectedvAppPowerStatus,
            @Output("cloudRestTimeout") final int cloudRestTimeout) {

        if (!cloudPortalCLIOperator.isLiveCloudCheck()) {
            cloudPortalCLIOperator.addFileToRemoteHost(localFile, remoteLocation);
        }

        final CloudPortalOperator cloudPortalOperator = operatorRegistry.provide(CloudPortalOperator.class);

        setTestStep("Run the rest call to get datacenter id from its name");
        CloudResponseObject listDatacentersResponse = new CloudResponseObject(cloudPortalOperator.listOrgVdcs(cloudRestTimeout));
        assertEquals(httpResponseDatacenter, listDatacentersResponse.getResponseCode());
        assertTrue(listDatacentersResponse.doesTagExist(expectedDatacenter));

        String datacenterId = listDatacentersResponse.getValueBySibling("OrgVdc", "name", datacenterName1, "vcd_id");
        assertNotNull(datacenterId);

        setTestStep("Run the rest call to get vapp id from its name and datacenter id");
        CloudResponseObject listVappsResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
        assertEquals(httpResponseVapp, listVappsResponse.getResponseCode());
        assertTrue(listVappsResponse.doesTagExist(expectedVapp));

        String vappId = listVappsResponse.getValueBySibling("vapps", "name", vappName, "vapp_id");
        assertNotNull(vappId);

        setTestStep("Start the background thread to check the busy state during execution");
        Thread checkBusyThread = cloudPortalOperator.createBusyCheckerThread(datacenterId, cloudRestTimeout, "vapps", vappName, 10, 1000);
        checkBusyThread.start();

        setTestStep("Run the rest call to add the vApp to the catalog");
        CloudResponseObject addResponse = new CloudResponseObject(cloudPortalOperator.addVappToCatalog(vappId, catalogName, vappTemplateName, timeoutAdd));
        assertEquals(httpResponseAdd, addResponse.getResponseCode());
        assertTrue(addResponse.doesTagExist(expectedAdd));

        setTestStep("Verify the busy state of the vapp during execution");
        boolean vappBecameBusy = !checkBusyThread.isAlive();
        assertEquals(expectedvAppBecameBusy, vappBecameBusy);

        setTestStep("Verify the busy state of the vapp after execution");
        String vAppBusyStatus = listVappsResponse.getValueBySibling("vapps", "name", vappName, "busy");
        assertEquals("0", vAppBusyStatus);

        setTestStep("Run the rest call to check status of various vApp templates");
        CloudResponseObject listVappTemplatesResponse = new CloudResponseObject(cloudPortalOperator.listVappTemplatesInCatalog(catalogName, cloudRestTimeout));
        assertEquals(httpResponseVappTemplates, listVappTemplatesResponse.getResponseCode());
        assertTrue(listVappTemplatesResponse.doesTagExist(expectedVappTemplates));

        String vappTemplateStatus = listVappTemplatesResponse.getValueBySibling("vapptemplates", "vapptemplate_name", vappTemplateName, "status");
        assertEquals(expectedVappTemplateStatus, vappTemplateStatus);

        if (!cloudPortalCLIOperator.isLiveCloudCheck()) {
            sleep(120);
            cloudPortalCLIOperator.removeRemoteFileOnHost(remoteLocation);
            setTestStep("Run the rest call to get datacenter id from its name");
            CloudResponseObject listDatacentersResponse2 = new CloudResponseObject(cloudPortalOperator.listOrgVdcs(cloudRestTimeout));
            assertEquals(expectedHttpStatusSync, listDatacentersResponse2.getResponseCode());
            datacenterId = listDatacentersResponse.getValueBySibling("OrgVdc", "name", datacenterName1, "vcd_id");

            setTestStep("Run the rest call to list the vapps in the datacenter");
            CloudResponseObject listVappsResponse2 = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
            assertEquals(expectedHttpStatusSync, listVappsResponse2.getResponseCode());
            vappId = listVappsResponse2.getValueBySibling("vapps", "name", expectedVappName, "vapp_id");
            assertNotNull(vappId);

            setTestStep("Run the rest call to get datacenter id from its name");
            CloudResponseObject listDatacentersResponse3 = new CloudResponseObject(cloudPortalOperator.listOrgVdcs(cloudRestTimeout));
            assertEquals(expectedHttpStatusSync, listDatacentersResponse3.getResponseCode());
            datacenterId = listDatacentersResponse.getValueBySibling("OrgVdc", "name", datacenterName2, "vcd_id");

            setTestStep("Run the rest call to list the vapps in the datacenter");
            CloudResponseObject listVappsResponse3 = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
            assertEquals(expectedHttpStatusSync, listVappsResponse3.getResponseCode());
            vappId = listVappsResponse3.getValueBySibling("vapps", "name", expectedVappName, "vapp_id");
            assertNotNull(vappId);

            setTestStep("Verify the power status of the created vApp");
            String powerStatus1;
            String powerStatus2;
            String busyStatus1;
            String busyStatus2;
            do {
                listVappsResponse2 = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
                busyStatus1 = listVappsResponse2.getValueBySibling("vapps", "name", expectedVappName, "busy");
                powerStatus1 = listVappsResponse2.getValueBySibling("vapps", "name", expectedVappName, "status");
                listVappsResponse3 = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
                busyStatus2 = listVappsResponse3.getValueBySibling("vapps", "name", expectedVappName, "busy");
                powerStatus2 = listVappsResponse3.getValueBySibling("vapps", "name", expectedVappName, "status");
                sleep(2);
            }while ((!powerStatus1.equals(expectedvAppPowerStatus) && !powerStatus2.equals(expectedvAppPowerStatus)) && (busyStatus1.equals(1) && busyStatus2.equals(1)));
            assertEquals(expectedvAppPowerStatus, powerStatus1.toString());
            assertEquals(expectedvAppPowerStatus, powerStatus2.toString());
        }
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "reportShouldGenerateAsExpected")
    @Test(groups = { "Functional" })
    @TestId(id = "CIP-8276_Func_1", title = "Verify the functionality of generating a report")
    public void reportShouldGenerateAsExpected(
            @Input("report") String report,
            @Input("format") String format,
            @Output("expectedHttpResponse") String expectedHttpResponse,
            @Output("expectedContains") String [] expectedContains,
            @Output("reportTimeout") final int reportTimeout) {

        CloudPortalOperator cloudPortalOperator = operatorRegistry.provide(CloudPortalOperator.class);

        setTestStep("Run the rest call to generate the report");
        HttpResponse generateReportResponse = cloudPortalOperator.generateReport(report, format, reportTimeout);
        assertEquals(generateReportResponse.getResponseCode().toString(),expectedHttpResponse);

        String responseBody = generateReportResponse.getBody();
        int expectedContainsCount = expectedContains.length;
        for (int i = 0; i < expectedContainsCount; i++)
        {
            assertTrue(responseBody.contains(expectedContains[i]));
        }
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "vappTemplateShouldDeleteAsExpected")
    @Test(groups = { "Functional", "Heartbeat" })
    @TestId(id = "CIP-5721_Func_3", title = "Verify the functionality of deleting a vApp template")
    public void vappTemplateShouldDeleteAsExpected(
            @Input("vappTemplateName") String vappTemplateName,
            @Input("catalogName") String catalogName,
            @Output("httpResponseTemplateName") String httpResponseTemplateName,
            @Output("expectedTemplateName") String expectedTemplateName,
            @Output("httpResponseDelete") String httpResponseDelete,
            @Output("expectedDelete") String expectedDelete,
            @Output("timeoutDelete") int timeoutDelete,
            @Output("httpResponseTemplateStatus") String httpResponseTemplateStatus,
            @Output("expectedTemplateStatus") String expectedTemplateStatus,
            @Output("expectedTemplateExists") String expectedTemplateExists,
            @Output("cloudRestTimeout") int cloudRestTimeout) {

        CloudPortalOperator cloudPortalOperator = operatorRegistry.provide(CloudPortalOperator.class);

        setTestStep("Run the rest call to get vApp template id from name");
        CloudResponseObject listVappTemplatesResponse = new CloudResponseObject(cloudPortalOperator.listVappTemplatesInCatalog(catalogName, cloudRestTimeout));
        assertEquals(httpResponseTemplateName, listVappTemplatesResponse.getResponseCode());
        assertTrue(listVappTemplatesResponse.doesTagExist(expectedTemplateName));

        String vappTemplateId = listVappTemplatesResponse.getValueBySibling("vapptemplates", "vapptemplate_name", vappTemplateName, "vapptemplate_id");
        assertNotNull(vappTemplateId);

        setTestStep("Run the rest call to delete the vApp template from catalog");
        CloudResponseObject deleteVappTemplateResponse = new CloudResponseObject(cloudPortalOperator.deleteVappTemplate(vappTemplateId, timeoutDelete));
        assertEquals(httpResponseDelete, deleteVappTemplateResponse.getResponseCode());
        assertTrue(deleteVappTemplateResponse.doesTagExist(expectedDelete));

        setTestStep("Run the rest call to check status of vApp templates");
        CloudResponseObject listVappTemplatesResponseAfter = new CloudResponseObject(cloudPortalOperator.listVappTemplatesInCatalog(catalogName, cloudRestTimeout));
        assertEquals(httpResponseTemplateStatus, listVappTemplatesResponseAfter.getResponseCode());
        assertTrue(listVappTemplatesResponseAfter.doesTagExist(expectedTemplateStatus));

        String vappTemplateStatus = listVappTemplatesResponseAfter.getValueBySibling("vapptemplates", "vapptemplate_name", vappTemplateName, "status");
        assertEquals(vappTemplateStatus != null, expectedTemplateExists.equals("yes"));
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "verifyvApp")
    @Test(groups = { "Functional", "Heartbeat" })
    @TestId(id = "CIP-5722_Func_1", title = "Verify that a vApp can be created in the cloud using a REST call")
    public void verifyvAppCreation(
            @Input("VappTemplateName") String vappTemplateName,
            @Input("CatalogName") String catalogName,
            @Input("VappName") final String vappName,
            @Input("DatacenterName") String datacenterName,
            @Input("PowerOn") String powerOn,
            @Input("LinkedClone") String linkedClone,
            @Input("vcliCommandsEnabled") boolean vcliCommandsEnabled,
            @Input("vappWillCreate") boolean vappWillCreate,
            @Input("useNewQuotaSystem") boolean useNewQuotaSystem,
            @Input("runningVappsQuota") int runningVappsQuota,
            @Input("runningCpusQuota") int runningCpusQuota,
            @Input("runningMemoryQuota") int runningMemoryQuota,
            @Input("totalVappsQuota") int totalVappsQuota,
            @Output("expectedvAppPowerStatus") String expectedvAppPowerStatus,
            @Output("ExpectedHttpStatus") String expectedHttpStatus,
            @Output("expectedCreateHttpStatus") String expectedCreateHttpStatus,
            @Output("expectedHttpSecurityPolicySet") String expectedHttpSecurityPolicySet,
            @Output("cloudRestTimeout") final int cloudRestTimeout,
            @Output("CreationTimeout") int creationTimeout,
            @Output("securityPolicyTimeout") int securityPolicyTimeout,
            @Output("expectedSecurityPolicySetVms") String expectedSecurityPolicySetVms,
            @Output("expectedvAppBecameBusy") boolean expectedvAppBecameBusy) {

        // If the csv is only used for delete, we don't want to test vapp creation. Update when bringing in scenarios
        if (catalogName == null)
        {
            return;
        }

        final CloudPortalOperator cloudPortalOperator = operatorRegistry.provide(CloudPortalOperator.class);
        cloudPortalCLIOperator = new CloudPortalCliOperator();

        setTestStep("Run the rest call to get datacenter id from its name");
        CloudResponseObject listDatacentersResponse = new CloudResponseObject(cloudPortalOperator.listOrgVdcs(cloudRestTimeout));
        final String datacenterId = listDatacentersResponse.getValueBySibling("OrgVdc", "name", datacenterName, "vcd_id");
        assertEquals(expectedHttpStatus, listDatacentersResponse.getResponseCode());

        setTestStep("Setting the provider quota system to use");
        int setQuotaSystemResult = cloudPortalCLIOperator.setProviderNewQuotaSystem(datacenterName,useNewQuotaSystem);
        assertEquals(setQuotaSystemResult,0);

        setTestStep("Setting the datacenter quotas");
        int setQuotaResult = cloudPortalCLIOperator.setOrgVdcQuotas(datacenterName, runningVappsQuota, runningCpusQuota, runningMemoryQuota, totalVappsQuota);
        assertEquals(setQuotaResult,0);

        setTestStep("Run the rest call to get the id of the vapp template to create");
        CloudResponseObject listTemplatesResponse = new CloudResponseObject(cloudPortalOperator.listVappTemplatesInCatalog(catalogName, cloudRestTimeout));
        String templateId = listTemplatesResponse.getValueBySibling("vapptemplates", "vapptemplate_name", vappTemplateName, "vapptemplate_id");
        assertEquals(expectedHttpStatus, listTemplatesResponse.getResponseCode());

        setTestStep("Enable or disable vcli commands depending on csv input");
        if (vcliCommandsEnabled)
        {
            assertEquals(0, cloudPortalCLIOperator.enableVcliCommands());
        }
        else
        {
            assertEquals(0, cloudPortalCLIOperator.disableVcliCommands());
        }

        setTestStep("Start the background thread to check the busy state during execution");
        Thread checkBusyThread = cloudPortalOperator.createBusyCheckerThread(datacenterId, cloudRestTimeout, "vapps", vappName, 120, 10000);
        checkBusyThread.start();

        setTestStep("Run the rest call to create the vApp");
        CloudResponseObject creationResponse = new CloudResponseObject(cloudPortalOperator.createVapp(templateId, vappName, datacenterName, powerOn, linkedClone, creationTimeout));
        assertEquals(creationResponse.getResponseCode(),expectedCreateHttpStatus);

        if (vappWillCreate)
        {
            if (expectedCreateHttpStatus.equals("OK"))
            {
                assertTrue(creationResponse.doesTagExist("vapp_id"));
                if(powerOn == "yes"){
                    assertTrue(creationResponse.doesTagExist("gateway_ipaddress"));
                }
            }

            setTestStep("Run the rest call to list the vapps in the datacenter");
            CloudResponseObject listVappsResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
            assertEquals(expectedHttpStatus, listVappsResponse.getResponseCode());
            String thevApp = listVappsResponse.getValueBySibling("vapps", "name", vappName, "name");
            String theVappId = listVappsResponse.getValueBySibling("vapps", "name", vappName, "vapp_id");
            assertEquals(thevApp, vappName);

            setTestStep("Verify the busy state of the vapp during execution");
            boolean vappBecameBusy = !checkBusyThread.isAlive();
            assertEquals(expectedvAppBecameBusy, vappBecameBusy);

            setTestStep("Verify the busy state of the vapp after execution");
            String vAppBusyStatus = listVappsResponse.getValueBySibling("vapps", "name", vappName, "busy");
            assertEquals("0", vAppBusyStatus);

            setTestStep("Run the rest call to check the security policy status of vms in the vApp");
            assertEquals(0, cloudPortalCLIOperator.enableVcliCommands());
            CloudResponseObject securityPolicySetResponse = new CloudResponseObject(cloudPortalOperator.securityPolicySet(theVappId, securityPolicyTimeout));
            assertEquals(expectedHttpSecurityPolicySet, securityPolicySetResponse.getResponseCode());

            if (expectedSecurityPolicySetVms != null)
            {
                for (String vmName : expectedSecurityPolicySetVms.split(","))
                {
                    String securityPolicySetValue = securityPolicySetResponse.getValueBySibling("vm", "name", vmName, "security_policy_set");
                    assertEquals("1", securityPolicySetValue);
                }
            }

            if (expectedCreateHttpStatus.equals("OK"))
            {
                if (powerOn == "yes") {
                    setTestStep("Verify the vapp list rest call has a gateway ip address for the vapp that was created");
                    String thevAppIp = listVappsResponse.getValueBySibling("vapps", "name", vappName, "gateway_ipaddress");
                    assertFalse(thevAppIp.equals(""));
                }

                setTestStep("Verify the power status of the created vApp");
                String powerStatus = listVappsResponse.getValueBySibling("vapps", "name", vappName, "status");
                assertEquals(expectedvAppPowerStatus, powerStatus);

                if (powerOn == "yes") {
                    setTestStep("Verify the Gateway to SPP mapping was added to the CI Portal correctly");
                    GenericRESTOperator websiteOperator = genericOperatorRegistry.provide(GenericRESTOperator.class);
                    String thevAppHostname = listVappsResponse.getValueBySibling("vapps", "name", vappName, "gateway_hostname");
                    HttpResponse result = websiteOperator.executeREST("ciportal", "/getSpp/", "gateway=" + thevAppHostname, "GET");
                    String ciPortalMappedSPPHostname = result.getBody();
                    String expectedSPPHostname = "https://" + DataHandler.getAttribute("host.cloudportal.hostname").toString() + "/";
                    assertEquals(ciPortalMappedSPPHostname, expectedSPPHostname);
                }
            }
        }
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "verifyVMPower")
    @Test(groups = { "Functional", "Heartbeat" })
    @TestId(id = "CIP-5722_Func_2", title = "Verify that a vm in a vApp can powered on and off, or vice versa, using a REST call")
    public void verifyVMPower(
            @Input("VappName") String vappName,
            @Input("VmName") final String vmName,
            @Input("DatacenterName") String datacenterName,
            @Input("PowerOnVm") boolean powerOnVm,
            @Input("useNewQuotaSystem") boolean useNewQuotaSystem,
            @Input("runningVappsQuota") int runningVappsQuota,
            @Input("runningCpusQuota") int runningCpusQuota,
            @Input("runningMemoryQuota") int runningMemoryQuota,
            @Input("totalVappsQuota") int totalVappsQuota,
            @Output("ExpectedVMStatus") String expectedVMStatus,
            @Output("ExpectedHttpStatus") String expectedHttpStatus,
            @Output("powerTimeout") int powerTimeout,
            @Output("expectedHttpPower") String expectedHttpPower,
            @Output("expectedPower") String expectedPower,
            @Output("vcliCommandsEnabled") boolean vcliCommandsEnabled,
            @Output("expectedHttpSecurityPolicySet") String expectedHttpSecurityPolicySet,
            @Output("securityPolicyTimeout") int securityPolicyTimeout,
            @Output("expectedSecurityPolicySetVms") String expectedSecurityPolicySetVms,
            @Output("expectedVMBecameBusy") boolean expectedVMBecameBusy,
            @Output("cloudRestTimeout") final int cloudRestTimeout) {

        setTestStep("Enable or disable vcli commands depending on csv input");
        if (vcliCommandsEnabled)
        {
            assertEquals(0, cloudPortalCLIOperator.enableVcliCommands());
        }
        else
        {
            assertEquals(0, cloudPortalCLIOperator.disableVcliCommands());
        }

        setTestStep("Run the rest call to get datacenter id from its name");
        final CloudPortalOperator cloudPortalOperator = operatorRegistry.provide(CloudPortalOperator.class);
        CloudResponseObject listDatacentersResponse = new CloudResponseObject(cloudPortalOperator.listOrgVdcs(cloudRestTimeout));
        assertEquals(listDatacentersResponse.getResponseCode(),expectedHttpStatus);
        String datacenterId = listDatacentersResponse.getValueBySibling("OrgVdc", "name", datacenterName, "vcd_id");

        setTestStep("Setting the provider quota system to use");
        int setQuotaSystemResult = cloudPortalCLIOperator.setProviderNewQuotaSystem(datacenterName,useNewQuotaSystem);
        assertEquals(setQuotaSystemResult,0);

        setTestStep("Setting the datacenter quotas");
        int setQuotaResult = cloudPortalCLIOperator.setOrgVdcQuotas(datacenterName, runningVappsQuota, runningCpusQuota, runningMemoryQuota, totalVappsQuota);
        assertEquals(setQuotaResult,0);

        setTestStep("Run the rest call to list the vapps in the datacenter");
        CloudResponseObject listVappsResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
        assertEquals(expectedHttpStatus, listVappsResponse.getResponseCode());
        final String vappId = listVappsResponse.getValueBySibling("vapps", "name", vappName, "vapp_id");
        assertNotNull(vappId);

        setTestStep("Run the rest call to list the vms in the vapp");
        CloudResponseObject listVmsResponse = new CloudResponseObject(cloudPortalOperator.listVmsInVapp(vappId, cloudRestTimeout));
        assertEquals(expectedHttpStatus, listVmsResponse.getResponseCode());
        String vmId = listVmsResponse.getValueBySibling("vms", "name", vmName, "vm_id");
        assertNotNull(vmId);

        Thread checkBusyThread = null;
        if (!cloudPortalCLIOperator.isLiveCloudCheck()) {
            setTestStep("Start the background thread to check the busy state during execution");
            checkBusyThread = cloudPortalOperator.createBusyCheckerThread(vappId, cloudRestTimeout, "vms", vmName, 10, 1000);
            checkBusyThread.start();
        }

        setTestStep("Run the call to set the power state of the vm");
        assertEquals(0, cloudPortalCLIOperator.enableSlowPowerOff());
        CloudResponseObject executionResponse = new CloudResponseObject(cloudPortalOperator.testVmPower(vmName, vmId, powerOnVm, powerTimeout));
        assertEquals(expectedHttpPower, executionResponse.getResponseCode());
        assertTrue(executionResponse.doesTagExist(expectedPower));
        assertEquals(0, cloudPortalCLIOperator.disableSlowPowerOff());

        if (!cloudPortalCLIOperator.isLiveCloudCheck())
        {
            setTestStep("Verify the busy state of the vm during execution");
            boolean vmBecameBusy = !checkBusyThread.isAlive();
            assertEquals(vmBecameBusy, expectedVMBecameBusy);
        }

        setTestStep("Verify the busy state of the vm after execution");
        String vmBusyStatus = listVmsResponse.getValueBySibling("vms", "name", vmName, "busy");
        assertEquals("0", vmBusyStatus);

        setTestStep("Run the rest all to verify the power state of the vm");
        listVmsResponse = new CloudResponseObject(cloudPortalOperator.listVmsInVapp(vappId, cloudRestTimeout));
        assertEquals(expectedHttpStatus, listVmsResponse.getResponseCode());
        String VMStatus = listVmsResponse.getValueBySibling("vms", "name", vmName, "status");
        assertEquals(expectedVMStatus, VMStatus);

        setTestStep("Run the rest call to check the security policy status of vms in the vApp");
        assertEquals(0, cloudPortalCLIOperator.enableVcliCommands());
        CloudResponseObject securityPolicySetResponse = new CloudResponseObject(cloudPortalOperator.securityPolicySet(vappId, securityPolicyTimeout));
        assertEquals(securityPolicySetResponse.getResponseCode(),expectedHttpSecurityPolicySet);

        if (expectedSecurityPolicySetVms != null)
        {
            for (String vmNameToCheck : expectedSecurityPolicySetVms.split(","))
            {
                String securityPolicySetValue = securityPolicySetResponse.getValueBySibling("vm", "name", vmNameToCheck, "security_policy_set");
                assertEquals("1", securityPolicySetValue);
            }
        }
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "verifyVMDelete")
    @Test(groups = { "Functional", "Heartbeat" })
    @TestId(id = "CIP-6685_Func_1", title = "Verify that a vm in a vApp can be deleted using a REST call")
    public void verifyVMDelete(
            @Input("VappName") String vappName,
            @Input("VmName") final String vmName,
            @Input("DatacenterName") String datacenterName,
            @Input("PowerOffVm") boolean powerOffVm,
            @Output("ExpectedVMStatus") String expectedVMStatus,
            @Output("ExpectedInitialVMStatus") String expectedInitialVMStatus,
            @Output("ExpectedHttpStatus") String expectedHttpStatus,
            @Output("powerTimeout") int powerTimeout,
            @Output("expectedHttpPower") String expectedHttpPower,
            @Output("expectedVMBecameBusy") boolean expectedVMBecameBusy,
            @Output("cloudRestTimeout") final int cloudRestTimeout) {

        setTestStep("Run the rest call to get datacenter id from its name");
        final CloudPortalOperator cloudPortalOperator = operatorRegistry
                .provide(CloudPortalOperator.class);
        CloudResponseObject listDatacentersResponse = new CloudResponseObject(cloudPortalOperator.listOrgVdcs(cloudRestTimeout));
        assertEquals(expectedHttpStatus, listDatacentersResponse.getResponseCode());
        String datacenterId = listDatacentersResponse.getValueBySibling("OrgVdc", "name", datacenterName, "vcd_id");

        setTestStep("Run the rest call to list the vapps in the datacenter");
        CloudResponseObject listVappsResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
        assertEquals(expectedHttpStatus, listVappsResponse.getResponseCode());
        final String vappId = listVappsResponse.getValueBySibling("vapps", "name", vappName, "vapp_id");
        assertNotNull(vappId);

        setTestStep("Run the rest call to list the vms in the vapp");
        CloudResponseObject listVmsResponse = new CloudResponseObject(cloudPortalOperator.listVmsInVapp(vappId, cloudRestTimeout));
        assertEquals(expectedHttpStatus, listVmsResponse.getResponseCode());
        String vmId = listVmsResponse.getValueBySibling("vms", "name", vmName, "vm_id");
        assertNotNull(vmId);

        if (powerOffVm) {
            setTestStep("Run the call to set the power state of the vm");
            assertEquals(0, cloudPortalCLIOperator.enableSlowPowerOff());
            CloudResponseObject executionResponsePowerOff = new CloudResponseObject(cloudPortalOperator.testVmPower(vmName, vmId, false, powerTimeout));
            assertEquals(0, cloudPortalCLIOperator.disableSlowPowerOff());
        }

        setTestStep("Run the rest all to verify the VM is in desired state to begin test");
        listVmsResponse = new CloudResponseObject(cloudPortalOperator.listVmsInVapp(vappId, cloudRestTimeout));
        assertEquals(expectedHttpStatus, listVmsResponse.getResponseCode());
        String VMStatus = listVmsResponse.getValueBySibling("vms", "name", vmName, "status");
        assertEquals(expectedInitialVMStatus, VMStatus);

        Thread checkBusyThread = null;
        if (!cloudPortalCLIOperator.isLiveCloudCheck()) {
            setTestStep("Start the background thread to check the busy state during execution");
            checkBusyThread = cloudPortalOperator.createBusyCheckerThread(
                    vappId, cloudRestTimeout, "vms", vmName, 10, 1000);
            checkBusyThread.start();
        }

        setTestStep("Run the call to delete the vm");
        CloudResponseObject executionResponse = new CloudResponseObject(cloudPortalOperator.testVmDelete(vmName, vmId, powerTimeout));
        assertEquals(expectedHttpPower, executionResponse.getResponseCode());

        if (!cloudPortalCLIOperator.isLiveCloudCheck()) {
            setTestStep("Verify the busy state of the vm during execution");
            boolean vmBecameBusy = !checkBusyThread.isAlive();
            assertEquals(vmBecameBusy, expectedVMBecameBusy);
        }

        setTestStep("Run the rest all to verify the state of the vm");
        listVmsResponse = new CloudResponseObject(cloudPortalOperator.listVmsInVapp(vappId, cloudRestTimeout));
        assertEquals(expectedHttpStatus, listVmsResponse.getResponseCode());
        VMStatus = listVmsResponse.getValueBySibling("vms", "name", vmName, "status");
        assertEquals(expectedVMStatus, VMStatus);

    }

    @Context(context = { Context.REST })
    @DataDriven(name = "vappShouldStopAsExpected")
    @Test(groups = { "Functional", "Heartbeat" })
    @TestId(id = "CIP-5721_Func_4", title = "Verify the functionality of stopping a vApp")
    public void vappShouldStopAsExpected(
            @Input("vappName") final String vappName,
            @Input("datacenterName") String datacenterName,
            @Output("httpResponseDatacenter") String httpResponseDatacenter,
            @Output("expectedDatacenter") String expectedDatacenter,
            @Output("httpResponseVapp") String httpResponseVapp,
            @Output("expectedVapp") String expectedVapp,
            @Output("httpResponseStop") String httpResponseStop,
            @Output("expectedStop") String expectedStop,
            @Output("timeoutStop") int timeoutStop,
            @Output("httpResponseStatus") String httpResponseStatus,
            @Output("expectedStatus") String expectedStatus,
            @Output("expectedPowerStatus") String expectedPowerStatus,
            @Output("expectedvAppBecameBusy") boolean expectedvAppBecameBusy,
            @Output("cloudRestTimeout") final int cloudRestTimeout) {

        final CloudPortalOperator cloudPortalOperator = operatorRegistry.provide(CloudPortalOperator.class);

        setTestStep("Run the rest call to get datacenter id from its name");
        CloudResponseObject listDatacentersResponse = new CloudResponseObject(cloudPortalOperator.listOrgVdcs(cloudRestTimeout));
        assertEquals(httpResponseDatacenter, listDatacentersResponse.getResponseCode());
        assertTrue(listDatacentersResponse.doesTagExist(expectedDatacenter));

        final String datacenterId = listDatacentersResponse.getValueBySibling("OrgVdc", "name", datacenterName, "vcd_id");
        assertNotNull(datacenterId);

        setTestStep("Run the rest call to get vapp id from its name and datacenter id");
        CloudResponseObject listVappsResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
        assertEquals(httpResponseVapp, listVappsResponse.getResponseCode());
        assertTrue(listVappsResponse.doesTagExist(expectedVapp));

        String vappId = listVappsResponse.getValueBySibling("vapps", "name", vappName, "vapp_id");
        assertNotNull(vappId);
        
        setTestStep("Start the background thread to check the busy state during execution");
        Thread checkBusyThread = cloudPortalOperator.createBusyCheckerThread(datacenterId, cloudRestTimeout, "vapps", vappName, 10, 1000);
        checkBusyThread.start();

        setTestStep("Run the rest call to stop the vApp");
        CloudResponseObject addResponse = new CloudResponseObject(cloudPortalOperator.stopVapp(vappId, timeoutStop));
        assertEquals(httpResponseStop, addResponse.getResponseCode());
        assertTrue(addResponse.doesTagExist(expectedStop));
        
        setTestStep("Verify the busy state of the vapp during execution");
        boolean vappBecameBusy = !checkBusyThread.isAlive();
        assertEquals(expectedvAppBecameBusy, vappBecameBusy);
        
        setTestStep("Verify the busy state of the vapp after execution");
        String vAppBusyStatus = listVappsResponse.getValueBySibling("vapps", "name", vappName, "busy");
        assertEquals("0", vAppBusyStatus);

        setTestStep("Run the rest call to check status of the vapp");
        CloudResponseObject listVappsAfterResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
        assertEquals(httpResponseStatus, listVappsAfterResponse.getResponseCode());
        assertTrue(listVappsAfterResponse.doesTagExist(expectedStatus));

        String vappPowerStatus = listVappsAfterResponse.getValueBySibling("vapps", "name", vappName, "status");
        assertEquals(expectedPowerStatus, vappPowerStatus);

    }

    @Context(context = { Context.REST })
    @DataDriven(name = "verifyVMResize")
    @Test(groups = { "Functional", "Heartbeat" })
    @TestId(id = "CIP-6696_Func_1", title = "Test Cloud SPP REST for Resizing VM")
    public void verifyVMresize(
            @Input("VappName") String vappName,
            @Input("VmName") final String vmName,
            @Input("DatacenterName") String datacenterName,
            @Input("BaseUrl") String baseURL,
            @Input("RESTparameter") String parameter,
            @Input("Value") String value,
            @Input("HotAdd") String hotAdd,
            @Input("PowerOffVm") boolean powerOffVm,
            @Output("ExpectedVMStatus") String expectedVMStatus,
            @Output("ExpectedInitialVMStatus") String expectedInitialVMStatus,
            @Output("ExpectedHttpStatus") String expectedHttpStatus,
            @Output("powerTimeout") int powerTimeout,
            @Output("expectedHttpPower") String expectedHttpPower,
            @Output("expectedVMBecameBusy") boolean expectedVMBecameBusy,
            @Output("cloudRestTimeout") final int cloudRestTimeout) {

        if (value == null){
            value = "";
        }
        if (hotAdd == null){
            hotAdd = "";
        }

        setTestStep("Run the rest call to get datacenter id from its name");
        final CloudPortalOperator cloudPortalOperator = operatorRegistry.provide(CloudPortalOperator.class);
        CloudResponseObject listDatacentersResponse = new CloudResponseObject(cloudPortalOperator.listOrgVdcs(cloudRestTimeout));
        assertEquals(expectedHttpStatus, listDatacentersResponse.getResponseCode());
        String datacenterId = listDatacentersResponse.getValueBySibling("OrgVdc", "name", datacenterName, "vcd_id");

        setTestStep("Run the rest call to list the vapps in the datacenter");
        CloudResponseObject listVappsResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
        assertEquals(expectedHttpStatus, listVappsResponse.getResponseCode());
        final String vappId = listVappsResponse.getValueBySibling("vapps", "name", vappName, "vapp_id");
        assertNotNull(vappId);

        setTestStep("Run the rest call to list the vms in the vapp");
        CloudResponseObject listVmsResponse = new CloudResponseObject(cloudPortalOperator.listVmsInVapp(vappId, cloudRestTimeout));
        assertEquals(expectedHttpStatus, listVmsResponse.getResponseCode());
        String vmId = listVmsResponse.getValueBySibling("vms", "name", vmName, "vm_id");
        assertNotNull(vmId);

        if (powerOffVm) {
            setTestStep("Run the call to set the power state of the vm");
            assertEquals(0, cloudPortalCLIOperator.enableSlowPowerOff());
            CloudResponseObject executionResponsePowerOff = new CloudResponseObject(cloudPortalOperator.testVmPower(vmName, vmId, false, powerTimeout));
            assertEquals(0, cloudPortalCLIOperator.disableSlowPowerOff());
        }

        setTestStep("Run the rest all to verify the VM is in desired state to begin test");
        listVmsResponse = new CloudResponseObject(
                cloudPortalOperator.listVmsInVapp(vappId, cloudRestTimeout));
        assertEquals(expectedHttpStatus, listVmsResponse.getResponseCode());
        String VMStatus = listVmsResponse.getValueBySibling("vms", "name", vmName, "status");
        assertEquals(expectedInitialVMStatus, VMStatus);

        Thread checkBusyThread = null;
        if (!cloudPortalCLIOperator.isLiveCloudCheck()) {
                setTestStep("Start the background thread to check the busy state during execution");
                checkBusyThread = cloudPortalOperator.createBusyCheckerThread(vappId, cloudRestTimeout, "vms", vmName, 10, 1000);
                checkBusyThread.start();
        }

        setTestStep("Run the call to Resize the vm");
        assertEquals(0, cloudPortalCLIOperator.enableSlowResizeMemory());
        assertEquals(0, cloudPortalCLIOperator.enableSlowResizeCpu());
        CloudResponseObject executionResponse = new CloudResponseObject(cloudPortalOperator.testVmResize(baseURL, parameter, value, hotAdd, vmName, vmId, powerTimeout));
        assertEquals(expectedHttpPower, executionResponse.getResponseCode());
        assertEquals(0, cloudPortalCLIOperator.disableSlowResizeMemory());
        assertEquals(0, cloudPortalCLIOperator.disableSlowResizeCpu());

        if (!cloudPortalCLIOperator.isLiveCloudCheck()) {
           setTestStep("Verify the busy state of the vm during execution");
           boolean vmBecameBusy = !checkBusyThread.isAlive();
           assertEquals(expectedVMBecameBusy, vmBecameBusy);
        }

        setTestStep("Run the rest all to verify the state of the vm");
        listVmsResponse = new CloudResponseObject(cloudPortalOperator.listVmsInVapp(vappId, cloudRestTimeout));
        assertEquals(expectedHttpStatus, listVmsResponse.getResponseCode());
        VMStatus = listVmsResponse.getValueBySibling("vms", "name", vmName, "status");
        assertEquals(expectedVMStatus, VMStatus);

    }

    @Context(context = { Context.REST })
    @DataDriven(name = "verifyvApp")
    @Test(groups = { "Functional", "Heartbeat" })
    @TestId(id = "CIP-5722_Func_3", title = "Verify that a vApp can be removed from the cloud using a REST call")
    public void verifyvAppRemoval(
            @Input("VappName") String vappName,
            @Input("DatacenterName") String datacenterName,
            @Input("vappWillCreate") boolean vappWillCreate,
            @Input("vappForPoweroffAndDelete") boolean vappForPoweroffAndDelete,
            @Input("MakeVappBusy") boolean makeVappBusy,
            @Output("ExpectedRemoval") String expectedRemoval,
            @Output("ExpectedHttpStatus") String expectedHttpStatus,
            @Output("cloudRestTimeout") int cloudRestTimeout,
            @Output("DeleteTimeout") int deleteTimeout) {

        if (vappWillCreate == true && vappForPoweroffAndDelete == false)
        {
            CloudPortalOperator cloudPortalOperator = operatorRegistry.provide(CloudPortalOperator.class);
            CloudResponseObject listDatacentersResponse = new CloudResponseObject(cloudPortalOperator.listOrgVdcs(cloudRestTimeout));
            assertEquals(expectedHttpStatus, listDatacentersResponse.getResponseCode());

            String datacenterId = listDatacentersResponse.getValueBySibling("OrgVdc", "name", datacenterName, "vcd_id");
            CloudResponseObject listVappsResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
            assertEquals(expectedHttpStatus, listVappsResponse.getResponseCode());     
            String thevAppId = listVappsResponse.getValueBySibling("vapps", "name", vappName, "vapp_id");
            if(makeVappBusy)
            {
                setTestStep("Make the Vapp busy");
                CloudResponseObject busyResponse = new CloudResponseObject(cloudPortalOperator.makeVappsBusy(thevAppId, cloudRestTimeout,Boolean.toString(makeVappBusy))) ;
                assertEquals(busyResponse.getResponseCode(), "OK");
                setTestStep("Run the rest call to delete the vapp");
                thevAppId = listVappsResponse.getValueBySibling("vapps", "name", vappName, "vapp_id");
                CloudResponseObject deletionResponse = new CloudResponseObject(cloudPortalOperator.deleteVapp(thevAppId, deleteTimeout));
                assertEquals(deletionResponse.getResponseCode(), "BAD_REQUEST");
                setTestStep("Unset the Vapp busy");
                busyResponse = new CloudResponseObject(cloudPortalOperator.makeVappsBusy(thevAppId, cloudRestTimeout,"false")) ;
                assertEquals(busyResponse.getResponseCode(), "OK");
            }

            setTestStep("Run the rest call to delete the vapp");
            CloudResponseObject deletionResponse = new CloudResponseObject(cloudPortalOperator.deleteVapp(thevAppId, deleteTimeout));
            assertEquals(deletionResponse.getResponseCode(), expectedHttpStatus);

            listVappsResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
            assertEquals(expectedHttpStatus, listVappsResponse.getResponseCode());

            String vappStatus = listVappsResponse.getValueBySibling("vapps", "name", vappName, "vapp_id");
            assertNull(vappStatus);
        }
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "verifyvApp")
    @Test(groups = { "Functional", "Heartbeat" })
    @TestId(id = "CIP-7051_Func_1", title = "Testing the REST Call to destroy a vapp on Cloud Portal")
    public void verifyvAppPowerOffAndDelete(
            @Input("VappName") String vappName,
            @Input("DatacenterName") String datacenterName,
            @Input("vappForPoweroffAndDelete") boolean vappForPoweroffAndDelete,
            @Output("ExpectedRemoval") String expectedRemoval,
            @Output("ExpectedHttpStatus") String expectedHttpStatus,
            @Output("cloudRestTimeout") int cloudRestTimeout,
            @Output("DeleteTimeout") int deleteTimeout) {

        if (vappForPoweroffAndDelete)
        {
            CloudPortalOperator cloudPortalOperator = operatorRegistry.provide(CloudPortalOperator.class);
            CloudResponseObject listDatacentersResponse = new CloudResponseObject(cloudPortalOperator.listOrgVdcs(cloudRestTimeout));
            assertEquals(expectedHttpStatus, listDatacentersResponse.getResponseCode());

            String datacenterId = listDatacentersResponse.getValueBySibling("OrgVdc", "name", datacenterName, "vcd_id");
            CloudResponseObject listVappsResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
            assertEquals(expectedHttpStatus, listVappsResponse.getResponseCode());

            setTestStep("Run the call to power off and delete the vapp");
            String thevAppId = listVappsResponse.getValueBySibling("vapps", "name", vappName, "vapp_id");
            CloudResponseObject deletionResponse = new CloudResponseObject(cloudPortalOperator.poweroffAndDeleteVapp(thevAppId, deleteTimeout));
            assertEquals(expectedHttpStatus, deletionResponse.getResponseCode());

            listVappsResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
            assertEquals(expectedHttpStatus, listVappsResponse.getResponseCode());

            String vappStatus = listVappsResponse.getValueBySibling("vapps", "name", vappName, "vapp_id");
            assertNull(vappStatus);
        }
    }

    @Context(context = { Context.REST })
    @DataDriven(name = "verifyRecomposevApp")
    @Test(groups = { "Functional", "Heartbeat" })
    @TestId(id = "CIP-7892_Func_1", title = "Verify that a vApp can be recomposed in the cloud using a REST call")
    public void verifyvAppRecompose(@Input("VappTemplateName") String vappTemplateName,
            @Input("CatalogName") String catalogName,
            @Input("VappName") final String vappName,
            @Input("DatacenterName") String datacenterName,
            @Input("Vms") String[] vms,
            @Input("MakeVappBusy") boolean makeVappBusy,
            @Input("useNewQuotaSystem") boolean useNewQuotaSystem,
            @Input("runningVappsQuota") int runningVappsQuota,
            @Input("runningCpusQuota") int runningCpusQuota,
            @Input("runningMemoryQuota") int runningMemoryQuota,
            @Input("totalVappsQuota") int totalVappsQuota,
            @Output("ExpectedOut") String expectedOut,
            @Output("ExpectedHttpStatus") String expectedHttpStatus,
            @Output("ExpectedHttpStatusRecompose") String expectedHttpStatusRecompose,
            @Output("CloudRestTimeout") int cloudRestTimeout,
            @Output("Expected") boolean expected,
            @Output("ExpectedvAppBecameBusy") boolean expectedvAppBecameBusy,
            @Output("ExpectedvAppPowerStatus") String expectedvAppPowerStatus,
            @Output("ExpectedInList") boolean expectedInList,
            @Output("Timeout") int timeout) {

        final CloudPortalOperator cloudPortalOperator = operatorRegistry.provide(CloudPortalOperator.class);
        cloudPortalCLIOperator = new CloudPortalCliOperator();

        setTestStep("Run the rest call to get datacenter id from its name");
        CloudResponseObject listDatacentersResponse = new CloudResponseObject(cloudPortalOperator.listOrgVdcs(cloudRestTimeout));
        final String datacenterId = listDatacentersResponse.getValueBySibling("OrgVdc", "name", datacenterName, "vcd_id");
        assertEquals(expectedHttpStatus, listDatacentersResponse.getResponseCode());

        setTestStep("Setting the provider quota system to use");
        int setQuotaSystemResult = cloudPortalCLIOperator.setProviderNewQuotaSystem(datacenterName,useNewQuotaSystem);
        assertEquals(setQuotaSystemResult,0);

        setTestStep("Setting the datacenter quotas");
        int setQuotaResult = cloudPortalCLIOperator.setOrgVdcQuotas(datacenterName, runningVappsQuota, runningCpusQuota, runningMemoryQuota, totalVappsQuota);
        assertEquals(setQuotaResult,0);

        setTestStep("Run the rest call to get the id of the vapp template");
        CloudResponseObject listTemplatesResponse = new CloudResponseObject(cloudPortalOperator.listVappTemplatesInCatalog(catalogName,
                cloudRestTimeout));
        String templateId = listTemplatesResponse.getValueBySibling("vapptemplates", "vapptemplate_name", vappTemplateName, "vapptemplate_id");
        assertEquals(expectedHttpStatus, listTemplatesResponse.getResponseCode());

        setTestStep("Run the rest call to list the vapps in the datacenter");
        CloudResponseObject listVappsResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
        assertEquals(expectedHttpStatus, listVappsResponse.getResponseCode());
        final String vAppId = listVappsResponse.getValueBySibling("vapps", "name", vappName, "vapp_id");
        assertNotNull(vAppId);

        setTestStep("Run the rest call to list the vms Macs in the vapp before");
        CloudResponseObject listVmsvAppMacResponse1 = new CloudResponseObject(cloudPortalOperator.listVmsNetworkDetailsInVapp(vAppId, 100));
        assertEquals(expectedHttpStatus, listVmsvAppMacResponse1.getResponseCode());

        setTestStep("Verify the vApp power status of the vapp before execution");
        CloudResponseObject listVappsResponse2 = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
        String powerStatus = listVappsResponse2.getValueBySibling("vapps", "name", vappName, "status");
        assertEquals(expectedvAppPowerStatus, powerStatus.toString());

        String events = "{\"devGroup\": [{\"id\": \"5\"}],\"dataCenters\":[{\"name\":\"Axis_OrgDC1\"}],\"startTime\":[{\"time\":\"2014-01-01 14:00:00\"}],\"endTime\":[{\"time\":\"2030-01-01 14:00:00\"}],\"eventTypes\":[{\"name\":\"recompose_vapp\"}]}";

        setTestStep("Run the rest call to get Events");
        HttpResponse eventsResponse = cloudPortalOperator.eventsSPP(events, timeout);
        assertEquals(expectedHttpStatus, eventsResponse.getResponseCode().toString());

        int count1 = GenericCloudFunctions.eventCount(eventsResponse.getBody().toString());

        JSONObject jsonBody = new JSONObject();
        JSONArray vmList = new JSONArray();
        ArrayList<String> listOfVMsvApp = new ArrayList<String>();
        ArrayList<String> listOfVMsvAppOrder = new ArrayList<String>();

        if (!vms.toString().contains("NULL")) {
            for (String vmName : vms) {
                setTestStep("Run the rest call to list the vms in the vapp");
                CloudResponseObject listVmsvAppResponse = new CloudResponseObject(cloudPortalOperator.listVmsInVapp(vAppId, cloudRestTimeout));
                assertEquals(expectedHttpStatus, listVmsvAppResponse.getResponseCode());
                String vmvAppId = listVmsvAppResponse.getValueBySibling("vms", "name", vmName, "vm_id");
                listOfVMsvApp.add(vmvAppId);

                setTestStep("Run the rest call to list the vms Order in the vapp");
                CloudResponseObject listVmsvAppOrderResponse = new CloudResponseObject(cloudPortalOperator.listVmsStartupSettingsInVapp(vAppId,
                        cloudRestTimeout));
                assertEquals(expectedHttpStatus, listVmsvAppOrderResponse.getResponseCode());
                String vmOrder = listVmsvAppOrderResponse.getValueBySibling("vm", "name", vmName, "order");
                listOfVMsvAppOrder.add(vmOrder);

                setTestStep("Run the rest call to get VM ids from vAppTemplate");
                CloudResponseObject listVmsResponse = new CloudResponseObject(cloudPortalOperator.listVmsInVappTemplate(templateId, cloudRestTimeout));
                assertEquals(expectedHttpStatus, listVmsResponse.getResponseCode());
                String vmId = listVmsResponse.getValueBySibling("vms", "name", vmName, "vm_id");
                JSONObject vmIdObj = new JSONObject();
                vmIdObj.put("id", vmId);
                vmList.put(vmIdObj);
            }
        }
        jsonBody.put("vms", vmList);

        setTestStep("Start the background thread to check the busy state during execution");
        Thread checkBusyThread = cloudPortalOperator.createBusyCheckerThread(datacenterId, cloudRestTimeout, "vapps", vappName, 10, 1000);
        checkBusyThread.start();

        if (makeVappBusy) {
            setTestStep("Make the Vapp busy");
            CloudResponseObject busyResponse = new CloudResponseObject(cloudPortalOperator.makeVappsBusy(vAppId, cloudRestTimeout,
                    Boolean.toString(makeVappBusy)));
            assertEquals(busyResponse.getResponseCode(), expectedHttpStatus);
        }

        setTestStep("Run the rest call to recompose the vApp");
        CloudResponseObject recomposeResponse = new CloudResponseObject(cloudPortalOperator.recomposeVapp(vAppId, jsonBody.toString(), timeout));
        assertEquals(recomposeResponse.getResponseCode(),expectedHttpStatusRecompose);
        assertEquals(recomposeResponse.getHttpResponse().getBody().toString().contains(expectedOut),expected);

        if (makeVappBusy) {
            setTestStep("Unset the Vapp busy");
            CloudResponseObject busyResponse = new CloudResponseObject(cloudPortalOperator.makeVappsBusy(vAppId, cloudRestTimeout, "false"));
            assertEquals(busyResponse.getResponseCode(), expectedHttpStatus);
        }

        setTestStep("Verify the busy state of the vapp during execution");
        boolean vappBecameBusy = !checkBusyThread.isAlive();
        assertEquals(expectedvAppBecameBusy, vappBecameBusy);

        setTestStep("Verify the busy state of the vapp after execution");
        String vAppBusyStatus = listVappsResponse.getValueBySibling("vapps", "name", vappName, "busy");
        assertEquals("0", vAppBusyStatus);

        if (!vms.toString().contains("NULL")) {
            for (String vmName : vms) {
                CloudResponseObject listVmsvAppResponse = new CloudResponseObject(cloudPortalOperator.listVmsInVapp(vAppId, cloudRestTimeout));
                assertEquals(expectedHttpStatus, listVmsvAppResponse.getResponseCode());
                String vmvAppId = listVmsvAppResponse.getValueBySibling("vms", "name", vmName, "vm_id");
                assertEquals(expectedInList, listOfVMsvApp.contains(vmvAppId));
                CloudResponseObject listVmsvAppOrderResponse = new CloudResponseObject(cloudPortalOperator.listVmsStartupSettingsInVapp(vAppId,
                        cloudRestTimeout));
                assertEquals(expectedHttpStatus, listVmsvAppOrderResponse.getResponseCode());
                String vmOrder = listVmsvAppOrderResponse.getValueBySibling("vm", "name", vmName, "order");
                assertEquals(true, listOfVMsvAppOrder.contains(vmOrder));
            }
        }

        setTestStep("Verify the vApp power status of the vapp after execution");
        CloudResponseObject listVappsResponse3 = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
        String powerStatus2 = listVappsResponse3.getValueBySibling("vapps", "name", vappName, "status");
        assertEquals(expectedvAppPowerStatus, powerStatus2.toString());

        setTestStep("Run the rest call to get Events");
        HttpResponse eventsResponse2 = cloudPortalOperator.eventsSPP(events, timeout);
        assertEquals(expectedHttpStatus, eventsResponse2.getResponseCode().toString());

        int count2 = GenericCloudFunctions.eventCount(eventsResponse2.getBody().toString());
        if (expectedInList) {
            assertEquals(count1, count2);
        } else {
            assertNotEquals(count1, count2);
        }

        setTestStep("Run the rest call to list the vms Macs in the vapp after");
        CloudResponseObject listVmsvAppMacResponse2 = new CloudResponseObject(cloudPortalOperator.listVmsNetworkDetailsInVapp(vAppId, 100));
        assertEquals(expectedHttpStatus, listVmsvAppMacResponse2.getResponseCode());
        assertEquals(listVmsvAppMacResponse1.getHttpResponse().getBody().toString(), listVmsvAppMacResponse2.getHttpResponse().getBody().toString());
    }
}
