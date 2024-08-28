package com.ericsson.cifwk.flows;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import com.ericsson.cifwk.teststeps.DynamicYumRepoTestSteps;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.*;

public class DynamicRepoForDropFlow {

    @Inject
    DynamicYumRepoTestSteps yumRepoTestSteps;

    public TestStepFlow getSetupFlow() {
        TestStepFlow setup = flow("Repo Setup Flow")
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.TESTCASE_SETUP))
                .withDataSources(dataSource("dynamicRepoSetup"))
                .build();
        return setup;
    }

    public TestStepFlow getRepoLocationCreationFlow() {
        TestStepFlow repoCreation = flow("Repo Creation Flow")
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.SETUP_DIRECTORIES))
                .withDataSources(dataSource("dynamicRepoLocationCreation"))
                .build();
        return repoCreation;
    }

    public TestStepFlow getYumConfUpdateFlow() {
        TestStepFlow updateConf = flow("Update yum.conf file")
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.UPDATE_YUM_CONFIG))
                .withDataSources(dataSource("yumConfUpdate"))
                .build();
        return updateConf;
    }

    public TestStepFlow getAddingArtifactToRepoLocationFlow() {
        TestStepFlow addingArtifacts = flow("Adding Artifacts To Repo Location Flow")
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.DOWNLOAD_ARTIFACTS))
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.ADD_ARTIFACTS_TO_REPO_LOCATION))
                .withDataSources(dataSource("downloadArtifacts"),
                        dataSource("addArtifactsToRepoLocation"))
                .build();
        return addingArtifacts;
    }

    public TestStepFlow getYumRepoPositiveFlow() {
        TestStepFlow yumRepo = flow("Yum Repo Positive Flow")
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_REPO_CREATION))
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_ADD_AND_CONTENT_CHECK))
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_REPO_DELETION))
                .withDataSources(dataSource("dynamicRepoCreation"),
                        dataSource("dynamicYumRepo"),
                        dataSource("dynamicRepoDelete"))
                .build();
        return yumRepo;
    }

    public TestStepFlow getYumRepoPositiveAddPackageFlow() {
        TestStepFlow yumRepo = flow("Yum Repo Positive Add Package Flow")
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_REPO_CREATION))
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_ADD_AND_CONTENT_CHECK))
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_REPO_DELETION))
                .withDataSources(
                        dataSource("dynamicRepoCreationPositiveAddPackage"),
                        dataSource("dynamicYumRepoPositiveAddPackage"),
                        dataSource("dynamicRepoDelete"))
                .build();
        return yumRepo;
    }

    public TestStepFlow getYumRepoInfraFlow() {
        TestStepFlow yumRepo = flow("Yum Repo Latest Infra Flow")
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_REPO_CREATION))
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_ADD_AND_CONTENT_CHECK))
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_REPO_DELETION))
                .withDataSources(dataSource("dynamicRepoCreationInfra"),
                        dataSource("dynamicRepoContentInfra"),
                        dataSource("dynamicRepoDelete"))
                .build();
        return yumRepo;
    }

    public TestStepFlow getYumRepoInfraPassedISOFlow() {
        TestStepFlow yumRepo = flow("Yum Repo Latest Infra And Latest Passed ISO Flow")
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_REPO_CREATION))
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_ADD_AND_CONTENT_CHECK))
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_REPO_DELETION))
                .withDataSources(
                        dataSource("dynamicRepoCreationInfraPassedISO"),
                        dataSource("dynamicRepoContentInfra"),
                        dataSource("dynamicRepoDelete"))
                .build();
        return yumRepo;
    }

    public TestStepFlow getYumRepoAppFlow() {
        TestStepFlow yumRepo = flow("Yum Repo Latest App Flow")
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_REPO_CREATION))
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_ADD_AND_CONTENT_CHECK))
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_REPO_DELETION))
                .withDataSources(dataSource("dynamicRepoCreationApp"),
                        dataSource("dynamicRepoContentApp"),
                        dataSource("dynamicRepoDelete")).build();
        return yumRepo;
    }

    public TestStepFlow getYumRepoAppPassedISOFlow() {
        TestStepFlow yumRepo = flow(
                "Yum Repo Latest App And Latest Passed ISO Flow")
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_REPO_CREATION))
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_ADD_AND_CONTENT_CHECK))
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_REPO_DELETION))
                .withDataSources(dataSource("dynamicRepoCreationAppPassedISO"),
                        dataSource("dynamicRepoContentApp"),
                        dataSource("dynamicRepoDelete"))
                .build();
        return yumRepo;
    }

    public TestStepFlow getYumRepoNegativeCreateFlow() {
        TestStepFlow yumRepo = flow("Yum Repo Negative Create Flow")
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_REPO_CREATION))
                .withDataSources(dataSource("dynamicRepoCreationNegative"))
                .build();
        return yumRepo;
    }

    public TestStepFlow getYumRepoNegativeDeleteFlow() {
        TestStepFlow yumRepo = flow("Yum Repo Negative Delete Flow")
                .addTestStep(annotatedMethod(yumRepoTestSteps, DynamicYumRepoTestSteps.YUM_REPO_DELETION))
                .withDataSources(dataSource("dynamicRepoDeleteNegative"))
                .build();
        return yumRepo;
    }

}
