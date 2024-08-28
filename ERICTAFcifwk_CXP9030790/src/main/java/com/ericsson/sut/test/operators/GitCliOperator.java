package com.ericsson.sut.test.operators;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;


@Operator(context = { Context.CLI })
public class GitCliOperator implements GitOperator {
	String forwardSlash = DataHandler.getAttribute("clicommand.slash.forward").toString();

    @Override
    public String setClone(String targetLocation, String projectName) {
        // git clone ssh://USER@gerritmirror.lmera.ericsson.se:29418/projectName
        // targetLocation
        String gerrit = DataHandler.getAttribute("host.gerritCentral.url").toString();
        String user = DataHandler.getAttribute("cifwk.git.user").toString();
        StringBuilder cloneCmd = new StringBuilder();
        cloneCmd.append(GitOperator.GIT_SHH)
                .append(user)
                .append("@")
                .append(gerrit)
                .append(":")
                .append(GitOperator.GERRIT_PORT)
                .append(forwardSlash)
                .append(projectName)
                .append(" ")
                .append(targetLocation);
        return cloneCmd.toString();
    }

    @Override
    public String setSha(String sha) {
        StringBuilder shaCmd = new StringBuilder();
        shaCmd.append(GitOperator.GIT_RESET_HARD)
            .append(sha);
        return shaCmd.toString();
    }

    @Override
    public String pushLocal() {
        // git push origin HEAD:refs/for/master
        String createPushCmd = GitOperator.GIT_PUSH_REVIEW;
        return createPushCmd;
    }

    @Override
    public String setRemoteUrl(String repo) {
        // git remote set-url origin ssh://gerrit1:29418/projectName
        String gerrit = DataHandler.getAttribute("host.gerritLocal.url").toString();
        StringBuilder createRemoteCmd = new StringBuilder();
        createRemoteCmd.append(GitOperator.GIT_REMOTE_SET_URL_ORIGIN + " ")
                .append(GitOperator.SSH_URL).append(gerrit)
                .append(":")
                .append(GitOperator.GERRIT_PORT)
                .append(forwardSlash)
                .append(repo);
        return createRemoteCmd.toString();
    }

    @Override
    public String createLocalProject(String projectName) {
        // ssh gerrit1 -p 29418 gerrit create-project -n
        // OSS/com.ericsson.oss.services/topologySearchService -p All-Projects
        String gerrit = DataHandler.getAttribute("host.gerritLocal.url").toString();
        StringBuilder createLocalCmd = new StringBuilder();
        createLocalCmd.append(GitOperator.SSH + " ")
                .append(gerrit)
                .append(" ")
                .append(GitOperator.GIT_GERRIT_CREATE_PROJECT)
                .append(" ")
                .append(projectName)
                .append(" ")
                .append(GitOperator.GIT_ALL_PROJECTS);
        return createLocalCmd.toString();
    }

    public String createHookCmd() {
        // gitdir=$(git rev-parse --git-dir); scp -p -P 29418
        // emarfah@gerrit1:hooks/commit-msg ${gitdir}/hooks/
        String gerrit = DataHandler.getAttribute("host.gerritLocal.url").toString();
        String user = DataHandler.getAttribute("clicommand.gerrit1.user").toString();
        StringBuilder createHookCmd = new StringBuilder();
        createHookCmd.append(GitOperator.GIT_HOOK_PORT)
                .append(" ")
                .append(user)
                .append("@")
                .append(gerrit)
                .append(GitOperator.GIT_HOOK_COMMIT);
        return createHookCmd.toString();
    }
}
