package com.ericsson.cloud.flows;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.annotatedMethod;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.dataSource;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.flow;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import com.ericsson.cloud.teststeps.CloudPluginTestSteps;

public class CloudPluginFlows {

    @Inject
    CloudPluginTestSteps cloudPluginTestSteps ;

    public TestStepFlow cloudPluginHealthCheckFlowPositive() {
        TestStepFlow healthCheckFlow = flow("Manual Delete Flow")
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disconnect_Slaves_Of_Label))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Place_Health_Check_Script))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Slaves_Online_Check))
                .withDataSources(dataSource("cloudPluginHealthCheckPositive"))
                .build();
        return healthCheckFlow;
    }

    public TestStepFlow cloudPluginManualDeleteTestFlowPositive() {
        TestStepFlow manualDelete = flow("Manual Delete Flow")
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_Jenkins_Slaves))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_DataCenter_ID))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Manual_Delete_Slaves_Portal))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Vapp_Destroy))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Cloud_Portal))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Manual_Delete_Slaves_Jenkins))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Jenkins))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Cloud))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_Slaves_Come_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .withDataSources(dataSource("cloudPluginManualDeletePositive"))
                .build() ;
        return manualDelete;
    }

    public TestStepFlow cloudPluginPostBuildDeleteFlowPositive() {
        TestStepFlow postBuildDelete = flow("Post Build Delete")
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Slaves_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_DataCenter_ID))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Vapp_Destroy))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Cloud_Portal))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Build_Job))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Build_Job_To_Finish))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_Slave_From_Build_Log))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Jenkins))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Cloud))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_Slaves_Come_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Job_To_Finish))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .withDataSources(dataSource("cloudPluginPostBuildDeletePositive"))
                .build() ;
        return postBuildDelete ;
    }

    public TestStepFlow cloudPluginPostBuildRecomposeFlowPositive() {
        TestStepFlow postBuildDelete = flow("Post Build Recompose")
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Slaves_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Vapp_Recompose))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Remember_Vms_Before_Recompose))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Cloud_Portal))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Build_Job))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Build_Job_To_Finish))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Build_Job_Slave_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Vms_Were_Recomposed))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .withDataSources(dataSource("cloudPluginPostBuildRecomposePositive"))
                .build() ;
        return postBuildDelete ;
    }

    public TestStepFlow cloudPluginPostBuildRefreshFlowPositive() {
        TestStepFlow postBuildRefresh = flow("Post Build Refresh")
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Slaves_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Enable_Catalog_Destroy))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Add_To_Catalog_PreStep))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Job_To_Finish_PreStep))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_Slaves_Come_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_Jenkins_Slaves))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_DataCenter_ID))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Vapp_Destroy))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Cloud_Portal))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Build_Job))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Build_Job_To_Finish))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_Slave_From_Build_Log))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Jenkins))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Cloud_Portal_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Cloud))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_Slaves_Come_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .withDataSources(dataSource("cloudPluginBuildRefreshSlavesPositive"))
                .build() ;
        return postBuildRefresh ;
    }

    public TestStepFlow cloudPluginBuildStepBulkRecomposeFlowPositive() {
        TestStepFlow buildBulkRecompose = flow("Build Step Bulk Recompose")
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Slaves_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Vapp_Recompose))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Remember_Vms_Before_Recompose))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_DataCenter_ID))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Update_Old_vAppTemplate_Name))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Cloud_Portal))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_Jenkins_Slaves))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Build_Job))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Build_Job_To_Finish))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Build_Job_Slave_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Vms_Were_Recomposed))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .withDataSources(dataSource("cloudPluginBuildRecomposeSlavesPositive"))
                .build() ;
        return buildBulkRecompose ;
    }

    public TestStepFlow cloudPluginPostBuildAddToCatalogPositive() {
        TestStepFlow postBuildAddToCatalog = flow("Post Build Add To Catalog")
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Slaves_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_DataCenter_ID))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Vapp_Destroy))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Cloud_Portal))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Add_To_Catalog))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Build_Job))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Build_Job_To_Finish))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Post_Build_Check))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Jenkins))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Cloud_Portal_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Cloud))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_Slaves_Come_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Enable_Vapp_Destroy))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_Slave_Name))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Job_To_Finish))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .withDataSources(dataSource("cloudPluginPostBuildAddToCatalogPositive"))
                .build();
        return postBuildAddToCatalog ;
    }

    public TestStepFlow cloudPluginManualDeleteTestFlowNegative() {
        TestStepFlow manualDelete = flow("Manual Delete Flow")
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Slaves_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_Jenkins_Slaves))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_DataCenter_ID))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Manual_Delete_Slaves_Portal))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Vapp_Destroy))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Cloud_Portal))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Manual_Delete_Slaves_Jenkins))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Jenkins))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Cloud))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_Slaves_Come_Online))
                .withDataSources(dataSource("cloudPluginManualDeleteNegative"))
                .build() ;
        return manualDelete;
    }

    public TestStepFlow cloudPluginPostBuildDeleteFlowNegative() {
        TestStepFlow postBuildDelete = flow("Post Build Delete")
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Slaves_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_DataCenter_ID))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Vapp_Destroy))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Cloud_Portal))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Build_Job))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Build_Job_To_Finish))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_Slave_From_Build_Log))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Jenkins))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Cloud))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_Slaves_Come_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Job_To_Finish))
                .withDataSources(dataSource("cloudPluginPostBuildDeleteNegative"))
                .build() ;
        return postBuildDelete ;
    }

    public TestStepFlow cloudPluginPostBuildRecomposeFlowNegative() {
        TestStepFlow postBuildDelete = flow("Post Build Recompose")
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Slaves_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Vapptemplate_List_Vms))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Vapp_Recompose))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Cloud_Portal))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Build_Job))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Build_Job_To_Finish))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Build_Job_Slave_Online))
                .withDataSources(dataSource("cloudPluginPostBuildRecomposeNegative"))
                .build();
        return postBuildDelete ;
    }

    public TestStepFlow cloudPluginPostBuildRefreshFlowNegative() {
        TestStepFlow postBuildRefresh = flow("Post Build Refresh")
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Slaves_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Enable_Catalog_Destroy))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Add_To_Catalog_PreStep))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Job_To_Finish_PreStep))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_Slaves_Come_Online_PreStep))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_Jenkins_Slaves))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_DataCenter_ID))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Vapp_Destroy))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Cloud_Portal))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Build_Job))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Build_Job_To_Finish))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_Slave_From_Build_Log))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Jenkins))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Cloud_Portal_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Cloud))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_Slaves_Come_Online))
                .withDataSources(dataSource("cloudPluginBuildRefreshSlavesNegative"))
                .build() ;
        return postBuildRefresh ;
    }

    public TestStepFlow cloudPluginPostBuildAddToCatalogNegative() {
        TestStepFlow postBuildAddToCatalog = flow("Post Build Add To Catalog")
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Slaves_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_DataCenter_ID))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Vapp_Destroy))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Cloud_Portal))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Add_To_Catalog))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Build_Job))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Build_Job_To_Finish))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Post_Build_Check))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Jenkins))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Cloud_Portal_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Slaves_Gone_Cloud))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_Slaves_Come_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Enable_Vapp_Destroy))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_Slave_Name))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Job_To_Finish))
                .withDataSources(dataSource("cloudPluginPostBuildAddToCatalogNegative"))
                .build();
        return postBuildAddToCatalog ;
    }

    public TestStepFlow cloudPluginBuildStepBulkRecomposeFlowNegative() {
        TestStepFlow buildBulkRecompose = flow("Build Step Bulk Recompose")
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Portal_Fully_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Put_Slaves_Online))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Vapp_Recompose))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Remember_Vms_Before_Recompose))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_DataCenter_ID))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Update_Old_vAppTemplate_Name))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Disable_Cloud_Portal))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Get_Jenkins_Slaves))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Build_Job))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Wait_For_Build_Job_To_Finish))
                .addTestStep(annotatedMethod(cloudPluginTestSteps, CloudPluginTestSteps.Check_Build_Job_Slave_Online))
                .withDataSources(dataSource("cloudPluginBuildRecomposeSlavesNegative"))
                .build() ;
        return buildBulkRecompose ;
    }

}
