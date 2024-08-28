package com.ericsson.cifwk.scenarios;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.runner;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.flows.DMTFlows;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.cifwk.taf.scenario.TestScenarioRunner;
import com.ericsson.cifwk.taf.scenario.api.ExceptionHandler;
import com.ericsson.cifwk.taf.scenario.impl.LoggingScenarioListener;

public class DMTScenarios extends TorTestCaseHelper {
    @Inject
    DMTFlows dmtFlows;

    @Test
    public void DMTScenario() {
        TestScenario scenario = scenario("DMT Scenario")
                .addFlow(dmtFlows.uploadSED())
                .addFlow(dmtFlows.verifyJSON())
                .build();

        TestScenarioRunner runner = runner()
                .withListener(new LoggingScenarioListener())
                .withExceptionHandler(ExceptionHandler.PROPAGATE).build();
        runner.start(scenario);
    }
}
