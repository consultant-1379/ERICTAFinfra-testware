package com.ericsson.cifwk.flows;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.annotatedMethod;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.dataSource;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.flow;
import javax.inject.Inject;
import com.ericsson.cifwk.taf.scenario.TestStepFlow;
import com.ericsson.cifwk.teststeps.ManagementCommandsTestSteps;

public class ManagementCommandsFlows {
    
    @Inject
    ManagementCommandsTestSteps managementCommandsTestSteps;
    
    public TestStepFlow getManagementCommandsFlow() {
        TestStepFlow managementCommands = flow("Run CIFWK MGMT Commands Flow")
                .addTestStep(annotatedMethod(managementCommandsTestSteps, ManagementCommandsTestSteps.MANAGEMENT_COMMANDS))
                .withDataSources(dataSource("managementCommands"))
                .build();
        return managementCommands;
    }
    
    public TestStepFlow getProductManagementCommandsFlow() {
        TestStepFlow productManagementCommands = flow("Run CIFWK Product MGMT Commands Flow")
                .addTestStep(annotatedMethod(managementCommandsTestSteps, ManagementCommandsTestSteps.PRODUCT_MANAGEMENT_COMMANDS))
                .withDataSources(dataSource("managementCommandsProduct"))
                .build();
        return productManagementCommands;
    }

}
