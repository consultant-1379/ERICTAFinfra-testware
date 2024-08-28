package com.ericsson.cifwk.flows;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.scenario.*;
import com.ericsson.cifwk.teststeps.DMTTestSteps;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.*;

public class DMTFlows {

    @Inject
    DMTTestSteps dmtTestSteps;

    public TestStepFlow uploadSED() {
        TestStepFlow upload = flow("Upload prepopulated SED")
                .addTestStep(
                        annotatedMethod(dmtTestSteps, DMTTestSteps.SED_SETUP))
                .withDataSources(dataSource("dmtSetupSed")).build();
        return upload;
    }

    public TestStepFlow verifyJSON() {
        TestStepFlow verify = flow("Verify host properties")
                .addTestStep(
                        annotatedMethod(dmtTestSteps,
                                DMTTestSteps.VERIFY_HOST_PROPERTIES))
                .withDataSources(dataSource("verifyJsonHostProperties"))
                .build();
        return verify;
    }
}