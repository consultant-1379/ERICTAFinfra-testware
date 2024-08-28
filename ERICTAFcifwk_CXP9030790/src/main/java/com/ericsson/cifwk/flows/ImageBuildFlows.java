package com.ericsson.cifwk.flows;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.scenario.*;
import com.ericsson.cifwk.teststeps.ImageBuildTestSteps;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.*;

public class ImageBuildFlows {

    @Inject
    ImageBuildTestSteps imageBuildTestSteps;

    public TestStepFlow getImageBuildFlow() {
        TestStepFlow imageBuild = flow("Image Build Flow")
                .addTestStep(annotatedMethod(imageBuildTestSteps, ImageBuildTestSteps.SETUP_DIRECTORIES))
                .addTestStep(annotatedMethod(imageBuildTestSteps, ImageBuildTestSteps.BASE_BUILD))
                .addTestStep(annotatedMethod(imageBuildTestSteps, ImageBuildTestSteps.BASE_RHEL_BUILD))
                .addTestStep(annotatedMethod(imageBuildTestSteps, ImageBuildTestSteps.IMAGE_BUILD_AND_VERIFY))
                .withDataSources(dataSource("imageBuildCommands"))
                .build();
        return imageBuild;
    }
}
