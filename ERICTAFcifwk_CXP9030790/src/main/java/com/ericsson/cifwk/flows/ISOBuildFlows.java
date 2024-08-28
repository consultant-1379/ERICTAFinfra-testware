package com.ericsson.cifwk.flows;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.scenario.*;
import com.ericsson.cifwk.teststeps.ISOBuildTestSteps;
import com.ericsson.cifwk.teststeps.ISOChangesTestSteps;
// statically import the taf TestScenarios class.
import static com.ericsson.cifwk.taf.scenario.TestScenarios.*;

public class ISOBuildFlows {

    @Inject
    ISOChangesTestSteps isoChangesTestSteps;

    @Inject
    ISOBuildTestSteps isoBuildTestSteps;

    public TestStepFlow getCheckIsoBeforeBuildFlow() {
        TestStepFlow isoBuild = flow("ISO Check Before")
                .addTestStep(annotatedMethod(isoChangesTestSteps, ISOChangesTestSteps.IS_ISO_BUILD_NEEDED))
                .withDataSources(dataSource("testISOChanges").withFilter("ExpectedResult=='true'"))
                .build();
        return isoBuild;
    }

    public TestStepFlow getISOBuildSetupDirFlow() {
        TestStepFlow directories = flow("Setup Directories")
                .addTestStep(annotatedMethod(isoBuildTestSteps, ISOBuildTestSteps.SETUP_DIRECTORIES))
                .withDataSources(dataSource("isoBuildSetupDir"))
                .build();
        return directories;
    }
    
    public TestStepFlow getISOConfigFilesFlow() {
        TestStepFlow copyConfigFiles = flow("Copy ISO Config file to temp Directory")
                .addTestStep(annotatedMethod(isoBuildTestSteps, ISOBuildTestSteps.COPYCONFIGFILES_TO_TMPDIRECTORY))
                .withDataSources(dataSource("isoCopyConfigFiles"))
                .build();
        return copyConfigFiles;
    }

    public TestStepFlow getISOBuildMavenFlow() {
        TestStepFlow mavenBuild = flow("Maven Build")
                .addTestStep(annotatedMethod(isoBuildTestSteps, ISOBuildTestSteps.MAVEN_ISO_BUILD))
                .withDataSources(dataSource("isoBuildMaven"))
                .build();
        return mavenBuild;
    }

    public TestStepFlow getISOBuildMountAndResultsFlow() {
        TestStepFlow mnt = flow("Mount ISO Build And Results")
                .addTestStep(annotatedMethod(isoBuildTestSteps, ISOBuildTestSteps.MOUNT_ISO))
                .withDataSources(dataSource("isoBuildMountAndFind"))
                .build();
        return mnt;
    }

    public TestStepFlow getISOBuildOutputFlow() {
        TestStepFlow mnt = flow("Check Results")
                .addTestStep(annotatedMethod(isoBuildTestSteps, ISOBuildTestSteps.VERIFY_OUTPUT))
                .withDataSources(dataSource("isoBuildResultsCheckMount"))
                .build();
        return mnt;
    }

    public TestStepFlow getISOBuildCIPortalOutputFlow() {
        TestStepFlow portal = flow("Check CI Portal Results")
                .addTestStep(annotatedMethod(isoBuildTestSteps, ISOBuildTestSteps.VERIFY_OUTPUT_CI_PORTAL))
                .withDataSources(dataSource("isoBuildResultsCheckCIPortal"))
                .build();
        return portal;
    }

    public TestStepFlow getCheckIsoAfterFlow() {
        TestStepFlow isoCheckAfterBuild = flow("Check ISO After")
                 .addTestStep(annotatedMethod(isoChangesTestSteps, ISOChangesTestSteps.IS_ISO_BUILD_NEEDED))
                 .withDataSources(dataSource("testISOChanges").withFilter("ExpectedResult=='false'"))
                 .build();
        return isoCheckAfterBuild;
     }
}
