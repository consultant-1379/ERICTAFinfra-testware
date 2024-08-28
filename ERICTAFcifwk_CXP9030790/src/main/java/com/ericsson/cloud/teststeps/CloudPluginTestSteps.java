package com.ericsson.cloud.teststeps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.ericsson.cifwk.taf.TestContext;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestStep;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.Ports;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.utils.XMLHandler;
import com.ericsson.cifwk.utils.jenkinsSlaveHandler;
import com.ericsson.cifwk.utils.cloud.CloudResponseObject;
import com.ericsson.sut.test.operators.CloudPluginRestOperator;
import com.ericsson.sut.test.operators.CloudPortalCliOperator;
import com.ericsson.sut.test.operators.CloudPortalRESTOperator;
import com.ericsson.sut.test.operators.GenericRESTOperatorHttp;
import com.ericsson.sut.test.operators.JenkinsRESTOperator;
import com.google.inject.Inject;

public class CloudPluginTestSteps extends TorTestCaseHelper {

    ArrayList<String> jenkinsSlavesManual = new ArrayList<String>();
    ArrayList<String> jenkinsSlavesPostDelete = new ArrayList<String>();
    ArrayList<String> jenkinsSlavesRefresh = new ArrayList<String>();
    ArrayList<String> jenkinsSlavesCatalog = new ArrayList<String>();

    private HttpResponse response;
    private int slaveListSize = 0;
    private String datacenterId;
    private ArrayList<String> deletedJenkinsSlavesManual = new ArrayList<String>();
    private ArrayList<String> deletedJenkinsSlavesPostDelete = new ArrayList<String>();
    private ArrayList<String> deletedJenkinsSlavesRefresh = new ArrayList<String>();
    private ArrayList<String> deletedJenkinsSlavesCatalog = new ArrayList<String>();

    Logger logger = Logger.getLogger(JenkinsRESTOperator.class);

    @Inject
    JenkinsRESTOperator jenkinsOperator;

    @Inject
    CloudPortalCliOperator cloudPortalCLIOperator;

    @Inject
    CloudPluginRestOperator cloudPluginOperator;

    @Inject
    CloudPortalRESTOperator cloudPortalOperator;

    @Inject
    GenericRESTOperatorHttp genericRestOperator;

    @Inject
    TestContext context;

    public static final String Put_Slaves_Online = "CIP-7213_Func_1.2";
    public static final String Get_Jenkins_Slaves = "CIP-7213_Func_1.3";
    public static final String Get_DataCenter_ID = "CIP-7213_Func_1.4";
    public static final String Manual_Delete_Slaves_Portal = "CIP-7213_Func_1.5";
    public static final String Disable_Vapp_Destroy = "CIP-7213_Func_1.6";
    public static final String Disable_Cloud_Portal = "CIP-7213_Func_1.7";
    public static final String Manual_Delete_Slaves_Jenkins = "CIP-7213_Func_1.8";
    public static final String Check_Slaves_Gone_Jenkins = "CIP-7213_Func_1.9";
    public static final String Check_Slaves_Gone_Cloud = "CIP-7213_Func_1.10";
    public static final String Wait_Slaves_Come_Online = "CIP-7213_Func_1.11";
    public static final String Build_Job = "CIP-7213_Func_1.12";
    public static final String Wait_For_Job_To_Finish = "CIP-7213_Func_1.13";
    public static final String Get_Slave_From_Build_Log = "CIP-7213_Func_1.14";
    public static final String Put_Portal_Fully_Online = "CIP-7213_Func_1.15";
    public static final String Add_To_Catalog_PreStep = "CIP-7213_Func_1.16";
    public static final String Wait_For_Job_To_Finish_PreStep = "CIP-7213_Func_1.17";
    public static final String Put_Cloud_Portal_Online = "CIP-7213_Func_1.18";
    public static final String Wait_For_Build_Job_To_Finish = "CIP-7213_Func_1.19";
    public static final String Enable_Vapp_Destroy = "CIP-7213_Func_1.20";
    public static final String Enable_Catalog_Destroy = "CIP-7213_Func_1.21";
    public static final String Post_Build_Check = "CIP-7213_Func_1.22";
    public static final String Get_Slave_Name = "CIP-7213_Func_1.23";
    public static final String Disable_Add_To_Catalog = "CIP-7213_Func_1.24";
    public static final String Wait_Slaves_Come_Online_PreStep = "CIP-7213_Func_1.25";
    public static final String Disable_Vapp_Recompose = "CIP-7894_Func_1.1";
    public static final String Check_Build_Job_Slave_Online = "CIP-7894_Func_1.2";
    public static final String Disable_Vapptemplate_List_Vms = "CIP-7894_Func_1.3";
    public static final String Remember_Vms_Before_Recompose = "CIP-7894_Func_1.4";
    public static final String Vms_Were_Recomposed = "CIP-7894_Func_1.5";
    public static final String Update_Old_vAppTemplate_Name = "CIP-8158_Func_1.1";
    public static final String Disconnect_Slaves_Of_Label = "CIP-8204_Func_1.1";
    public static final String Place_Health_Check_Script = "CIP-8204_Func_1.2";
    public static final String Put_Slaves_Online_Check = "CIP-8204_Func_1.3";

