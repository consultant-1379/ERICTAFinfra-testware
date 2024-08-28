package com.ericsson.cifwk.scenarios;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.flows.DynamicRepoForDropFlow;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.cifwk.taf.scenario.TestScenarioRunner;
import com.ericsson.cifwk.taf.scenario.api.ExceptionHandler;
import com.ericsson.cifwk.taf.scenario.impl.LoggingScenarioListener;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.runner;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;

public class DynamicRepoScenario extends TorTestCaseHelper {

    @Inject
    DynamicRepoForDropFlow dynamicRepoForDropFlow;

    @Test
    public void dynamicRepoScenario(){
        TestScenario scenario = scenario("Dynamic Repo Scenario")
                .addFlow(dynamicRepoForDropFlow.getSetupFlow())
                .addFlow(dynamicRepoForDropFlow.getYumConfUpdateFlow())
                .addFlow(dynamicRepoForDropFlow.getRepoLocationCreationFlow())
                .addFlow(dynamicRepoForDropFlow.getAddingArtifactToRepoLocationFlow())
                .addFlow(dynamicRepoForDropFlow.getYumRepoPositiveFlow())
                .addFlow(dynamicRepoForDropFlow.getYumRepoPositiveAddPackageFlow())
                .addFlow(dynamicRepoForDropFlow.getYumRepoNegativeCreateFlow())
                .addFlow(dynamicRepoForDropFlow.getYumRepoNegativeDeleteFlow())
                .addFlow(dynamicRepoForDropFlow.getYumRepoInfraFlow())
                .addFlow(dynamicRepoForDropFlow.getYumRepoInfraPassedISOFlow())
                .addFlow(dynamicRepoForDropFlow.getYumRepoAppFlow())
                .addFlow(dynamicRepoForDropFlow.getYumRepoAppPassedISOFlow())
                .build();
        TestScenarioRunner runner = runner().withListener(new LoggingScenarioListener()).withExceptionHandler(ExceptionHandler.PROPAGATE).build();
        runner.start(scenario);
    }
}
