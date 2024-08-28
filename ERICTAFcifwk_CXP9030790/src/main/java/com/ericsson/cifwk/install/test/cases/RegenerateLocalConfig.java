package com.ericsson.cifwk.install.test.cases;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.*;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.utils.FileHandling;
import com.ericsson.sut.test.operators.CliCommandHelperCliOperator;
import com.ericsson.sut.test.operators.CliCommandHelperOperator;
import com.google.inject.Inject;

public class RegenerateLocalConfig extends TorTestCaseHelper implements TestCase {
    public static final String CLIHOST = "ciportal";

    @Inject
    private OperatorRegistry<CliCommandHelperOperator> cliToolProvider;
    private FileHandling fileHandler;
    private Map<String, String> details;
    private Host host = DataHandler.getHostByName(CLIHOST);
    public String localFileCopy = "";

    @TestId(id = "CIP-5872-1", title = "Local Django Configuration file Removed")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "checkLocalConfig")
    public void checkRemoteConfigDoesNtExist(@Input("command") String command,
            @Input("directory") String directory,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) {
        CliCommandHelperCliOperator cmdHelper = (CliCommandHelperCliOperator) cliToolProvider.provide(CliCommandHelperOperator.class);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, directory, timeout);
        setTestStep("Verifying exit value");
        assert (expectedExitCode != (Integer) commandResults.get("exitValue"));
   }

    @TestId(id = "CIP-5872-2a", title = "ReGenerate the Local Django Configuration file")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "regenerateLocalConfig")
    public void regenerateLocalConfig(@Input("id") String Id,
            @Input("localFile") String localFile,
            @Input("localFileCopy") String localFileCopyData,
            @Input("fileType") String fileType,
            @Output("expectedOutFindReplaceInFile") boolean expectedOutFindReplaceInFile) {
        File file1 =  FileHandling.getFileToDeploy(localFile);
        String email = DataHandler.getAttribute("cifwk.delivery.email").toString();
        details = new HashMap<String, String>();
        details.put("_USER_EMAIL_", email);
        fileHandler = new FileHandling();
        setTestStep("Call the findAndReplaceInFile function in the filehandler Object to build up Temp Local Django Config File");
        localFileCopy = fileHandler.copyFindReplaceInFile(file1, localFileCopyData, fileType, details);
        assertEquals(expectedOutFindReplaceInFile, true);
    }

    @TestId(id = "CIP-5872-2b", title = "ReGenerate the Local Django Configuration file")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "regenerateLocalConfig")
    public void copyLocalConfigToRemote(@Input("id") String Id,
            @Input("remoteFile") String remoteFile,
            @Output("expectedOutCopyLocalFileToRemote") boolean expectedOutCopyLocalFileToRemote) {
        setTestStep("Call the RemoveFileHandler to copy the local Config copy to the remote server");
        RemoteFileHandler remote = new RemoteFileHandler(host);
        File file = new File(localFileCopy);
        boolean status = remote.copyLocalFileToRemote(file.getName(), remoteFile, file.getParent());
        assertEquals(expectedOutCopyLocalFileToRemote, status);
    }

    @TestId(id = "CIP-5872-2c", title = "ReGenerate the Local Django Configuration file")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "regenerateLocalConfig")
    public void deleteLocalConfigCopy(@Input("id") String Id,
            @Output("expectedOutFileDeletedResult") boolean expectedOutFileDeletedResult) {
        setTestCase(Id, "ReGenerate the Local Django Configuration file");
        fileHandler = new FileHandling();
        setTestStep("Call the deleteFile function in the FileHandling Object to delete the temp Local Django Config File");
        boolean deleteFile = fileHandler.deleteFile(localFileCopy.toString());
        assertEquals(expectedOutFileDeletedResult, deleteFile);
    }

    @TestId(id = "CIP-5872-3", title = "Local Django Configuration file Exists")
    @Context(context = { Context.CLI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "checkLocalConfig")
    public void checkRemoteConfigExist(@Input("command") String command,
            @Input("directory") String directory,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout){
        CliCommandHelperCliOperator cmdHelper = (CliCommandHelperCliOperator) cliToolProvider.provide(CliCommandHelperOperator.class);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, directory, timeout);
        setTestStep("Verifying exit value");
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
    }
}