    @TestStep(id = Put_Slaves_Online_Check)
    public void putSlavesOnline(@Input("jenkinsHostName") String jenkinsHostName,
                                @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                @Input("jenkinsSlaveLabel") String jenkinsSlaveLabel,
                                @Input("slaveOnlineTimeout") int slaveOnlineTimeout,
                                @Input("jenkinsRestTimeout") int jenkinsRestTimeout,
                                @Output("expectedSlavesOnline") boolean expectedSlavesOnline) {
        ArrayList<String> slaves = jenkinsOperator.getAllJenkinsSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout);
        jenkinsOperator.onlineSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsRestTimeout, slaves);
        boolean allSlavesOnline = jenkinsOperator.waitForSlavesToComeOnline(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout, slaveOnlineTimeout, slaves);
        assertEquals(allSlavesOnline, expectedSlavesOnline);
    }

    @TestStep(id = Put_Slaves_Online)
    public void putSlavesOnline(@Input("jenkinsHostName") String jenkinsHostName,
                                @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                @Input("jenkinsSlaveLabel") String jenkinsSlaveLabel,
                                @Input("jenkinsRestTimeout") int jenkinsRestTimeout) {
        ArrayList<String> slaves = jenkinsOperator.getAllJenkinsSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout);
        jenkinsOperator.onlineSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsRestTimeout, slaves);
        boolean allSlavesOnline = jenkinsOperator.waitForSlavesToComeOnline(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout, 10000, slaves);
        assertEquals(allSlavesOnline, true);
    }

    @TestStep(id = Get_Jenkins_Slaves)
    public void getJenkinsSlaves(@Input("jenkinsHostName") String jenkinsHostName,
                                 @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                 @Input("jenkinsSlaveLabel") String jenkinsSlaveLabel,
                                 @Input("jenkinsRestTimeout") int jenkinsRestTimeout,
                                 @Input("slaveList") String slaveList) {
        if (slaveList.equals("manual")) {
            jenkinsSlavesManual = jenkinsOperator.getAllJenkinsSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout);
            jenkinsSlaveHandler.setCurrentJenkinsSlaves(jenkinsSlavesManual);
        }
        if (slaveList.equals("PostRefresh")) {
            jenkinsSlavesRefresh = jenkinsOperator.getAllJenkinsSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout);
            context.setAttribute("jenkinsSlavesRefreshed", jenkinsSlavesRefresh);
            jenkinsSlaveHandler.setCurrentJenkinsSlaves(jenkinsSlavesRefresh);
        }
    }

    @TestStep(id = Get_DataCenter_ID)
    public void getDataCenterID(@Input("cloudRestTimeout") int cloudRestTimeout,
                                @Input("datacenterName") String datacenterName,
                                @Output("httpResponseDatacenter") String httpResponseDatacenter,
                                @Output("expectedDatacenter") String expectedDatacenter) {
        CloudResponseObject listDatacentersResponse = new CloudResponseObject(cloudPortalOperator.listOrgVdcs(cloudRestTimeout));
        assertEquals(listDatacentersResponse.getResponseCode(), httpResponseDatacenter);
        assertTrue(listDatacentersResponse.doesTagExist(expectedDatacenter));

        datacenterId = listDatacentersResponse.getValueBySibling("OrgVdc", "name", datacenterName, "vcd_id");
        assertTrue(datacenterId != null);
    }

    @TestStep(id = Manual_Delete_Slaves_Portal)
    public void manualDeleteSlavesPortal(@Input("manualDeleteCloud") boolean manualDeleteCloud,
                                         @Input("numberOfSlavesToDelete") int numberOfSlavesToDelete,
                                         @Input("cloudRestTimeout") int cloudRestTimeout,
                                         @Input("manualDeleteTimeout") int manualDeleteTimeout) {
        slaveListSize = jenkinsSlavesManual.size();
        if (manualDeleteCloud) {
            if (slaveListSize >= 1 && numberOfSlavesToDelete >= 1) {
                for (int i = 0; i < numberOfSlavesToDelete; i++) {
                    if (i < slaveListSize) {
                        CloudResponseObject listVappsResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
                        assertEquals("OK", listVappsResponse.getResponseCode());
                        String vappName = jenkinsSlavesManual.get(i);
                        setTestStep("Simulate vapp not existing in cloud portal");
                        String thevAppId = listVappsResponse.getValueBySibling("vapps", "name", vappName, "vapp_id");
                        CloudResponseObject deletionResponse = new CloudResponseObject(cloudPortalOperator.deleteVapp(thevAppId, manualDeleteTimeout));
                        assertEquals("OK", deletionResponse.getResponseCode());
                        listVappsResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
                        assertEquals("OK", listVappsResponse.getResponseCode());
                        String vappStatus = listVappsResponse.getValueBySibling("vapps", "name", vappName, "vapp_id");
                        assertEquals(vappStatus, null);
                    }
                }
            }
        }
    }

    @TestStep(id = Disable_Vapp_Destroy)
    public void disableVappDestroy(@Input("cloudDeleteSucceeds") boolean cloudDeleteSucceeds) {
        if (cloudDeleteSucceeds) {
            assertEquals(cloudPortalCLIOperator.enableVappDestroyApi(), 0);
        } else {
            assertEquals(cloudPortalCLIOperator.disableVappDestroyApi(), 0);
        }
    }

    @TestStep(id = Disable_Vapp_Recompose)
    public void disableVappRecompose(@Input("cloudRecomposeSucceeds") boolean cloudRecomposeSucceeds) {
        if (cloudRecomposeSucceeds) {
            assertEquals(cloudPortalCLIOperator.enableVappRecomposeApi(), 0);
        } else {
            assertEquals(cloudPortalCLIOperator.disableVappRecomposeApi(), 0);
        }
    }

    @TestStep(id = Disable_Vapptemplate_List_Vms)
    public void disableVapptemplateListVms(@Input("cloudVapptemplateListVmsSucceeds") boolean cloudVapptemplateListVmsSucceeds) {
        if (cloudVapptemplateListVmsSucceeds) {
            assertEquals(cloudPortalCLIOperator.enableVapptemplateListVmsApi(), 0);
        } else {
            assertEquals(cloudPortalCLIOperator.disableVapptemplateListVmsApi(), 0);
        }
    }

    @TestStep(id = Remember_Vms_Before_Recompose)
    public void rememberVmsBeforeRecompose(@Input("jenkinsHostName") String jenkinsHostName,
            @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
            @Input("jenkinsSlaveLabel") String jenkinsSlaveLabel,
            @Input("jenkinsRestTimeout") int jenkinsRestTimeout,
            @Input("datacenterName") String datacenterName,
            @Input("slaveList") String slaveList,
            @Input("cloudRestTimeout") int cloudRestTimeout) {

        ArrayList<String> jenkinsSlaves = jenkinsOperator.getAllJenkinsSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout);
        String mainSlave = jenkinsSlaves.get(0);
        if(slaveList.equals("postRecompose")){
            context.setAttribute("vmsBeforeRecompose", cloudPortalOperator.listVmsInVapp(datacenterName, mainSlave, cloudRestTimeout));
        }
        if(slaveList.equals("bulkRecompose")){
            context.setAttribute("vmsBeforeRecomposeBulk", cloudPortalOperator.listVmsInVapp(datacenterName, mainSlave, cloudRestTimeout));
        }

    }

    @TestStep(id = Vms_Were_Recomposed)
    public void vmsWereRecomposed(@Input("vmsToRecompose") String [] vmsToRecompose,
            @Input("jenkinsHostName") String jenkinsHostName,
            @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
            @Input("jenkinsSlaveLabel") String jenkinsSlaveLabel,
            @Input("jenkinsRestTimeout") int jenkinsRestTimeout,
            @Input("datacenterName") String datacenterName,
            @Input("slaveList") String slaveList,
            @Input("cloudRestTimeout") int cloudRestTimeout) {

        ArrayList<Map<String,String>> vmsBeforeRecompose = new ArrayList<Map<String,String>>() ;

        if(slaveList.equals("postRecompose")){
            vmsBeforeRecompose = context.getAttribute("vmsBeforeRecompose");
        }
        if(slaveList.equals("bulkRecompose")){
            vmsBeforeRecompose = context.getAttribute("vmsBeforeRecomposeBulk");
        }

        ArrayList<String> jenkinsSlaves = jenkinsOperator.getAllJenkinsSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout);
        String mainSlave = jenkinsSlaves.get(0);
        List<Map<String,String>> vmsAfterRecompose = cloudPortalOperator.listVmsInVapp(datacenterName, mainSlave, cloudRestTimeout);

        int vmsBeforeRecomposeSize = vmsBeforeRecompose.size();
        int vmsAfterRecomposeSize = vmsAfterRecompose.size();

        int vmsRecomposed = 0;
        for (int x = 0; x < vmsBeforeRecomposeSize; x++)
        {
            for (int y = 0; y < vmsAfterRecomposeSize; y++)
            {
                if (vmsBeforeRecompose.get(x).get("name").equals(vmsAfterRecompose.get(y).get("name")))
                {
                    boolean shouldBeRecomposed = false;
                    for (int v = 0; v < vmsToRecompose.length; v++)
                    {
                        if (vmsToRecompose[v].equals(vmsBeforeRecompose.get(x).get("name")))
                        {
                            shouldBeRecomposed = true;
                            break;
                        }
                    }
                    if (shouldBeRecomposed)
                    {
                        vmsRecomposed++;
                        assertNotEquals(vmsBeforeRecompose.get(x).get("vm_id"), vmsAfterRecompose.get(y).get("vm_id"));
                    }
                    else {
                        assertEquals(vmsBeforeRecompose.get(x).get("vm_id"), vmsAfterRecompose.get(y).get("vm_id"));
                    }
                    break;
                }
            }
        }
        assertEquals(vmsRecomposed, vmsToRecompose.length);
    }

    @TestStep(id = Disable_Cloud_Portal)
    public void disableCloudPortal(@Input("cloudAlive") boolean cloudAlive) {
        cloudPortalCLIOperator = new CloudPortalCliOperator();
        if (cloudAlive) {
            assertEquals(cloudPortalCLIOperator.startWebInterface(), 0);
        } else {
            assertEquals(cloudPortalCLIOperator.stopWebInterface(), 0);
        }
    }

    @TestStep(id = Disable_Add_To_Catalog)
    public void disableAddToCatalog(@Input("addToCatalogSucceeds") boolean addToCatalogSucceeds) {
        if (addToCatalogSucceeds) {
            assertEquals(cloudPortalCLIOperator.enableVappAddToCatalogApi(), 0);
        } else {
            assertEquals(cloudPortalCLIOperator.disableVappAddToCatalogApi(), 0);
        }
    }

    @TestStep(id = Manual_Delete_Slaves_Jenkins)
    public void manualDeleteSlavesJenkins(@Input("numberOfSlavesToDelete") int numberOfSlavesToDelete,
                                          @Input("jenkinsHostName") String jenkinsHostName,
                                          @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                          @Input("jenkinsRestTimeout") int jenkinsRestTimeout,
                                          @Output("ExpectedDeleteResponse") String expectedDeleteResponse) {
        deletedJenkinsSlavesManual.clear();
        if (slaveListSize >= 1 && numberOfSlavesToDelete >= 1) {
            for (int i = 0; i < numberOfSlavesToDelete; i++) {
                if (i < slaveListSize) {
                    String slave = jenkinsSlavesManual.get(i);
                    response = cloudPluginOperator.deleteJenkinsSlave(jenkinsHostName, jenkinsBaseDirectory, slave, jenkinsRestTimeout);
                    assertEquals(expectedDeleteResponse, response.getResponseCode().toString());
                    deletedJenkinsSlavesManual.add(slave);
                }
            }
        }
    }

    @TestStep(id = Disconnect_Slaves_Of_Label)
    public void disconnectSlaves(@Input("jenkinsHostName") String jenkinsHostName,
                                @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                @Input("jenkinsSlaveLabel") String jenkinsSlaveLabel,
                                @Input("jenkinsRestTimeout") int jenkinsRestTimeout) {

        ArrayList<String> slaves = jenkinsOperator.getAllJenkinsSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout);
        for (String slave : slaves) {
            cloudPluginOperator.disconnectJenkinsSlave(jenkinsHostName, jenkinsBaseDirectory, slave, jenkinsRestTimeout);
        }
    }

    @TestStep(id = Place_Health_Check_Script)
    public void placeHealthCheckScript(@Input("localHealthCheckFile") String localHealthCheckFile,
                                @Input("remoteHealthCheckFileLocation") String remoteHealthCheckFileLocation) {
        if (!cloudPortalCLIOperator.isLiveCloudCheck())
        {
            if (remoteHealthCheckFileLocation != null)
            {
                cloudPortalCLIOperator.removeRemoteFileOnHost(remoteHealthCheckFileLocation);
            }
            if (localHealthCheckFile != null && remoteHealthCheckFileLocation != null) {
                cloudPortalCLIOperator.addFileToRemoteHost(localHealthCheckFile, remoteHealthCheckFileLocation);
            }
        }
    }

    @TestStep(id = Check_Slaves_Gone_Jenkins)
    public void checkSlavesGoneJenkins(@Input("jenkinsHostName") String jenkinsHostName,
                                       @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                       @Input("jenkinsSlaveLabel") String jenkinsSlaveLabel,
                                       @Input("jenkinsRestTimeout") int jenkinsRestTimeout,
                                       @Input("slaveDeleteTimeout") int slaveDeleteTimeout,
                                       @Input("slaveList") String slaveList,
                                       @Output("expectedSlaveExistsJenkins") boolean expectedSlaveExistsJenkins) {
        ArrayList<String> deletedJenkinsSlaves = new ArrayList<String>();
        if (slaveList.equals("manual")) {
            deletedJenkinsSlaves = new ArrayList<String>(deletedJenkinsSlavesManual);
        }
        if (slaveList.equals("PostBuild")) {
            deletedJenkinsSlaves = new ArrayList<String>(deletedJenkinsSlavesPostDelete);
        }
        if (slaveList.equals("PostRefresh")) {
            deletedJenkinsSlaves = context.getAttribute("jenkinsSlavesRefreshed") ;
        }
        if (slaveList.equals("PostCatalog")) {
            deletedJenkinsSlaves = new ArrayList<String>(deletedJenkinsSlavesCatalog);

        }

        boolean slavesStillExistsJenkins = jenkinsOperator.anySlaveStillExistsAfterTime(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout, slaveDeleteTimeout,
                deletedJenkinsSlaves);
        assertEquals(slavesStillExistsJenkins, expectedSlaveExistsJenkins);
    }

    @TestStep(id = Check_Slaves_Gone_Cloud)
    public void checkSlavesGoneCloud(@Input("cloudRestTimeout") int cloudRestTimeout,
                                     @Input("slaveList") String slaveList,
                                     @Output("httpResponseCloudVapp") String httpResponseCloudVapp,
                                     @Output("expectedSlaveExistsCloud") boolean expectedSlaveExistsCloud) {
        assertEquals(cloudPortalCLIOperator.startWebInterface(), 0);
        boolean slavesStillExistsCloud = false;
        ArrayList<String> deletedJenkinsSlaves = new ArrayList<String>();
        if (slaveList.equals("manual")) {
            deletedJenkinsSlaves = deletedJenkinsSlavesManual;
        }
        if (slaveList.equals("PostBuild")) {
            deletedJenkinsSlaves = deletedJenkinsSlavesPostDelete;
        }
        if (slaveList.equals("PostRefresh")) {
            deletedJenkinsSlaves = context.getAttribute("jenkinsSlavesRefreshed");
        }
        if (slaveList.equals("PostCatalog")) {
            deletedJenkinsSlaves = deletedJenkinsSlavesCatalog;
        }
        for (String slave : deletedJenkinsSlaves) {
            CloudResponseObject listVappsResponse = new CloudResponseObject(cloudPortalOperator.listVappsInDatacenter(datacenterId, cloudRestTimeout));
            assertEquals(listVappsResponse.getResponseCode(), httpResponseCloudVapp);

            if ((listVappsResponse.getValueBySibling("vapps", "name", slave, "vapp_id") == null) ? false : true) {
                slavesStillExistsCloud = true;
                break;
            }
        }
        assertEquals(slavesStillExistsCloud, expectedSlaveExistsCloud);
    }

    @TestStep(id = Wait_Slaves_Come_Online)
    public void waitForSlavesToComeOnline(@Input("jenkinsHostName") String jenkinsHostName,
                                          @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                          @Input("jenkinsSlaveLabel") String jenkinsSlaveLabel,
                                          @Input("slaveList") String slaveList,
                                          @Input("jenkinsRestTimeout") int jenkinsRestTimeout,
                                          @Input("slaveOnlineTimeout") int slaveOnlineTimeout,
                                          @Output("expectedSlavesOnline") boolean expectedSlavesOnline) {
        ArrayList<String> deletedJenkinsSlaves = new ArrayList<String>();
        if (slaveList.equals("manual")) {
            deletedJenkinsSlaves = deletedJenkinsSlavesManual;
        }
        if (slaveList.equals("PostBuild")) {
            deletedJenkinsSlaves = deletedJenkinsSlavesPostDelete;
        }
        if (slaveList.equals("PostRefresh")) {
            deletedJenkinsSlaves = context.getAttribute("jenkinsSlavesRefreshed") ;
        }
        if (slaveList.equals("PostCatalog")) {
            deletedJenkinsSlaves = deletedJenkinsSlavesCatalog;
        }
        boolean allSlavesOnline = jenkinsOperator.waitForSlavesToComeOnline(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout, slaveOnlineTimeout, deletedJenkinsSlaves);
        assertEquals(allSlavesOnline, expectedSlavesOnline);
        assertEquals(cloudPortalCLIOperator.enableVappDestroyApi(), 0);
    }

    @TestStep(id = Check_Build_Job_Slave_Online)
    public void checkBuildJobSlaveOnline(
                                          @Input("jenkinsHostName") String jenkinsHostName,
                                          @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                          @Input("jenkinsSlaveLabel") String jenkinsSlaveLabel,
                                          @Input("jenkinsJobName") String jenkinsJobName,
                                          @Input("cloudAlive") boolean cloudAlive,
                                          @Input("slaveList") String slaveList,
                                          @Output("expectedSlaveOnline") boolean expectedSlaveOnline
                                          ) {
        if(!cloudAlive && slaveList.equals("bulkRecompose")){
            assertEquals(true, expectedSlaveOnline);
        }
        else{
            String slaveDisplayName = jenkinsOperator.getLastJobSlaveName(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);
            if(slaveList.equals("bulkRecompose"))
            {
                slaveDisplayName = jenkinsOperator.getStringFromConsole(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, "lastBuild", "Recomposing the Slave: ", "\n") ;
            }
            boolean slaveOnline = jenkinsOperator.isSlaveOnline(jenkinsHostName, jenkinsBaseDirectory, slaveDisplayName, 10000);
            System.out.println(slaveDisplayName + " is in state " + slaveOnline);
            assertEquals(slaveOnline, expectedSlaveOnline);
        }

    }

    @TestStep(id = Wait_Slaves_Come_Online_PreStep)
    public void waitForSlavesToComeOnlinePreStep(@Input("jenkinsHostName") String jenkinsHostName,
                                                 @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                                 @Input("jenkinsSlaveLabel") String jenkinsSlaveLabel,
                                                 @Input("slaveList") String slaveList,
                                                 @Input("jenkinsRestTimeout") int jenkinsRestTimeout,
                                                 @Input("slaveOnlinePostCatalogAddTimeout") int slaveOnlinePostCatalogAddTimeout,
                                                 @Output("expectedSlavesOnlinePreStep") boolean expectedSlavesOnlinePreStep) {
        ArrayList<String> deletedJenkinsSlaves = jenkinsOperator.getAllJenkinsSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout);
        boolean allSlavesOnline = jenkinsOperator.waitForSlavesToComeOnline(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout, slaveOnlinePostCatalogAddTimeout,
                deletedJenkinsSlaves);
        assertEquals(allSlavesOnline, expectedSlavesOnlinePreStep);
    }

    @TestStep(id = Build_Job)
    public void runBuildJob(@Input("jenkinsHostName") String jenkinsHostName,
                            @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                            @Input("jenkinsSlaveLabel") String jenkinsSlaveLabel,
                            @Input("jenkinsJobName") String jenkinsJobName,
                            @Input("jenkinsJobParameters") String jenkinsJobParameters) {
        jenkinsOperator.buildJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, jenkinsJobParameters);
        int queueRetryCount = 0;
        while (jenkinsOperator.jenkinsJobQueueStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName)) {
            queueRetryCount++;
            if (queueRetryCount < 60) {
                sleep(5);
            } else {
                break;
            }
        }
    }

    @TestStep(id = Get_Slave_From_Build_Log)
    public void getSlaveFromBuildLog(@Input("jenkinsHostName") String jenkinsHostName,
                                     @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                     @Input("jenkinsJobName") String jenkinsJobName,
                                     @Input("slaveList") String slaveList,
                                     @Input("cloudAlive") boolean cloudAlive) {
        String slaveToBeDeleted = jenkinsOperator.getStringFromConsole(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, "lastBuild", "Deleting the Slave:", "\n");
        slaveToBeDeleted = slaveToBeDeleted.replaceAll("\\s", "");
        if (slaveList.equals("PostBuild")) {
            deletedJenkinsSlavesPostDelete.add(slaveToBeDeleted);
        }
        if (slaveList.equals("PostRefresh") && cloudAlive) {
            deletedJenkinsSlavesRefresh.add(slaveToBeDeleted);
        }
        if (slaveList.equals("PostRefresh") && !cloudAlive) {
            deletedJenkinsSlavesRefresh = jenkinsSlavesRefresh;
        }
    }

    @TestStep(id = Wait_For_Job_To_Finish)
    public void waitForJobToFinish(@Input("jenkinsHostName") String jenkinsHostName,
                                   @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                   @Input("jenkinsSlaveLabel") String jenkinsSlaveLabel,
                                   @Input("jenkinsJobName") String jenkinsJobName,
                                   @Output("expectedJobResult") String expectedJobResult) {
        String jobResult = "";
        Document jobStatus = XMLHandler.createDocument(jenkinsOperator.jenkinsJobStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, "lastBuild").getBody());
        NodeList node = jobStatus.getElementsByTagName("result");
        if (node.getLength() > 0) {
            jobResult = node.item(0).getTextContent();
        }
        assertEquals(jobResult, expectedJobResult);
    }

    @TestStep(id = Put_Portal_Fully_Online)
    public void putPortalFullyOnline() {
        assertEquals(cloudPortalCLIOperator.enableVappDestroyApi(), 0);
        assertEquals(cloudPortalCLIOperator.enableVappAddToCatalogApi(), 0);
        assertEquals(cloudPortalCLIOperator.enableVappRecomposeApi(), 0);
        assertEquals(cloudPortalCLIOperator.enableVapptemplateListVmsApi(), 0);
        assertEquals(cloudPortalCLIOperator.startWebInterface(), 0);
    }

    @TestStep(id = Add_To_Catalog_PreStep)
    public void addToCatalogPreStep(@Input("jenkinsHostName") String jenkinsHostName,
                                    @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                    @Input("jenkinsPreStepJobName") String jenkinsPreStepJobName) {
        jenkinsOperator.buildJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsPreStepJobName, null);
        sleep(1);
        int queueRetryCount = 0;
        while (jenkinsOperator.jenkinsJobQueueStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsPreStepJobName)) {
            queueRetryCount++;
            if (queueRetryCount < 300) {
                sleep(2);
            } else {
                break;
            }
        }
    }

    @TestStep(id = Wait_For_Job_To_Finish_PreStep)
    public void waitForJobToFinishPreStep(@Input("jenkinsHostName") String jenkinsHostName,
                                          @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                          @Input("jenkinsPreStepJobName") String jenkinsPreStepJobName,
                                          @Output("expectedJobResult") String expectedJobResult) {
        assertEquals(jenkinsOperator.waitForBuildJobToFinish(jenkinsHostName, jenkinsBaseDirectory, jenkinsPreStepJobName), "SUCCESS");
    }

    @TestStep(id = Put_Cloud_Portal_Online)
    public void putCloudPortalOnline() {
        assertEquals(cloudPortalCLIOperator.startWebInterface(), 0);
    }

    @TestStep(id = Enable_Vapp_Destroy)
    public void enableVappDestroy() {
        assertEquals(cloudPortalCLIOperator.enableVappDestroyApi(), 0);
    }

    @TestStep(id = Enable_Catalog_Destroy)
    public void enableCatalogDestroy() {
        assertEquals(cloudPortalCLIOperator.enableVappAddToCatalogApi(), 0);
    }

    @TestStep(id = Wait_For_Build_Job_To_Finish)
    public void waitForBuildJobToFinish(@Input("jenkinsHostName") String jenkinsHostName,
                                        @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                        @Input("jenkinsJobName") String jenkinsJobName,
                                        @Output("expectedJobResult") String expectedJobResult) {
        assertEquals(jenkinsOperator.waitForBuildJobToFinish(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName), expectedJobResult);
    }

    @TestStep(id = Post_Build_Check)
    public void postBuildCheck(@Input("jenkinsHostName") String jenkinsHostName,
                               @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                               @Input("jenkinsJobName") String jenkinsJobName,
                               @Input("addToCatalogSucceeds") boolean addToCatalogSucceeds,
                               @Input("cloudAlive") boolean cloudAlive) {
        deletedJenkinsSlavesCatalog.clear();
        if (addToCatalogSucceeds) {
            if (cloudAlive) {
                String slaveToBeDeleted = jenkinsOperator.getStringFromConsole(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, "lastBuild", "Deleting this Slave: ", "\n");
                deletedJenkinsSlavesCatalog.add(slaveToBeDeleted);
            } else {
                String slaveToBeDeleted = jenkinsOperator.getStringFromConsole(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, "lastBuild", "Disconnecting the Slave:", "\n");
                deletedJenkinsSlavesCatalog.add(slaveToBeDeleted);
            }
        }
        if (!addToCatalogSucceeds) {
            String slaveToBeDeleted = jenkinsOperator.getStringFromConsole(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, "lastBuild", "Disconnecting the Slave:", "\n");
            deletedJenkinsSlavesCatalog.add(slaveToBeDeleted);
        }
    }

    @TestStep(id = Get_Slave_Name)
    public void getSlaveName(@Input("jenkinsHostName") String jenkinsHostName,
                             @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                             @Input("jenkinsJobName") String jenkinsJobName,
                             @Input("jenkinsSlaveLabel") String jenkinsSlaveLabel,
                             @Input("jenkinsRestTimeout") int jenkinsRestTimeout,
                             @Input("cloudDeleteSucceeds") boolean cloudDeleteSucceeds,
                             @Input("cloudAlive") boolean cloudAlive,
                             @Input("addToCatalogSucceeds") boolean addToCatalogSucceeds,
                             @Input("slaveOnlineEndTimeout") int slaveOnlineEndTimeout) {
        if (!cloudDeleteSucceeds) {
            for (String slave : deletedJenkinsSlavesCatalog) {
                cloudPluginOperator.deleteJenkinsSlave(jenkinsHostName, jenkinsBaseDirectory, slave, jenkinsRestTimeout);
            }
            boolean slavesOnline = jenkinsOperator.waitForSlavesToComeOnline(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout, slaveOnlineEndTimeout,
                    deletedJenkinsSlavesCatalog);
            assertEquals(true, slavesOnline);
        }
        if (!cloudAlive || !addToCatalogSucceeds) {
            ArrayList<String> executedSlave = new ArrayList<String>();
            String deletedSlave = jenkinsOperator.getStringFromConsole(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, "lastBuild", "Disconnecting the Slave:", "\n");
            executedSlave.add(deletedSlave);
            cloudPluginOperator.deleteJenkinsSlave(jenkinsHostName, jenkinsBaseDirectory, deletedSlave, jenkinsRestTimeout);
            boolean slavesOnline = jenkinsOperator.waitForSlavesToComeOnline(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout, slaveOnlineEndTimeout, executedSlave);
            assertEquals(true, slavesOnline);
        }
    }

    @TestStep(id = Update_Old_vAppTemplate_Name)
    public void updateOldVappTemplateName( @Input("jenkinsHostName") String jenkinsHostName,
                                           @Input("jenkinsBaseDirectory") String jenkinsBaseDirectory,
                                           @Input("jenkinsSlaveLabel") String jenkinsSlaveLabel,
                                           @Input("jenkinsRestTimeout") int jenkinsRestTimeout,
                                           @Input("catalogName") String catalogName,
                                           @Input("cloudRestTimeout") int cloudRestTimeout,
                                           @Input("templateCommonString") String templateCommonString,
                                           @Input("cloudVapptemplateListVmsSucceeds") boolean cloudVapptemplateListVmsSucceeds,
                                           @Input("cloudAlive") boolean cloudAlive){

        try{
            if(cloudAlive && cloudVapptemplateListVmsSucceeds){
                HttpResponse templateResponse = cloudPortalOperator.listVappTemplatesInCatalog(catalogName, cloudRestTimeout) ;
                CloudResponseObject listTemplatesResponse = new CloudResponseObject(templateResponse);
                List<Map<String, String>> templateNamesMap = listTemplatesResponse.getSimpleValues("vapptemplate_name") ;
                ArrayList<String> jenkinsSlaves = jenkinsOperator.getAllJenkinsSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, jenkinsRestTimeout);
                for(String slave: jenkinsSlaves){
                    String slaveId = cloudPortalOperator.getVappIdFromName(slave, datacenterId, cloudRestTimeout) ;
                    String latestVappTemplate = cloudPortalOperator.getLatestTemplateForVapp(slaveId, cloudRestTimeout) ;
                    ArrayList<String> templateNames = new ArrayList<String>() ;
                    String baseStr = templateCommonString ;
                    String oldestTemplateName = "";
                    SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss") ;
                    for(int i = 0 ; i < templateNamesMap.size(); i++){
                        String template = templateNamesMap.get(i).get("#text") ;
                        if(template.contains(baseStr)){
                            String strDate = template.substring(baseStr.length(), template.length()) ;
                            Date newDate = date.parse(strDate) ;
                            if(oldestTemplateName.equals("")){
                                if(newDate.before(date.parse(latestVappTemplate.substring(baseStr.length(), latestVappTemplate.length()))))
                                {
                                    oldestTemplateName = template ;
                                }
                            }
                            else{
                                if(newDate.before(date.parse(oldestTemplateName.substring(baseStr.length(), oldestTemplateName.length())))){
                                    oldestTemplateName = template ;
                                }
                            }
                            templateNames.add(template) ;
                        }
                    }
                    if(oldestTemplateName.equals("")){
                        System.out.println("No Older Template");
                        return ;
                    }
                    Date newestDate = date.parse(latestVappTemplate.substring(baseStr.length(), oldestTemplateName.length())) ;
                    long millisecs  = newestDate.getTime() + (60000*2);
                    newestDate.setTime(millisecs);
                    String updatedTemplateName = baseStr + date.format(newestDate) ;
                    String vappId = listTemplatesResponse.getValueBySibling("vapptemplates", "vapptemplate_name", oldestTemplateName, "vapptemplate_id");
                    HttpResponse renameResponse = cloudPortalOperator.renameVappTemplate(vappId, updatedTemplateName, cloudRestTimeout) ;
                    assertEquals(renameResponse.getResponseCode(), com.ericsson.cifwk.taf.tools.http.constants.HttpStatus.OK);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
