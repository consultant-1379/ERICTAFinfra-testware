package com.ericsson.cifwk.scenarios;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.flows.PortalRESTFlow;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.cifwk.taf.scenario.TestScenarioRunner;
import com.ericsson.cifwk.taf.scenario.api.ExceptionHandler;
import com.ericsson.cifwk.taf.scenario.impl.LoggingScenarioListener;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.runner;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;

public class PortalRESTScenario extends TorTestCaseHelper {

    @Inject
    PortalRESTFlow portalRESTFlow;

    @Test
    public void portalRESTScenario(){
        TestScenario scenario = scenario("Portal REST Scenario")
                .addFlow(portalRESTFlow.runRestCalls())
                .build();
        TestScenarioRunner runner = runner().withListener(new LoggingScenarioListener()).withExceptionHandler(ExceptionHandler.PROPAGATE).build();
        runner.start(scenario);
    }

    @Test
    public void InfraRESTScenario(){
        TestScenario scenario = scenario("Rest Call to return the Latest infrastructure/Applications in a Drop with infrastructure/Applications from selected ISO Version")
                .addFlow(portalRESTFlow.runRestCallsInfra())
                .addFlow(portalRESTFlow.runRestCallsInfraJsonReturn())
                .build();
        TestScenarioRunner runner = runner().withListener(new LoggingScenarioListener()).withExceptionHandler(ExceptionHandler.PROPAGATE).build();
        runner.start(scenario);
    }

    @Test
    public void genericPortalRESTScenario(){
        TestScenario scenario = scenario("Generic Portal REST Commands Scenario")
                .addFlow(portalRESTFlow.runGenericRests())
                .build();
        TestScenarioRunner runner = runner().withListener(new LoggingScenarioListener()).withExceptionHandler(ExceptionHandler.PROPAGATE).build();
        runner.start(scenario);
    }

    @Test
    public void openCloseDropRESTScenario(){
        TestScenario scenario = scenario("Open Close Drop REST Commands Scenario")
                .addFlow(portalRESTFlow.runDropActivityRests())
                .build();
        TestScenarioRunner runner = runner().withListener(new LoggingScenarioListener()).withExceptionHandler(ExceptionHandler.PROPAGATE).build();
        runner.start(scenario);
    }
}
