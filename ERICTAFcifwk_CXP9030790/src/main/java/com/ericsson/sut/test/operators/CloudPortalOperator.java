package com.ericsson.sut.test.operators;

import java.util.List;
import java.util.Map;

import com.ericsson.cifwk.taf.tools.http.HttpResponse;

/**
 * CloudPortalOperator is a CloudPortal TAF Operator interface.
 *
 */
public interface CloudPortalOperator {

    // Rest calls
    /**
     * This retrieves a list of datacenters
     *
     * @param timeout
     *            The timeout for the REST call
     * @return HttpResponse Object returned to test case.
     */
    HttpResponse listOrgVdcs(int timeout);

    /**
     * This lists the vapps in a given datacenter
     *
     * @param datacenterId
     *         The id of the datacenter to get the list from
     * @param timeout
     *            The timeout for the REST call
     * @return HttpResponse Object returned to test case.
     */
    HttpResponse listVappsInDatacenter(String datacenterId, int timeout);

    /**
     * This lists the vapps in a datacenter
     *
     * @param catalogName
     *            The catalog name to get the list from
     * @param timeout
     *            The timeout for the REST call
     * @return HttpResponse Object returned to test case.
     */
    HttpResponse listVappTemplatesInCatalog(String catalogName, int timeout);

    /**
     * This adds a vapp to a catalog
     *
     * @param vapp_id
     *            The id of the datacenter to get the list from
     * @param destinationCatalogName
     *            The name of the catalog to add the vapp to
     * @param newVappTemplateName
     *            The name to give to the new vapp template
     * @param timeout
     *            The timeout for the REST call
     * @return HttpResponse Object returned to test case.
     */
    HttpResponse addVappToCatalog(String vapp_id, String destinationCatalogName, String newVappTemplateName, int timeout);

    /**
     * This stops a vApp
     *
     * @param vappId
     *            The id of the vapp you wish to stop
     * @param timeout
     *            The timeout for the REST call
     * @return
     */
    HttpResponse stopVapp(String vappId, int timeout);

    /**
     * This deletes a vapp template
     *
     * @param vappTemplateId
     *            The id of the vapp template to delete
     * @param timeout
     *            The timeout for the REST call
     * @return HttpResponse Object returned to test case.
     */
    HttpResponse deleteVappTemplate(String vappTemplateId, int timeout);

    /**
     * Deletes a vApp
     *
     * @param vapp_id
     *            The id of the vApp that is to be deleted
     * @param timeout
     *            The timeout for the REST call
     * @return
     */
    HttpResponse deleteVapp(String vapp_id, int timeout);

    /**
     * Creates a vApp in a specified datacenter
     *
     * @param vapp_template_id
     *            The id of the template that the vApp is going to be created
     *            from
     * @param vapp_name
     *            The name of the new vApp
     * @param datacenter_Name
     *            The name of the datacenter that the vApp is being added to
     * @param power_On
     *            Specifies whether or not the vApp is to power on upon creation
     * @param linked_Clone
     *            Specifies whether or not a link and delta disk is to be
     *            created for the new vApp
     * @param timeout
     *            The timeout for the REST call
     * @return
     */
    HttpResponse createVapp(String vapp_template_id, String vapp_name, String datacenter_Name, String power_On, String linked_Clone, int timeout);

    /**
     * Powers on/off a specified vm in a vApp
     *
     * @param vmName
     *            The name of the vm to be powered on/off
     * @param vm_id
     *            Name id of the vm to be powered on/off
     * @param poweron
     *            Whether to power on the vm, otherwise it gets powered off
     * @param timeout
     *            The timeout for the REST call
     * @return
     */
    HttpResponse testVmPower(String vmName, String vm_id, boolean poweron, int timeout);

    /**
     * Lists all of the vms in a specified vApp
     *
     * @param vappId
     *            ID of the vApp
     * @param timeout
     *            the Timeout for the REST call
     * @return
     */
    HttpResponse listVmsInVapp(String vappId, int timeout);

