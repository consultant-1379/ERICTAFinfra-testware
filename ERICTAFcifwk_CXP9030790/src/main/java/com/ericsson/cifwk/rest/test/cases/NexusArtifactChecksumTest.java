package com.ericsson.cifwk.rest.test.cases;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.sut.test.operators.JenkinsRESTOperator;
import com.ericsson.sut.test.operators.NexusOperator;
import com.google.inject.Inject;
import com.ericsson.cifwk.taf.annotations.*;

public class NexusArtifactChecksumTest extends TorTestCaseHelper implements
        TestCase {

    private NexusOperator nexusOperator;
    Logger logger = Logger.getLogger(JenkinsRESTOperator.class);

    @Inject
    OperatorRegistry<NexusOperator> operatorRegistry;

    @Context(context = { Context.REST })
    @DataDriven(name = "nexusArtifactChecksumData")
    @Test(groups = { "Functional, Heartbeat", "vAPP", "Physical" })
    @TestId(id = "CIP-5680_Func_7", title = "Verifies the checksum of an artifact is not corrupted")
    public void testArtifactChecksum(
            @Input("nexusBaseDirectory") String nexusBaseDirectory,
            @Input("group") String group, @Input("artifact") String artifact,
            @Input("version") String version,
            @Input("fileType") String fileType,
            @Input("checksumType") String checksumType,
            @Input("checksumInstance") String checksumInstance,
            @Output("Expected") boolean expected) {
        String nexusHostName = DataHandler.getAttribute(
                "cifwk.hubNexus.hyperText").toString();
        nexusOperator = operatorRegistry.provide(NexusOperator.class);
        boolean actual;
        try {
            actual = nexusOperator.getChecksumMatch(nexusHostName,
                    nexusBaseDirectory, group, artifact, version, fileType,
                    checksumType, checksumInstance);
        } catch (Exception e) {
            if (expected == true) {
                actual = false;
            } else {
                actual = true;
            }
            logger.error(e);
        }

        assertEquals(expected, actual);
    }
}
