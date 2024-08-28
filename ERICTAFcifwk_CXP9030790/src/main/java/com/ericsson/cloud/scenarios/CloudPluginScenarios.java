package com.ericsson.cloud.scenarios;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.runner;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.scenario.ExecutionMode;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.cifwk.taf.scenario.TestScenarioRunner;
import com.ericsson.cifwk.taf.scenario.api.ExceptionHandler;
import com.ericsson.cifwk.taf.scenario.impl.LoggingScenarioListener;
import com.ericsson.cloud.flows.CloudPluginFlows;


public class CloudPluginScenarios extends TorTestCaseHelper {

    @Inject
    CloudPluginFlows cloudPluginFlows ;

    @Test
    public void cloudPluginPositiveScenario() {
        TestScenario scenario = scenario("CloudPlugin Positive Scenario")
                .addFlow(cloudPluginFlows.cloudPluginManualDeleteTestFlowPositive())
                .addFlow(cloudPluginFlows.cloudPluginPostBuildDeleteFlowPositive())
                .addFlow(cloudPluginFlows.cloudPluginPostBuildRecomposeFlowPositive())
                .addFlow(cloudPluginFlows.cloudPluginPostBuildAddToCatalogPositive())
                .addFlow(cloudPluginFlows.cloudPluginPostBuildRefreshFlowPositive())
                .addFlow(cloudPluginFlows.cloudPluginBuildStepBulkRecomposeFlowPositive())
                .addFlow(cloudPluginFlows.cloudPluginHealthCheckFlowPositive())
                .withExecutionMode(ExecutionMode.PARALLEL)
                .build();

        TestScenarioRunner runner = runner().withListener(new LoggingScenarioListener()).withExceptionHandler(ExceptionHandler.PROPAGATE).build();
        runner.start(scenario);
    }

    @Test
    public void cloudPluginNegativeScenario() {
        TestScenario scenario = scenario("CloudPlugin Negative Scenario")
                .addFlow(cloudPluginFlows.cloudPluginManualDeleteTestFlowNegative())
                .addFlow(cloudPluginFlows.cloudPluginPostBuildDeleteFlowNegative())
                .addFlow(cloudPluginFlows.cloudPluginPostBuildRecomposeFlowNegative())
                .addFlow(cloudPluginFlows.cloudPluginPostBuildRefreshFlowNegative())
                .addFlow(cloudPluginFlows.cloudPluginPostBuildAddToCatalogNegative())
                .addFlow(cloudPluginFlows.cloudPluginBuildStepBulkRecomposeFlowNegative())
                .withExecutionMode(ExecutionMode.SEQUENTIAL)
                .build();

        TestScenarioRunner runner = runner().withListener(new LoggingScenarioListener()).withExceptionHandler(ExceptionHandler.PROPAGATE).build();
        runner.start(scenario);
    }
}