    /**
     * Lists all of the vms in a specified vApp with their security policy settings
     *
     * @param vappId
     *            ID of the vApp
     * @param timeout
     *            the Timeout for the REST call
     * @return
     */
    HttpResponse securityPolicySet(String vappId, int timeout);

    /**
     * Starting the Browser for the UI Testcases
     * @param host
     * @param location
     * @param packageName
     */
    void startBrowser(String host, String location, String packageName);

    /**
     * For Checking Cloud Portal that edit and open options are available on ORGSVDC Page
     * @param username
     * @param password
     * @param type
     * @param product
     * @param pkg
     * @param rstate
     * @param email
     * @param drop
     * @param timeOut
     * @return
     */
    String checkActionsOptions(String username,
            String password,
            String requirementsArea,
            String dataCenterId,
            long timeOut);

    /**
     * For Updating the Cloud Portal Datacenter with RA
     * @param username
     * @param password
     * @param requirementsArea
     * @param dataCenterId
     * @param timeOut
     * @return
     */
    String updateDataCenter(String username,
            String password,
            String requirementsArea,
            String dataCenterId,
            long timeOut);

    /**
     * This function creates a thread that can be run in the background while
     * executing vapp / vm actions. Its to check did the vapp / vm go into a
     * busy state during certain testcases
     *
     * @param parentId
     *            The id of the parent, for vapp the parent is the datacenter,
     *            for vms, the parent is the vapp
     * @param cloudRestTimeout
     *            The timeout for the rest calls to check if its busy
     * @param xmlObjectName
     *            The xml tag used for the object in question, ie "vapps" or
     *            "vms"
     * @param objectName
     *            The name of the vapp / vm to search for
     * @param retryAttempts
     *            The number of times to check if its busy
     * @param retryTimeout
     *            The time between retries
     * @return
     */
    Thread createBusyCheckerThread(final String parentId,
            final int cloudRestTimeout, final String xmlObjectName,
            final String objectName, final int retryAttempts,
            final int retryTimeout);

    /**
     * Resize CPU of a specified vm in a vApp
     *
     * @param vmName
     *            The name of the vm
     * @param vm_id
     *            Name id of the vm
     * @param baseURL
     *            Base of REST ie resize_cpu_internal_api or
     *            resize_memory_internal_api
     * @param parameter
     *            The input name ie cpu_count or memory_mb
     * @param value
     *            The count of the CPU or Memory in MB
     * @param hotAdd
     *            The hot add yes or no
     * @param timeout
     *            The timeout for the REST call
     * @return
     */

    HttpResponse testVmResize(String baseUrl, String parameter, String value,
            String hotAdd, String vmName, String vmId, int powerTimeout);

    /**
     * Deletes a specified vm in a vApp
     *
     * @param vmName
     *            The name of the vm to be deleted
     * @param vmId
     *            Name id of the vm to be deleted
     * @param timeout
     *            The timeout for the REST call
     * @return
     */

    HttpResponse testVmDelete(String vmName, String vmId, int timeout);

    /**
     * Powers off and Deletes a specified vApp
     *
     * @param thevAppId
     *         Name id of the vapp to be deleted
     * @param timeout
     *         The timeout for the REST call
     * @return
     */
    HttpResponse poweroffAndDeleteVapp(String thevAppId, int deleteTimeout);

    /**
     * Makes a Vapp Busy or UnBusy
     *
     * @param vAppId
     *         Name id of the vapp to be deleted
     * @param busyTimeout
     *         The timeout for the REST call
     * @param busyStatus
     *         what we want to set the busy status as
     * @return
     */
    HttpResponse makeVappsBusy(String vAppId, int busyTimeout, String busyStatus);

    /**
     * Recompose a vApp
     *
     * @param vAppId
     *         Name id of the vapp to be recomposed
     * @param jsonBody
     *         The json for rest call
     * @param timeout
     *         The timeout for the REST call
     * @return
     */
    HttpResponse recomposeVapp(String vAppId, String jsonBody, int timeout);

