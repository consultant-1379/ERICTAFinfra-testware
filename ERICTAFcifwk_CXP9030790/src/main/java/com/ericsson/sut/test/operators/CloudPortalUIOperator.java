package com.ericsson.sut.test.operators;

import java.util.List;
import java.util.Map;

import com.ericsson.cifwk.taf.UiOperator;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.ui.*;
import com.ericsson.cloudPortal.ui.test.pages.UpdateCloudPortalDataCenterData;

@Operator(context = Context.UI)
public class CloudPortalUIOperator implements CloudPortalOperator, UiOperator {
    private BrowserTab browserTab;
    private UpdateCloudPortalDataCenterData updateSPPDataCenterData;
    private GenericUIOperator genericUIOperator;
    private String result;

    public void startBrowser(String hostName, String location, String packageName){
        genericUIOperator = new GenericUIOperator();
        browserTab = genericUIOperator.startBrowser(hostName, location, packageName);
    }

    public String checkActionsOptions(String username, String password, String requirementsArea, String dataCenterId, long timeOut) {
        updateSPPDataCenterData = browserTab.getView(UpdateCloudPortalDataCenterData.class);
        result = updateSPPDataCenterData.checkActionsOptions(browserTab, username, password, requirementsArea, dataCenterId,timeOut);
        return result;
    }

    public String updateDataCenter(String username, String password, String requirementsArea, String dataCenterId, long timeOut) {
        updateSPPDataCenterData = browserTab.getView(UpdateCloudPortalDataCenterData.class);
        result = updateSPPDataCenterData.updateDataCenterUI(browserTab, username, password, requirementsArea, dataCenterId,timeOut);
        return result;
    }

    @Override
    public HttpResponse listOrgVdcs(int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse listVappsInDatacenter(String datacenterId, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse listVappTemplatesInCatalog(String catalogName, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse addVappToCatalog(String vapp_id, String destinationCatalogName, String newVappTemplateName, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse stopVapp(String vappId, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse deleteVappTemplate(String vappTemplateId, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse deleteVapp(String vapp_id, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse createVapp(String vapp_template_id, String vapp_name, String datacenter_Name, String power_On, String linked_Clone, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse listVmsInVapp(String vappId, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse testVmPower(String vmName, String vm_id, boolean poweron, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse securityPolicySet(String vappId, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Thread createBusyCheckerThread (final String parentId, final int cloudRestTimeout, final String xmlObjectName, final String objectName, final int retryAttempts, final int retryTimeout) {
        return null;
    }

    @Override
    public HttpResponse testVmResize(String baseUrl, String parameter,
            String value, String hotAdd, String vmName, String vmId,
            int powerTimeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse testVmDelete(String vmName, String vm_id, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse poweroffAndDeleteVapp(String thevAppId,
            int deleteTimeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse makeVappsBusy(String vAppId, int busyTimeout, String busyStatus) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse recomposeVapp(String vAppId, String jsonBody, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse listVmsInVappTemplate(String vAppTempId, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse eventsSPP(String jsonBody, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse listVmsNetworkDetailsInVapp(String vAppId, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse listVmsNetworkDetailsInVappTemplate(String vAppTempId, int timeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse listVmsStartupSettingsInVapp(String vAppId, int cloudRestTimeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse listVmsStartupSettingsInVappTemplate(String vAppTempId, int cloudRestTimeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Map<String, String>> listVmsInVapp(String datacenterName,
            String vappName, int cloudRestTimeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse renameVappTemplate(String vAppTempId,
                                           String newVappName,
                                           int cloudRestTimeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse getVappMetadata(String vAppId,
                                              int cloudRestTimeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getLatestTemplateForVapp(String vAppId,
                                           int cloudRestTimeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getVappIdFromName(String vAppName,
                                    String datacenterName,
                                    int cloudRestTimeout) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse generateReport(String report, String format, int reportTimeout) {
        // TODO Auto-generated method stub
        return null;
    }
}
