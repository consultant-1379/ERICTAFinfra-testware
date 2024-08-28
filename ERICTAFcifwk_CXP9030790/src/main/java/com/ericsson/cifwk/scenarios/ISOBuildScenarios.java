package com.ericsson.cifwk.scenarios;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.runner;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.flows.ISOBuildFlows;
import com.ericsson.cifwk.flows.ISOTestWareFlows;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.cifwk.taf.scenario.TestScenarioRunner;
import com.ericsson.cifwk.taf.scenario.api.ExceptionHandler;
import com.ericsson.cifwk.taf.scenario.impl.LoggingScenarioListener;

public class ISOBuildScenarios extends TorTestCaseHelper {
    @Inject
    ISOBuildFlows isoBuildFlows;
    @Inject
    ISOTestWareFlows isoTestWareFlows;
    
    @Test
    public void ISOBuildScenario(){
        TestScenario scenario = scenario("ISO Build Scenario")
                .addFlow(isoBuildFlows.getCheckIsoBeforeBuildFlow())
                .addFlow(isoBuildFlows.getISOBuildSetupDirFlow())
                .addFlow(isoBuildFlows.getISOConfigFilesFlow())
                .addFlow(isoBuildFlows.getISOBuildMavenFlow())
                .addFlow(isoBuildFlows.getISOBuildMountAndResultsFlow())
                .addFlow(isoBuildFlows.getISOBuildOutputFlow())
                .addFlow(isoBuildFlows.getISOBuildCIPortalOutputFlow())
                .addFlow(isoBuildFlows.getCheckIsoAfterFlow())
                .build();

        TestScenarioRunner runner = runner().withListener(new LoggingScenarioListener()).withExceptionHandler(ExceptionHandler.PROPAGATE).build();
        runner.start(scenario);
    }
    
        
    @Test
    public void ISOTestwareScenario(){
        TestScenario scenario = scenario("ISO Testware Scenario")
                .addFlow(isoTestWareFlows.getCheckIsoBeforeTestWareFlow())
                .addFlow(isoTestWareFlows.getISOTestWareSetupDirFlow())
                .addFlow(isoTestWareFlows.getISOTestWareConfigFilesFlow())
                .addFlow(isoTestWareFlows.getISOTestWareMavenFlow())
                .addFlow(isoTestWareFlows.getISOTestWareMountAndResultsFlow())
                .addFlow(isoTestWareFlows.getISOTestWareOutputFlow())
                .addFlow(isoTestWareFlows.getISOTestWareCIPortalOutputFlow())
                .addFlow(isoTestWareFlows.getCheckIsoTestWareAfterFlow())
                .addFlow(isoTestWareFlows.getCheckProductTestwareMediaMappings())
                .build();

        TestScenarioRunner runner = runner().withListener(new LoggingScenarioListener()).withExceptionHandler(ExceptionHandler.PROPAGATE).build();
        runner.start(scenario);
    }
}
