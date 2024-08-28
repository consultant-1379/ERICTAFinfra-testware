package com.ericsson.sut.test.operators;

import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.tools.http.HttpTool;
import com.ericsson.cifwk.taf.tools.http.HttpToolBuilder;
import com.ericsson.cifwk.taf.tools.http.constants.ContentType;
import com.ericsson.cifwk.utils.cloud.CloudResponseObject;

/**
 * CloudPortalRESTOperator is a Cloud Portal TAF Operator class, which allows
 * users to perform various actions using Cloud Portal REST calls.
 *
 * @see CloudPortalOperator
 */
@Operator(context = Context.REST)
@Singleton
public class CloudPortalRESTOperator implements CloudPortalOperator {

    private Host host;

    /**
     * CloudPortalRESTOperator is the constructor which instantiates a host
     * object from data in the host.properties:
     */
    public CloudPortalRESTOperator() {
        host = DataHandler.getHostByName("cloudportal");
    }

    @Override
    public HttpResponse deleteVapp(String vapp_id, int timeout) {

        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/Vapps/destroy_vapp_api/vapp_id:" + vapp_id
                + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).post(urlToGet);
        return response;
    }

    @Override
    public HttpResponse createVapp(String vapp_template_id, String vapp_name, String datacenter_Name, String power_On, String linked_Clone, int timeout) {

        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/VappTemplates/deploy_api/vapp_template_id:"
                + vapp_template_id + "/new_vapp_name:" + vapp_name
                + "/datacenter:" + datacenter_Name + "/poweron:" + power_On
                + "/linked_clone:" + linked_Clone + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).post(urlToGet);
        return response;
    }

    @Override
    public HttpResponse testVmPower(String vmName, String vmId, boolean poweron, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String powerString = "";
        if (poweron) {
            powerString = "poweron_api";
        } else {
            powerString = "poweroff_api";
        }

        String urlToGet = "/Vms/" + powerString + "/vm_name:" + vmName + "/vm_id:" + vmId + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse listVappTemplatesInCatalog(String catalogName, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/VappTemplates/index_api/catalog_name:"
                + catalogName + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse listVappsInDatacenter(String datacenterId, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/Vapps/index_api/orgvdc_id:" + datacenterId
                + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse addVappToCatalog(String vappId, String destinationCatalogName, String newVappTemplateName, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/Vapps/add_to_catalog_api/vapp_id:" + vappId
                + "/dest_catalog_name:" + destinationCatalogName
                + "/new_vapp_template_name:" + newVappTemplateName + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse listOrgVdcs(int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/OrgVdcs/index_api/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse listVmsInVapp(String vappId, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/Vms/vapp_index_api/vapp_id:" + vappId + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse deleteVappTemplate(String vappTemplateId, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/VappTemplates/delete_api/vapp_template_id:"
                + vappTemplateId + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse stopVapp(String vappId, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/Vapps/stop_vapp_api/vapp_id:" + vappId + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse securityPolicySet(String vappId, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/Vms/is_security_policy_set_api/vapp_id:" + vappId + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).get(urlToGet);
        return response;
    }

    @Override
    public void startBrowser(String host, String location, String packageName) {
        // TODO Auto-generated method stub

    }

    @Override
    public String checkActionsOptions(String username, String password, String requirementsArea, String dataCenterId, long timeOut) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String updateDataCenter(String username, String password, String requirementsArea, String dataCenterId, long timeOut) {
        // TODO Auto-generated method stub
        return null;
    }

    public Thread createBusyCheckerThread (final String parentId, final int cloudRestTimeout, final String xmlObjectName, final String objectName, final int retryAttempts, final int retryTimeout)
    {
        return new Thread() {
            public void run() {
                CloudResponseObject listObjectsResponse;
                for (int x=0;x<retryAttempts;x++)
                {
                    if (xmlObjectName.equals("vms"))
                    {
                        listObjectsResponse = new CloudResponseObject(listVmsInVapp(parentId, cloudRestTimeout));
                    } else {
                        listObjectsResponse = new CloudResponseObject(listVappsInDatacenter(parentId, cloudRestTimeout));
                    }
                    String objectBusyStatus = listObjectsResponse.getValueBySibling(xmlObjectName, "name", objectName, "busy");
                    if (objectBusyStatus!=null && objectBusyStatus.equals("1"))
                    {
                        // Finish the thread execution if we see the vm/vapp has become busy
                        return;
                    }
                    try {
                        Thread.sleep(retryTimeout);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Put the thread into waiting state, so that later we know the vm/vapp didn't become busy
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public HttpResponse testVmResize(String baseUrl, String parmeter, String value, String hotAdd, String vmName, String vmId, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host)
                .useHttpsIfProvided(false)
                .trustSslCertificates(true)
                .build();
        String urlToGet = "/Vms/" + baseUrl + "/" + parmeter + ":" + value
                + "/hot_add:" + hotAdd + "/vm_name:" + vmName + "/vm_id:"
                + vmId + "/.xml";
        final HttpResponse response = tool.request()
                .authenticate(host.getUser(), host.getPass())
                .timeout(timeout)
                .get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse testVmDelete(String vmName, String vmId, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host)
                .useHttpsIfProvided(false)
                .trustSslCertificates(true)
                .build();
        String urlToGet = "/Vms/delete_internal_api/vm_name:" + vmName
                + "/vm_id:" + vmId + "/.xml";
        final HttpResponse response = tool.request()
                .authenticate(host.getUser(), host.getPass())
                .timeout(timeout)
                .get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse poweroffAndDeleteVapp(String vappId, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/Vapps/destroy_vapp_api/vapp_id:" + vappId + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse makeVappsBusy(String vAppId, int busyTimeout, String busyStatus) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();
        String urlToGet = "/Vapps/make_vapp_busy_api/vapp_id:" + vAppId + "/busy_status:" + busyStatus + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(busyTimeout).get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse recomposeVapp(String vAppId, String jsonBody, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToPost = "/Vapps/recompose_vapp_api/vapp_id:" + vAppId + "/.xml";
        final HttpResponse response = tool.request()
                .header("Accept", "application/json")
                .contentType(ContentType.APPLICATION_JSON)
                .body(jsonBody)
                .authenticate(host.getUser(), host.getPass())
                .timeout(timeout)
                .post(urlToPost);
        return response;
    }

    @Override
    public HttpResponse listVmsInVappTemplate(String vAppTempId, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/Vms/vapptemplate_index_api/vapp_template_id:" + vAppTempId + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse eventsSPP(String jsonBody, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToPost = "/events/index/.json";
        final HttpResponse response = tool.request()
                .header("Accept", "application/json")
                .contentType(ContentType.APPLICATION_JSON)
                .body(jsonBody)
                .authenticate(host.getUser(), host.getPass())
                .timeout(timeout)
                .post(urlToPost);
        return response;

    }

    @Override
    public HttpResponse listVmsNetworkDetailsInVapp(String vAppId, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/Vms/vapp_network_api/vapp_id:" + vAppId + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse listVmsNetworkDetailsInVappTemplate(String vAppTempId, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/Vms/vapptemplate_network_api/vapp_template_id:" + vAppTempId + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse listVmsStartupSettingsInVapp(String vAppId, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/Vms/vapp_startup_settings_api/vapp_id:" + vAppId + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse listVmsStartupSettingsInVappTemplate(String vAppTempId, int timeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/Vms/vapptemplate_startup_settings_api/vapp_template_id:" + vAppTempId + "/.xml";
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(timeout).get(urlToGet);
        return response;
    }

    @Override
    public List<Map<String, String>> listVmsInVapp(String datacenterName, String vappName, int cloudRestTimeout)
    {
        CloudResponseObject listDatacentersResponse = new CloudResponseObject(listOrgVdcs(cloudRestTimeout));
        String datacenterId = listDatacentersResponse.getValueBySibling("OrgVdc", "name", datacenterName, "vcd_id");
        CloudResponseObject listVappsResponse = new CloudResponseObject(listVappsInDatacenter(datacenterId, cloudRestTimeout));
        final String vappId = listVappsResponse.getValueBySibling("vapps", "name", vappName, "vapp_id");
        CloudResponseObject listVmsResponse = new CloudResponseObject(listVmsInVapp(vappId, cloudRestTimeout));
        return listVmsResponse.getSimpleValues("vms");
    }

    @Override
    public HttpResponse renameVappTemplate(String vAppTempId,
                                           String newVappName,
                                           int cloudRestTimeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();

        String urlToGet = "/VappTemplates/rename_api/vapp_template_id:" + vAppTempId + "/vapp_template_new_name:" + newVappName + "/.xml" ;
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(cloudRestTimeout).get(urlToGet);
        return response;
    }

    @Override
    public HttpResponse getVappMetadata(String vAppId,
                                              int cloudRestTimeout) {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();
        String urlToGet = "/Vms/vapp_metadata_api/vapp_id:" + vAppId + "/.xml" ;
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(cloudRestTimeout).get(urlToGet);
        return response;
    }

    @Override
    public String getLatestTemplateForVapp(String vAppId,
                                           int cloudRestTimeout) {
        HttpResponse metadataResponse = getVappMetadata(vAppId, cloudRestTimeout) ;
        CloudResponseObject metadataResponseObj = new CloudResponseObject(metadataResponse);
        String vappTemplateId = null ;
        try{
            vappTemplateId = metadataResponseObj.getSimpleValues("vapp.latestTemplate.name").get(0).get("#text") ;
        }
        catch(Exception e){
            System.out.println("No Metadata for latest vappTemplate found");
            e.printStackTrace();
        }

        return vappTemplateId;
    }

    @Override
    public String getVappIdFromName(String vAppName,
                                    String datacenterId,
                                    int cloudRestTimeout) {
        CloudResponseObject listVappsResponse = new CloudResponseObject(listVappsInDatacenter(datacenterId, cloudRestTimeout));
        final String vappId = listVappsResponse.getValueBySibling("vapps", "name", vAppName, "vapp_id");
        return vappId;
    }

    @Override
    public HttpResponse generateReport(String report, String format, int reportTimeout)
    {
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();
        String urlToGet = "/Reports/" + report + "." + format;
        final HttpResponse response = tool.request().authenticate(host.getUser(), host.getPass()).timeout(reportTimeout).get(urlToGet);
        return response;
    }
}
