package com.ericsson.cifwk.test.cases;

import java.io.IOException;
import java.util.Map;

import org.testng.annotations.Test;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.cli.TimeoutException;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.sut.test.operators.CliCommandHelperOperator;
import com.ericsson.sut.test.operators.GitCliOperator;
import com.ericsson.sut.test.operators.GitOperator;
import com.google.inject.Inject;

public class GitCommands extends TorTestCaseHelper implements TestCase {
    @Inject
    OperatorRegistry<CliCommandHelperOperator> cliToolProvider;
    OperatorRegistry<GitOperator> operatorRegistry;

    @TestId(id = "CIP-5679-Funct-1", title = "Verify the clone of a git repo")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP", "Physical" })
    @DataDriven(name = "gitCommands")
    public void cloneRepo(@Input("host") String hostname,
            @Input("projectName") String projectName,
            @Input("repoDirectory") String repoDirectory,
            @Input("sha") String sha,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) {
        GitCliOperator gitCliOperator = new GitCliOperator();
        setTestStep("Getting Command For Execution");
        String cloneCmd = gitCliOperator.setClone(repoDirectory, projectName);
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command : " + cloneCmd);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, cloneCmd, timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"),assertValue);
    }

    @TestId(id = "CIP-5679-Funct-2", title = "Verify the setting of a SHA in a git repo")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP", "Physical" })
    @DataDriven(name = "gitCommands")
    public void setRepoSha(@Input("host") String hostname,
            @Input("projectName") String projectName,
            @Input("repoDirectory") String repoDirectory,
            @Input("sha") String sha,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) {
        GitCliOperator gitCliOperator = new GitCliOperator();
        setTestStep("Getting Command For Execution");
        String shaCmd = gitCliOperator.setSha(sha);
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command : cd " + repoDirectory + "; " + shaCmd);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "cd " + repoDirectory + "; " + shaCmd);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"), assertValue);
    }

    @TestId(id = "CIP-5679-Funct-3", title = "Verify the creation of a new Project on Gerrit")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP" })
    @DataDriven(name = "gitCommands")
    public void populateProjectToGerrit(@Input("host") String hostname,
            @Input("projectName") String projectName,
            @Input("repoDirectory") String repoDirectory,
            @Input("sha") String sha,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) {
        GitCliOperator gitCliOperator = new GitCliOperator();
        setTestStep("Getting Command For Execution");
        String createLocalCmd = gitCliOperator.createLocalProject(projectName);
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command : cd " + repoDirectory + "; " + createLocalCmd);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "cd " + repoDirectory + "; " + createLocalCmd, timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"),assertValue);
    }

    @TestId(id = "CIP-5679-Funct-4", title = "Verify the update to the local gerrit")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP" })
    @DataDriven(name = "gitCommands")
    public void setRemoteGerrit(@Input("host") String hostname,
            @Input("projectName") String projectName,
            @Input("repoDirectory") String repoDirectory,
            @Input("sha") String sha,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {

        GitCliOperator gitCliOperator = new GitCliOperator();
        setTestStep("Getting Command For Execution");
        String createRemoteCmd = gitCliOperator.setRemoteUrl(projectName);
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command : cd " + repoDirectory + "; " + createRemoteCmd);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "cd " + repoDirectory + "; " + createRemoteCmd, timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"),assertValue);
    }

    @TestId(id = "CIP-5679-Funct-5", title = "Push Repo to Gerrit Local")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP" })
    @DataDriven(name = "gitCommands")
    public void pushRepoToLocalGerrit(@Input("host") String hostname,
            @Input("projectName") String projectName,
            @Input("repoDirectory") String repoDirectory,
            @Input("sha") String sha,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command : cd " + repoDirectory + "; " + GitOperator.GIT_PUSH_LOCAL);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "cd " + repoDirectory + "; " + GitOperator.GIT_PUSH_LOCAL, timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode != (Integer) commandResults.get("exitValue"),assertValue);
    }

    @TestId(id = "CIP-5679-Funct-6", title = "Adding hook commit msg to the git repo")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP", "Physical" })
    @DataDriven(name = "gitCommands")
    public void addHookRepo(@Input("host") String hostname,
            @Input("projectName") String projectName,
            @Input("repoDirectory") String repoDirectory,
            @Input("sha") String sha,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) {
        GitCliOperator gitCliOperator = new GitCliOperator();
        setTestStep("Getting Command For Execution");
        String hookCmd = gitCliOperator.createHookCmd();
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command : cd " + repoDirectory + "; " + hookCmd);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "cd " + repoDirectory + "; " + hookCmd, 50);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"),assertValue);
    }

    @TestId(id = "CIP-5679-Funct-7", title = "Create a new change for push testing")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP", "Physical" })
    @DataDriven(name = "gitCommands")
    public void createChange(@Input("host") String hostname,
            @Input("projectName") String projectName,
            @Input("repoDirectory") String repoDirectory,
            @Input("sha") String sha,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command : cd " + repoDirectory + "; date +\"%m-%d-%Y:%T\" > foo");
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "cd " + repoDirectory + "; date +\"%m-%d-%Y:%T\" > foo", timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"),assertValue);
    }

    @TestId(id = "CIP-5679-Funct-8", title = "Add the change to Git")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP", "Physical" })
    @DataDriven(name = "gitCommands")
    public void addChange(@Input("host") String hostname,
            @Input("projectName") String projectName,
            @Input("repoDirectory") String repoDirectory,
            @Input("sha") String sha,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command : cd " + repoDirectory + "; " + GitCliOperator.GIT_ADD);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "cd " + repoDirectory + "; " + GitCliOperator.GIT_ADD, timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"),assertValue);
    }

    @TestId(id = "CIP-5679-Funct-9", title = "Commit the Change")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP", "Physical" })
    @DataDriven(name = "gitCommands")
    public void commitChange(@Input("host") String hostname,
            @Input("projectName") String projectName,
            @Input("repoDirectory") String repoDirectory,
            @Input("sha") String sha,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command : cd " + repoDirectory + "; " + GitCliOperator.GIT_COMMIT);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "cd " + repoDirectory + "; " + GitCliOperator.GIT_COMMIT, timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"),assertValue);
    }

    @TestId(id = "CIP-5679-Funct-10", title = "Verify the push for review to gerrit")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP", "Physical" })
    @DataDriven(name = "gitCommands")
    public void pushForReview(@Input("host") String hostname,
            @Input("projectName") String projectName,
            @Input("repoDirectory") String repoDirectory,
            @Input("sha") String sha,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command : cd " + repoDirectory + "; " + GitCliOperator.GIT_PUSH_REVIEW);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "cd " + repoDirectory + "; " + GitCliOperator.GIT_PUSH_REVIEW, timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"),assertValue);
    }

    @TestId(id = "CIP-5679-Funct-11", title = "Verify that Gerrit is alive")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP", "Physical" })
    @DataDriven(name = "gerritCheck")
    public void checkGerrit(@Input("host") String hostname,
            @Input("serverName") String serverName,
            @Input("timeout") int timeout,
            @Input("assertValue") boolean assertValue,
            @Output("exitCode") int expectedExitCode) throws TimeoutException,
            IOException, InterruptedException {
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        String user = DataHandler.getAttribute("cifwk.git.user").toString();
        Host host = DataHandler.getHostByName(hostname);
        StringBuilder serverCheckCmd = new StringBuilder();
        serverCheckCmd.append(GitOperator.SSH_PORT)
                      .append(GitOperator.GERRIT_PORT).append(" ").append(user)
                      .append("@").append(serverName).append(" ")
                      .append(GitOperator.GERRIT_VERSION);
        setTestStep("Execute Command :" + serverCheckCmd.toString());
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, serverCheckCmd.toString());
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"),assertValue);
    }

    @TestId(id = "CIP-5679-Funct-12", title = "Verify the Repo Directory Does not Exist")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP", "Physical" })
    @DataDriven(name = "gitCommands")
    public void checkRepoDirectories(@Input("host") String hostname,
            @Input("projectName") String projectName,
            @Input("repoDirectory") String repoDirectory,
            @Input("sha") String sha,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command :[ -d " + repoDirectory + " ] || echo \"Yes\"");
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "[ -d " + repoDirectory + " ] || echo \"Yes\"");
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"),assertValue);
    }

    @TestId(id = "CIP-5679-Funct-17", title = "Clean down of repo")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP", "Physical" })
    @DataDriven(name = "gitCommands")
    public void cleanClone(@Input("host") String hostname,
            @Input("projectName") String projectName,
            @Input("repoDirectory") String repoDirectory,
            @Input("sha") String sha,
            @Input("assertValue") boolean assertValue,
            @Output("expectedExit") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command :rm -rf " + repoDirectory);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "rm -rf " + repoDirectory, timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"),assertValue);
    }

    @TestId(id = "CIP-5679-Funct-18", title = "Clean down of Gerrit")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP" })
    @DataDriven(name = "gerritClean")
    public void cleanGerrit(@Input("host") String hostname,
            @Input("cleanDirectory") String cleanDirectory,
            @Input("assertValue") boolean assertValue,
            @Output("exitCode") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        setTestStep("Execute Command :rm -rf " + cleanDirectory);
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "rm -rf " + cleanDirectory, timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"),assertValue);
    }

    @TestId(id = "CIP-5679-Funct-19", title = "Flush the cache in Gerrit")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP" })
    @DataDriven(name = "gerritCommands")
    public void flushGerritCache(@Input("flushHost") String hostname,
            @Input("serverName") String serverName,
            @Input("flushCommand") String command,
            @Input("assertValue") boolean assertValue,
            @Output("exitCode") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        StringBuilder flushGerritCmd = new StringBuilder();
        flushGerritCmd.append(GitOperator.SSH_PORT)
                      .append(GitOperator.GERRIT_PORT).append(" ").append(serverName).append(" ").append(command);
        setTestStep("Execute Command :" + flushGerritCmd.toString());
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, flushGerritCmd.toString(), timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"),assertValue);
    }

    @TestId(id = "CIP-5679-Funct-20", title = "Abandon review in Gerrit")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP" })
    @DataDriven(name = "abandonGerritReviews")
    public void abandonReview(@Input("host") String hostname,
            @Input("serverName") String serverName,
            @Input("directory") String directory,
            @Input("command") String command,
            @Input("assertValue") boolean assertValue,
            @Output("exitCode") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        StringBuilder abandonReviewCmd = new StringBuilder();
        abandonReviewCmd.append(GitOperator.GIT_COMMIT_SHA).append(";")
                        .append(GitOperator.SSH_PORT).append(GitOperator.GERRIT_PORT)
                        .append(" ").append(serverName)
                        .append(" ").append(command).append(" $gitcommit");
        setTestStep("Execute Command :" + abandonReviewCmd.toString());
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, "cd " + directory + ";" + abandonReviewCmd.toString(), timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"),assertValue);
    }
    
    @TestId(id = "CIP-5679-Funct-21", title = "Restart Gerrit")
    @Context(context = { Context.CLI })
    @Test(groups = { "Functional", "vAPP" })
    @DataDriven(name = "gerritCommands")
    public void restartGerrit(@Input("restartHost") String hostname,
            @Input("serverName") String serverName,
            @Input("directory") String directory,
            @Input("restartCommand") String command,
            @Input("assertValue") boolean assertValue,
            @Output("exitCode") int expectedExitCode,
            @Input("timeout") int timeout) throws TimeoutException,
            IOException, InterruptedException {
        CliCommandHelperOperator cmdHelper = cliToolProvider.provide(CliCommandHelperOperator.class);
        Host host = DataHandler.getHostByName(hostname);
        StringBuilder flushGerritCmd = new StringBuilder();
        flushGerritCmd.append("cd ")
                      .append(directory).append(";").append(" ").append(command);
        setTestStep("Execute Command :" + flushGerritCmd.toString());
        Map<String, Object> commandResults = cmdHelper.executeCommandAndReturnCommandDetails(host, flushGerritCmd.toString(), timeout);
        setTestStep("Verifying exit value");
        assertEquals(expectedExitCode == (Integer) commandResults.get("exitValue"),assertValue);
    }
}
