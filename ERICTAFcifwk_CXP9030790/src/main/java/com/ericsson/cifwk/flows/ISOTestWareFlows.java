package com.ericsson.cifwk.flows;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.scenario.*;
import com.ericsson.cifwk.teststeps.*;

// statically import the taf TestScenarios class.
import static com.ericsson.cifwk.taf.scenario.TestScenarios.*;

public class ISOTestWareFlows {

    @Inject
    ISOChangesTestSteps isoChangesTestSteps;

    @Inject
    ISOBuildTestSteps isoBuildTestSteps;
    
    @Inject
    PortalRESTTestSteps portalRESTTestSteps;

    public TestStepFlow getCheckIsoBeforeTestWareFlow() {
        TestStepFlow isoBuild = flow("ISO Check Before")
                .addTestStep(annotatedMethod(isoChangesTestSteps, ISOChangesTestSteps.IS_ISO_BUILD_NEEDED))
                .withDataSources(dataSource("testISOChanges").withFilter("ExpectedResult=='true'"))
                .build();
        return isoBuild;
    }

    public TestStepFlow getISOTestWareSetupDirFlow() {
        TestStepFlow directories = flow("Setup Directories")
                .addTestStep(annotatedMethod(isoBuildTestSteps, ISOBuildTestSteps.SETUP_DIRECTORIES))
                .withDataSources(dataSource("isoTestWareSetupDir"))
                .build();
        return directories;
    }
    
    public TestStepFlow getISOTestWareConfigFilesFlow() {
        TestStepFlow copyConfigFiles = flow("Copy ISO Config file to temp Directory")
                .addTestStep(annotatedMethod(isoBuildTestSteps, ISOBuildTestSteps.COPYCONFIGFILES_TO_TMPDIRECTORY))
                .withDataSources(dataSource("isoCopyTestWareConfigFiles"))
                .build();
        return copyConfigFiles;
    }
    
    public TestStepFlow getISOTestWareMavenFlow() {
        TestStepFlow mavenBuild = flow("Maven Build")
                .addTestStep(annotatedMethod(isoBuildTestSteps, ISOBuildTestSteps.MAVEN_ISO_BUILD))
                .withDataSources(dataSource("isoTestWareMaven"))
                .build();
        return mavenBuild;
    }

    public TestStepFlow getISOTestWareMountAndResultsFlow() {
        TestStepFlow mnt = flow("Mount ISO Build And Results")
                .addTestStep(annotatedMethod(isoBuildTestSteps, ISOBuildTestSteps.MOUNT_ISO))
                .withDataSources(dataSource("isoTestWareMountAndFind"))
                .build();
        return mnt;
    }

    public TestStepFlow getISOTestWareOutputFlow() {
        TestStepFlow mnt = flow("Check Results")
                .addTestStep(annotatedMethod(isoBuildTestSteps, ISOBuildTestSteps.VERIFY_OUTPUT))
                .withDataSources(dataSource("isoTestWareResultsCheckMount"))
                .build();
        return mnt;
    }

    public TestStepFlow getISOTestWareCIPortalOutputFlow() {
        TestStepFlow portal = flow("Check CI Portal Results")
                .addTestStep(annotatedMethod(isoBuildTestSteps, ISOBuildTestSteps.VERIFY_OUTPUT_CI_PORTAL))
                .withDataSources(dataSource("isoTestWareResultsCheckCIPortal"))
                .build();
        return portal;
    }

    public TestStepFlow getCheckIsoTestWareAfterFlow() {
        TestStepFlow isoCheckAfterBuild = flow("Check ISO After")
                 .addTestStep(annotatedMethod(isoChangesTestSteps, ISOChangesTestSteps.IS_ISO_BUILD_NEEDED))
                 .withDataSources(dataSource("testISOChanges").withFilter("ExpectedResult=='false'"))
                 .build();
        return isoCheckAfterBuild;
     }
    
    public TestStepFlow getCheckProductTestwareMediaMappings() {
        TestStepFlow mappingCheck = flow("Check Product to Testware Media Mappings")
                .addTestStep(annotatedMethod(portalRESTTestSteps, PortalRESTTestSteps.RUN_REST_CALLS))
                .withDataSources(dataSource("isoTestwareMapping"))
                .build();
       return mappingCheck;
    }
}