    /**
     * Returns list of Vms in vApp template
     *
     * @param vAppTempId
     *         Name id of the vapp template
     * @param timeout
     *         The timeout for the REST call
     * @return
     */
    HttpResponse listVmsInVappTemplate(String vAppTempId, int timeout);

    /**
     * Returns list of Events
     *
     * @param jsonBody
     *         The json for rest call
     * @param timeout
     *         The timeout for the REST call
     * @return
     */
    HttpResponse eventsSPP(String jsonBody, int timeout);

    /**
     * Returns list of network details in vApp
     *
     * @param vAppId
     *            Name id of the vapp
     * @param timeout
     *            The timeout for the REST call
     * @return
     */
    HttpResponse listVmsNetworkDetailsInVapp(String vAppId, int timeout);

    /**
     * Returns list of network details in vAppTemplate
     *
     * @param vAppTempId
     *            Name id of the vapp Template
     * @param timeout
     *            The timeout for the REST call
     * @return
     */
    HttpResponse listVmsNetworkDetailsInVappTemplate(String vAppTempId, int timeout);

    /**
     * Returns list of Starting Order in vApp
     *
     * @param vAppId
     *            Name id of the vapp
     * @param timeout
     *            The timeout for the REST call
     * @return
     */

    HttpResponse listVmsStartupSettingsInVapp(String vAppId, int cloudRestTimeout);

    /**
     * Returns list of Starting Order in vApp Template
     *
     * @param vAppId
     *            Name id of the vapp Template
     * @param timeout
     *            The timeout for the REST call
     * @return
     */

    HttpResponse listVmsStartupSettingsInVappTemplate(String vAppTempId, int cloudRestTimeout);

    /**
     * Returns list of Starting Order in vApp Template
     *
     * @param vAppId
     *            Name id of the vapp Template
     * @param newVappName
     *            the name to rename vapp template
     * @param cloudRestTimeout
     *            The timeout for the REST call
     * @return
     */

    HttpResponse renameVappTemplate(String vAppTempId, String newVappName,int cloudRestTimeout);

    /**
     * Returns metadata of a Vapp
     *
     * @param vAppId
     *            Name id of the vapp Template
     * @param cloudRestTimeout
     *            The timeout for the REST call
     * @return
     */

    HttpResponse getVappMetadata(String vAppId,int cloudRestTimeout);

    /**
     * Returns latest template used for vapp
     *
     * @param vAppId
     *            Name id of the vapp Template
     * @param cloudRestTimeout
     *            The timeout for the REST call
     * @return
     */
    String getLatestTemplateForVapp(String vAppId, int cloudRestTimeout);

    /**
     * Returns list of vms in a vapp as a hash map of name value pairs for each element in each vms fields
     *
     * @param datacenterName
     *            Name of the datacenter to look for the vApp
     * @param vappName
     *            Name of the vApp
     * @param cloudRestTimeout
     *            The timeout for the REST call
     * @return
     */
    List<Map<String, String>> listVmsInVapp(String datacenterName,
            String vappName, int cloudRestTimeout);

    /**
     * Returns vapp id
     *
     * @param vAppName
     *            Name of the vapp
     * @param datacenterId
     *            ID of the datacenter to look for the vApp
     * @param cloudRestTimeout
     *            The timeout for the REST call
     * @return
     */
    String getVappIdFromName(String vAppName,String datacenterId, int cloudRestTimeout);

    /**
     * Returns one of the cloud portal reports, depending on what report url and format is given
     * 
     * @param report
     *          The report to be generated, eg vapp_report
     * @param format
     *          The format to have the report generate in, eg xml, json, csv
     * @param reportTimeout
     *          The timeout for the REST call
     * @return
     */
    HttpResponse generateReport(String report, String format, int reportTimeout);
}
