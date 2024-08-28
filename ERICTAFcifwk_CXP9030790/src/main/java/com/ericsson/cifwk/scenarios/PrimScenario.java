package com.ericsson.cifwk.scenarios;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.flows.PortalRESTFlow;
import com.ericsson.cifwk.flows.PrimFlows;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.cifwk.taf.scenario.TestScenarioRunner;
import com.ericsson.cifwk.taf.scenario.api.ExceptionHandler;
import com.ericsson.cifwk.taf.scenario.impl.LoggingScenarioListener;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.runner;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;

public class PrimScenario extends TorTestCaseHelper {

    @Inject
    PrimFlows primFlow;

    @Test
    public void primScenario(){
        TestScenario scenario = scenario("PRIM Scenario")
                .addFlow(primFlow.getManagementCommandsFlow())
                .addFlow(primFlow.runRestCalls())
                .build();
        TestScenarioRunner runner = runner().withListener(new LoggingScenarioListener()).withExceptionHandler(ExceptionHandler.PROPAGATE).build();
        runner.start(scenario);
    }
}
