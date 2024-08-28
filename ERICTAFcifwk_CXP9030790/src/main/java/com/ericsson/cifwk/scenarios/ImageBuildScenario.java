package com.ericsson.cifwk.scenarios;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.runner;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.flows.ImageBuildFlows;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.cifwk.taf.scenario.TestScenarioRunner;
import com.ericsson.cifwk.taf.scenario.api.ExceptionHandler;
import com.ericsson.cifwk.taf.scenario.impl.LoggingScenarioListener;

public class ImageBuildScenario extends TorTestCaseHelper {
    @Inject
    ImageBuildFlows imageBuildFlows;

    @Test
    public void ImageBuild(){
        TestScenario scenario = scenario("Image Build Scenario")
                .addFlow(imageBuildFlows.getImageBuildFlow())
                .build();

        TestScenarioRunner runner = runner().withListener(new LoggingScenarioListener()).withExceptionHandler(ExceptionHandler.PROPAGATE).build();
        runner.start(scenario);
    }
}
