package com.ericsson.cifwk.scenarios;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.runner;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.flows.ImageBuildFlows;
import com.ericsson.cifwk.flows.ManagementCommandsFlows;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.scenario.ExecutionMode;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.cifwk.taf.scenario.TestScenarioRunner;
import com.ericsson.cifwk.taf.scenario.api.ExceptionHandler;
import com.ericsson.cifwk.taf.scenario.impl.LoggingScenarioListener;

public class ManagementCommandsScenario extends TorTestCaseHelper {
    
    @Inject
    ManagementCommandsFlows managementCommandsFlows;
    @Inject
    ImageBuildFlows imageBuildFlows;

    @Test
    public void RunManagementCommands(){
        TestScenario scenario = scenario("Run CIFWK Commands / Image Build Parallel Scenario")
                .addFlow(managementCommandsFlows.getManagementCommandsFlow())
                .addFlow(imageBuildFlows.getImageBuildFlow())
                .withVusers(1)
                .withExecutionMode(ExecutionMode.PARALLEL)
                .build();
        
        TestScenarioRunner runner = runner().withListener(new LoggingScenarioListener()).withExceptionHandler(ExceptionHandler.PROPAGATE).build();
        runner.start(scenario);
        
    }

}
