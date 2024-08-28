package com.ericsson.sut.test.operators;

/**
 * GitOperator is a Git TAF Operator interface.
 * 
 */
public interface GitOperator {
    public static final String GIT_SHH = "git clone ssh://";
    public static final String GERRIT_PORT = "29418";
    public static final String GERRIT_LIST_PROJECTS = "gerrit ls-projects";
    public static final String GERRIT_VERSION = "gerrit version";
    public static final String SSH = "ssh";
    public static final String SSH_URL = "ssh://";
    public static final String SSH_PORT = "ssh -p ";
    public static final String GIT_PUSH_REVIEW = "git push origin HEAD:refs/for/master";
    public static final String GIT_RESET_HARD = "git reset --hard ";
    public static final String GIT_COMMIT = "git commit -m 'autodeployment update'";
    public static final String GIT_ADD = "git add .";
    public static final String GIT_PUSH_LOCAL = "git push --mirror origin";
    public static final String GIT_REMOTE_SET_URL_ORIGIN = "git remote set-url origin";
    public static final String GIT_GERRIT_CREATE_PROJECT = "-p 29418 gerrit create-project -n";
    public static final String GIT_ALL_PROJECTS = "-p All-Projects";
    public static final String GIT_HOOK_PORT = "gitdir=$(git rev-parse --git-dir); scp -p -P 29418";
    public static final String GIT_HOOK_COMMIT = ":hooks/commit-msg ${gitdir}/hooks/";
    public static final String GIT_COMMIT_SHA = "gitcommit=$(git log | head -1 | awk '{print $2}')";

    /**
     * 
     * This creates the clone command to be executed according to the data
     * received
     * 
     * @param targetLocation
     *            The Location where the repo will be clone to on the server
     * @param projectName
     *            The project name to be cloned
     * @return cloneCmd The full command to perform the clone.
     */
    public String setClone(String targetLocation, String projectName);

    /**
     * 
     * This creates the command to set the project to a specific SHA
     * 
     * @param sha
     *            The sha which the project will be set to
     * @return shaCmd The full command to set the SHA.
     */
    public String setSha(String sha);

    /**
     * 
     * This creates the command to push the changes for review
     * 
     * @return createPushCmd The full command to push the changes.
     */
    public String pushLocal();

    /**
     * 
     * This creates the command to set the url (fetch/push) to a specific gerrit
     * e.g Gerrit Central to vAPP gerrit local
     * 
     * @param repo
     *            The repo name
     * @return createRemoteCmd The full command to set the url to a specific
     *         gerrit.
     */
    public String setRemoteUrl(String repo);

    /**
     * 
     * This creates the command to push the project to the appropriate gerrit
     * e.g push all content of project to vAPP Gerrit local
     * 
     * @param repo
     *            The repo name
     * @return createLocalCmd The full command to push the project to a specific
     *         gerrit.
     */
    public String createLocalProject(String repo);

    /**
     * 
     * This creates the command to push the project to the appropriate gerrit
     * e.g push all content of project to vAPP Gerrit local
     * 
     * @return createHookCmd The full command to push the project to a specific
     *         gerrit.
     */
    public String createHookCmd();
}
