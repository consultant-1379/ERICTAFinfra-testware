package com.ericsson.cifwk.teststeps;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.TestContext;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestStep;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.utils.FileHandling;
import com.ericsson.cifwk.utils.SutHost;

import com.ericsson.cifwk.utils.TestEnvironmentDetails;
import com.ericsson.sut.test.operators.CliCommandHelperCliOperator;
import com.ericsson.sut.test.operators.GenericRESTOperatorHttp;
import com.google.gson.*;

import org.apache.log4j.Logger;

public class DMTTestSteps extends TorTestCaseHelper {

    public static final String SED_SETUP = "CIP-8082_Func_1.0";
    public static final String VERIFY_HOST_PROPERTIES = "CIP-8082_Func_1.1";

    @Inject
    CliCommandHelperCliOperator cmdHelper;
    private Host host = null;
    final String commandPropertyPrefix = "ciPortalJenkins.";
    private Map<String, Object> commandResults;
    private RemoteFileHandler remote = null;
    Logger logger = Logger.getLogger(ISOBuildTestSteps.class);

    @Inject
    TestContext context;

    @TestStep(id = SED_SETUP)
    public void uploadSed(@Input("hostname") String hostName,
            @Input("command") String command,
            @Input("clusterId") String clusterId,
            @Input("sedVersion") String sedVersion,
            @Input("localFile") String localFile,
            @Input("remoteFile") String remoteFile,
            @Input("remotePath") String remotePath,
            @Input("proto") String proto, @Input("timeout") int timeOut,
            @Output("expectedOut") String expectedOut) {

        host = DataHandler.getHostByName(hostName);
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,
                "recursively.delete", remotePath, timeOut);
        assert ((Integer) commandResults.get("exitValue") == 0);
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,
                "make.dir.p", remotePath, timeOut);
        assert ((Integer) commandResults.get("exitValue") == 0);
        remote = new RemoteFileHandler(host);
        File file = FileHandling.getFileToDeploy(localFile);
        Path FilePath = Paths.get(file.getPath());
        String remoteLocation = remotePath + remoteFile;
        remote.copyLocalFileToRemote(FilePath.toString(), remoteLocation);
        String args = "-X POST -H \"Content-Type:multipart/form-data\" -F \"file=@"+remotePath+remoteFile+"\" --form-string \"clusterId="
                + clusterId
                + "\" --form-String \"sedVersion="
                + sedVersion
                + "\"  "
                + proto
                + "://"
                + host.getIp()
                + "/dmt/upload/preLoadedSed/";
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host,
                command, args, timeOut);
        assertEquals(true,
                commandResults.get("stdOut").toString().contains(expectedOut));
    }

    @Inject
    GenericRESTOperatorHttp restHTTPOperator;

    @TestStep(id = VERIFY_HOST_PROPERTIES)
    public void verifyHostProperties(@Input("Id") String Id,
            @Input("Description") String testCaseDescription,
            @Input("HostName") String hostName,
            @Input("baseURL") String baseURL,
            @Input("RestGetParameters") String restGetParameters,
            @Output("ExpectedOutput") String expectedOutput

    ) {
        HttpResponse response = restHTTPOperator.executeREST(hostName, baseURL,
                restGetParameters, "GET");
        String sutResource = response.getBody();
        JsonArray jsonArray;
        JsonParser parser = new JsonParser();
        jsonArray = (JsonArray) parser.parse(response.getBody());
        JsonObject jsonObject = (JsonObject) jsonArray.get(0);
        SutHost[] sutHosts = extractSutHosts(sutResource);
        
        SutHost ms1Host = getSutHostByHostName(sutHosts, "ms1");
        SutHost uiHost = getSutHostByHostName(sutHosts, "mscm_1");
        SutHost eventbasedclient1 = getSutHostByHostName(sutHosts,
                "eventbasedclient_1");
        
        uiHost = getSutHostInterfaceByType(uiHost.getInterfaces(), "public");
        eventbasedclient1 = getSutHostInterfaceByType(
                eventbasedclient1.getInterfaces(), "internal");
        TestEnvironmentDetails result = new TestEnvironmentDetails();
        if (ms1Host != null) {
            result.setLmsHostIpAddress(ms1Host.getIp());
        }
        if (uiHost != null) {
            if (uiHost.getIp() != null) {
                result.setUiHostIpAddress(uiHost.getIp());
            } else {
                result.setUiHostIpAddress(uiHost.getIpv4());
            }
        }
        if (eventbasedclient1 != null) {
            if (eventbasedclient1.getIp() != null) {
                result.eventbasedclient1IpAddress(eventbasedclient1.getIp());
            } else {
                result.eventbasedclient1IpAddress(eventbasedclient1.getIpv4());
            }
        }

        String resultsString = "http:" + result.getUiHostIpAddress()
                + ",event:" + result.geteventbasedclient1IpAddress();
        assertEquals(true, resultsString.equals(expectedOutput));

    }

    SutHost[] extractSutHosts(String sutResource) {
        return new Gson().fromJson(sutResource, SutHost[].class);
    }

    SutHost getSutHostByHostName(SutHost[] sutHosts, final String hostName) {
        for (SutHost sutHost : sutHosts) {
            SutHost[] nodes = sutHost.getNodes();
            if (hostName.equalsIgnoreCase(sutHost.getHostname())) {
                return sutHost;
            } else if (nodes != null && nodes.length > 0) {
                return getSutHostByHostName(nodes, hostName);
            }
        }
        return null;
    }

    SutHost getSutHostByType(SutHost[] sutHosts, final String type) {
        for (SutHost sutHost : sutHosts) {
            SutHost[] nodes = sutHost.getNodes();
            if (type.equalsIgnoreCase(sutHost.getType())) {
                return sutHost;
            } else if (nodes != null && nodes.length > 0) {
                return getSutHostByType(nodes, type);
            }
        }
        return null;
    }

    SutHost getSutHostInterfaceByType(SutHost[] hostInterfaces,
            final String type) {
        for (SutHost hostInterface : hostInterfaces) {
            if (type.equalsIgnoreCase(hostInterface.getType())) {
                return hostInterface;
            }
        }
        return null;
    }

}
