package com.ericsson.cifwk.teststeps;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.utils.FileHandling;
import com.ericsson.sut.test.operators.CliCommandHelperCliOperator;
import javax.inject.Inject;
import org.apache.log4j.Logger;

public class ImageBuildTestSteps extends TorTestCaseHelper {

    @Inject
    CliCommandHelperCliOperator cmdHelper;
    private Map<String, Object> commandResults;
    private Host host = DataHandler.getHostByName("gateway");
    private RemoteFileHandler remote = new RemoteFileHandler(host);
    private String command;

    Logger logger = Logger.getLogger(ImageBuildTestSteps.class);

    public static final String SETUP_DIRECTORIES = "CIP-6730_Func_1.1";
    public static final String BASE_BUILD = "CIP-6730_Func_1.2";
    public static final String BASE_RHEL_BUILD = "CIP-6730_Func_1.3";
    public static final String IMAGE_BUILD_AND_VERIFY = "CIP-6730_Func_1.4";

    @TestStep(id = SETUP_DIRECTORIES)
    public void directorySetUp(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("remoteBasePath") String remoteBasePath,
            @Input("remoteRhelBasePath") String remoteRhelBasePath,
            @Input("remoteImagePath") String remoteImagePath,
            @Input("outputDirectory") String outputDirectory,
            @Output("expectedExitCode") int expectedExitCode,
            @Input("timeout") int timeOut)
    {
        command = "sudorecursively.delete";
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, outputDirectory, timeOut);
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));

        command = "recursively.delete";
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, remoteBasePath, timeOut);
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));

        command = "make.dir.p";
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, remoteBasePath, timeOut);
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));

        command = "recursively.delete";
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, remoteRhelBasePath, timeOut);
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));

        command = "make.dir.p";
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, remoteRhelBasePath, timeOut);
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));

        command = "recursively.delete";
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, remoteImagePath, timeOut);
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));

        command = "make.dir.p";
        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, remoteImagePath, timeOut);
        assert (expectedExitCode == (Integer) commandResults.get("exitValue"));
    }

    @TestStep(id = BASE_BUILD)
    public void buildBaseImage(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("remotePom") String remotePom,
            @Input("localBasePom") String localBasePom,
            @Input("remoteBasePath") String remoteBasePath,
            @Input("mavenCmd") String mavenCmd,
            @Output("expectedOut") String expectedOut,
            @Input("timeout") int timeOut)
    {
        File baseFile = FileHandling.getFileToDeploy(localBasePom);
        Path basePomFilePath = Paths.get(baseFile.getPath());
        String remoteBasePomLocation = remoteBasePath + remotePom;
        remote.copyLocalFileToRemote(basePomFilePath.toString(), remoteBasePomLocation);

        command = "mvn";
        String baseArgs;
        baseArgs = mavenCmd + remoteBasePomLocation;

        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, baseArgs, timeOut);
        assertEquals(true, commandResults.get("stdOut").toString().contains(expectedOut));
    }

    @TestStep(id = BASE_RHEL_BUILD)
    public void buildBaseRhelImage(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("remotePom") String remotePom,
            @Input("localRhelBasePom") String localRhelBasePom,
            @Input("remoteRhelBasePath") String remoteRhelBasePath,
            @Input("mavenCmd") String mavenCmd,
            @Input("localRhelBaseConfigCmdFile") String localRhelBaseConfigCmdFile,
            @Input("remoteConfigCmdFile") String remoteConfigCmdFile,
            @Output("expectedOut") String expectedOut,
            @Input("timeout") int timeOut)
    {
        File rhelBaseFile = FileHandling.getFileToDeploy(localRhelBasePom);
        Path rhelBasePomFilePath = Paths.get(rhelBaseFile.getPath());
        String remoteRhelBasePomLocation = remoteRhelBasePath + remotePom;
        remote.copyLocalFileToRemote(rhelBasePomFilePath.toString(), remoteRhelBasePomLocation);

        File rhelCfgFile = FileHandling.getFileToDeploy(localRhelBaseConfigCmdFile);
        Path rhelCfgFilePath = Paths.get(rhelCfgFile.getPath());
        String remoteRhelCfgFileLocation = remoteRhelBasePath + remoteConfigCmdFile;
        remote.copyLocalFileToRemote(rhelCfgFilePath.toString(), remoteRhelCfgFileLocation);

        command = "mvn";
        String rhelBaseArgs;
        rhelBaseArgs = mavenCmd + remoteRhelBasePomLocation;

        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, rhelBaseArgs, timeOut);
        assertEquals(true, commandResults.get("stdOut").toString().contains(expectedOut));
    }

    @TestStep(id = IMAGE_BUILD_AND_VERIFY)
    public void buildImage(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("remotePom") String remotePom,
            @Input("localImagePom") String localImagePom,
            @Input("remoteImagePath") String remoteImagePath,
            @Input("mavenProfileCmd") String mavenProfileCmd,
            @Input("localImageConfigCmdFile") String localImageConfigCmdFile,
            @Input("remoteConfigCmdFile") String remoteConfigCmdFile,
            @Output("expectedOut") String expectedOut,
            @Output("expectedCheck1") String expectedCheck1,
            @Output("expectedCheck2") String expectedCheck2,
            @Output("errorCheck") String errorCheck,
            @Input("timeout") int timeOut)
    {
        File imageFile = FileHandling.getFileToDeploy(localImagePom);
        Path imageFilePath = Paths.get(imageFile.getPath());
        String remoteImagePomLocation = remoteImagePath + remotePom;
        remote.copyLocalFileToRemote(imageFilePath.toString(), remoteImagePomLocation);

        File imageCfgFile = FileHandling.getFileToDeploy(localImageConfigCmdFile);
        Path imageCfgFilePath = Paths.get(imageCfgFile.getPath());
        String remoteImageCfgFileLocation = remoteImagePath + remoteConfigCmdFile;
        remote.copyLocalFileToRemote(imageCfgFilePath.toString(), remoteImageCfgFileLocation);

        command = "mvn";
        String imageArgs;
        imageArgs = mavenProfileCmd + remoteImagePomLocation;

        commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, command, imageArgs, timeOut);
        assertEquals(true, commandResults.get("stdOut").toString().contains(expectedOut));
        assertEquals(true, commandResults.get("stdOut").toString().contains(expectedCheck1));
        assertEquals(true, commandResults.get("stdOut").toString().contains(expectedCheck2));
        assertEquals(false, commandResults.get("stdOut").toString().contains(errorCheck));
    }
}
