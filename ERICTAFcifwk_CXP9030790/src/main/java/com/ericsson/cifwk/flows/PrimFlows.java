package com.ericsson.cifwk.flows;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.annotatedMethod;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.dataSource;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.flow;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import com.ericsson.cifwk.teststeps.ManagementCommandsTestSteps;
import com.ericsson.cifwk.teststeps.PortalRESTTestSteps;

public class PrimFlows {

    @Inject
    ManagementCommandsTestSteps managementCommandsTestSteps;

    @Inject
    PortalRESTTestSteps portalRESTTestSteps;

    public TestStepFlow getManagementCommandsFlow() {
        TestStepFlow managementCommands = flow("Run MGMT Commands PRIM Setup Flow")
                .addTestStep(annotatedMethod(managementCommandsTestSteps, ManagementCommandsTestSteps.MANAGEMENT_COMMANDS))
                .withDataSources(dataSource("primManagementCommands"))
                .build();
        return managementCommands;
    }

    public TestStepFlow runRestCalls() {
        TestStepFlow restCalls = flow("Run PRIM REST Call Flow")
                .addTestStep(annotatedMethod(portalRESTTestSteps, PortalRESTTestSteps.RUN_REST_CALLS_TIMEOUT))
                .withDataSources(dataSource("primRESTCommands"))
                .build();
        return restCalls;
    }
}
