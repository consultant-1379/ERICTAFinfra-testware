package com.ericsson.sut.test.operators;

import java.util.ArrayList;

import com.ericsson.cifwk.taf.tools.http.HttpResponse;

/**
 * JenkinsOperator is a Jenkins TAF Operator interface. JenkinsOperatorInterface
 * is a Jenkins TAF Operator interface.
 */

public interface JenkinsOperator {

    /**
     * createJenkinsJob creates a defined Jenkins Job based on a defined Jenkins
     * Instance:
     *
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param jenkinsJobName
     *            Jenkins Job names translate to the name of the Jenkins Job.
     * @param jenkinsJobConfigurationFile
     *            Jenkins Job Configuartion file, this is the Jenkins JOb
     *            configuration File example: config.xml.
     * @return HttpResponse HttpResponse Object returned to test case.
     */
    HttpResponse createJenkinsJob(String jenkinsHostName,
                                  String jenkinsBaseDirectory,
                                  String jenkinsJobName,
                                  String jenkinsJobConfigurationFile);

    /**
     * buildJenkinsJob builds a defined Jenkins Job based on a defined Jenkins
     * Instance:
     * 
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param jenkinsJobName
     *            Jenkins Job names translate to the name of the Jenkins Job.
     * @return HttpResponse HttpResponse Object returned to test case.
     */
    HttpResponse buildJenkinsJob(String jenkinsHostName,
                                 String jenkinsBaseDirectory,
                                 String jenkinsJobName,
                                 String jenkinsJobParameters);

    /**
     * jenkinsJobStatus gets the Status of a defined Jenkins Job based on a
     * defined Jenkins Instance:
     * 
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param jenkinsJobName
     *            Jenkins Job names translate to the name of the Jenkins Job.
     * @param jenkinsStatusType
     *            Jenkins Status type is defined to get the status of a job
     *            examples of these are: jenkinsStatusTypes: lastBuild,
     *            lastCompletedBuild, lastFailedBuild, lastStableBuild,
     *            lastSuccessfulBuild and lastUnsuccessfulBuild
     * @return HttpResponse HttpResponse Object returned to test case.
     */
    HttpResponse jenkinsJobStatus(String jenkinsHostName,
                                  String jenkinsBaseDirectory,
                                  String jenkinsJobName,
                                  String jenkinsStatusType);

    /**
     * deleteJenkinsJob deletes a defined Jenkins Job based on a defined Jenkins
     * Instance:
     * 
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param jenkinsJobName
     *            Jenkins Job names translate to the name of the Jenkins Job.
     * @return HttpResponse HttpResponse Object returned to test case.
     */
    HttpResponse deleteJenkinsJob(String jenkinsHostName,
                                  String jenkinsBaseDirectory,
                                  String jenkinsJobName);

    /**
     * getJenkinsJobLog gets the Jenkins Build Log of a defined Jenkins Job
     * based on a defined Jenkins Instance:
     * 
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param jenkinsJobName
     *            Jenkins Job names translate to the name of the Jenkins Job.
     * @param jenkinsStatusType
     *            Jenkins Status type is defined to get the status of a job
     *            examples of these are: jenkinsStatusTypes: lastBuild,
     *            lastCompletedBuild, lastFailedBuild, lastStableBuild,
     *            lastSuccessfulBuild and lastUnsuccessfulBuild
     * @return HttpResponse HttpResponse Object returned to test case.
     */
    HttpResponse getJenkinsJobLog(String jenkinsHostName,
                                  String jenkinsBaseDirectory,
                                  String jenkinsJobName,
                                  String jenkinsStatusType);

    /**
     * jenkinsJobQueueStatus gets the Queue Status of a defined Jenkins Job
     * based on a defined Jenkins Instance:
     * 
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param jenkinsJobName
     *            Jenkins Job names translate to the name of the Jenkins Job.
     * @return boolean HttpResponse Object returned to test case.
     */
    boolean jenkinsJobQueueStatus(String jenkinsHostName,
                                  String jenkinsBaseDirectory,
                                  String jenkinsJobName);

    /**
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param jenkinsJobName
     *            Jenkins Job names translate to the name of the Jenkins Job.
     * @return
     */
    boolean deleteJenkinsPendingJobFromQueue(String jenkinsHostName,
                                             String jenkinsBaseDirectory,
                                             String jenkinsJobName);

    /**
     * stopBuildingJenkinsJob aborts a defined Jenkins Job based on a defined
     * Jenkins Instance:
     * 
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param jenkinsJobName
     *            Jenkins Job names translate to the name of the Jenkins Job.
     * @return HttpResponse
     */
    HttpResponse stopBuildingJenkinsJob(String jenkinsHostName,
                                        String jenkinsBaseDirectory,
                                        String jenkinsJobName);

    String getvAppTemplateNameFromConsole(String jenkinsHostName,
                                          String jenkinsBaseDirectory,
                                          String jobName,
                                          String jobType);

    /**
     * Function to get the name of a slave that a particular job was built on
     * 
     * @param jenkinsPath
     *            Base jenkins URL from CSV File
     * @param jobName
     *            Name of the job that we are checking
     * @param jobType
     *            Type of job can be job number or lastBuild, lastSucess etc.
     * @return The string containing the name of the slave
     */
    String getJenkinsJobSlaveName(String jenkinsHostName,
                                  String jenkinsBaseDirectory,
                                  String jobName,
                                  String jobType);

    /**
     * Function to return an arraylist of strings that contains all jenkins
     * slaves with the specified label that exist in jenkins.
     * 
     * @param jenkinsPath
     *            URL to our jenkins
     * @param jenkinsLabel
     *            Label filter that we are looking for in jenkins
     * @return Arraylist of all slaves with label in name
     */
    ArrayList<String> getAllJenkinsSlaves(String jenkinsHostName,
                                          String jenkinsBaseDirectory,
                                          String jenkinsLabel,
                                          int timeout);

    /**
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param slaveName
     *            String containing the slaveName we want to check
     * @param timeout
     *            timeout for checking if slave is online
     * @return
     */
    boolean isSlaveOnline(String jenkinsHostName,
                          String jenkinsBaseDirectory,
                          String slaveName,
                          int timeout);

    /**
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param slaveName
     *            String containing the slaveName we want to check
     * @param timeout
     *            timeout for checking if slave is temporarily online
     * @return
     */
    boolean isSlaveTemporarilyOnline(String jenkinsHostName,
                          String jenkinsBaseDirectory,
                          String slaveName,
                          int timeout);

    /**
     * Function to check what the next slave number will be when one of the
     * current slaves is deleted
     * 
     * @param slaveList
     *            List of all the slaves currently existing in jenkins
     * @return An integer which will be the number of the next slave
     */
    int getNextSlaveNumberInt(ArrayList<String> slaveList);

    /**
     * Function to check what the string of the next slave will be when one of
     * the current slaves is deleted
     * 
     * @param slaveList
     *            List of all the slaves currently existing in jenkins
     * @return A string which will be the name of the next slave
     */
    String getNextSlaveNumberString(ArrayList<String> slaveList);

    /**
     * Function to check if a slave with specified number exists in jenkins
     * 
     * @param slaveList
     *            List of all the slaves currently existing in jenkins
     * @param slaveNumber
     *            An integer of the number slave we are checking for
     * @return Boolean which is true if the slave exists
     */
    Boolean checkIfSlaveWithNumberExists(ArrayList<String> slaveList,
                                         int slaveNumber);

    /**
     * Function which will parse a slave name String for the number of the slave
     * it contains
     * 
     * @param slaveName
     *            String containing the slaveName we want to check
     * @return An integer which is the slave number that is contained in the
     *         string
     */
    int getSlaveNumberFromString(String slaveName);

    /**
     * Function which will parse a slave name String for the String without a
     * number on the end
     * 
     * @param slaveName
     *            String containing the slaveName we want to check
     * @return An String which is the slave without the number
     */
    public String getSlavePartStringFromString(String slaveName);

    /**
     * Function to check if slave is gone from jenkins
     * 
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param jenkinsSlaveLabel
     *            Jenkins slave label to check if it's gone
     * @param timeoutGetSlaves
     *            timeout for getting slaves from jenkins
     * @param slaveDeleteTimeout
     *            timeout for deleting slave from jenkins
     * @param deletedJenkinsSlaves
     *            list of slaves we have deleted
     * @return true if slave is gone before slaveDeleteTimeout reached
     */
    boolean anySlaveStillExistsAfterTime(String jenkinsHostName,
                                         String jenkinsBaseDirectory,
                                         String jenkinsSlaveLabel,
                                         int timeoutGetSlaves,
                                         int slaveDeleteTimeout,
                                         ArrayList<String> deletedJenkinsSlaves);

    /**
     * Function to wait for slaves to come back online
     * 
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param jenkinsSlaveLabel
     *            Jenkins slave label to wait for online
     * @param timeoutGetSlaves
     *            timeout for getting slaves from jenkins
     * @param slaveOnlineTimeout
     *            timeout for waiting for slaves to come back online
     * @param deletedJenkinsSlaves
     *            list of slaves we have deleted
     * @return true if the slaves came back online before slaveOnlineTimeout
     *         reached
     */
    boolean waitForSlavesToComeOnline(String jenkinsHostName,
                                      String jenkinsBaseDirectory,
                                      String jenkinsSlaveLabel,
                                      int timeoutGetSlaves,
                                      int slaveOnlineTimeout,
                                      ArrayList<String> deletedJenkinsSlaves);

    /**
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param jobName
     *            Name of jenkins job to parse console
     * @param jobType
     *            type of job e.g job number or lastBuild etc
     * @param startString
     *            string to search for in text
     * @param endString
     *            end search at this string can be end of line e.g \n
     * @return
     */
    String getStringFromConsole(String jenkinsHostName,
                                String jenkinsBaseDirectory,
                                String jobName,
                                String jobType,
                                String startString,
                                String endString);

    /**
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param jobName
     *            Name of jenkins job to parse console
     * @return
     */
    String waitForBuildJobToFinish(String jenkinsHostName,
                                    String jenkinsBaseDirectory,
                                    String jenkinsJobName);

    /**
     * This function returns the name of the slave that last ran this job
     *
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param jobName
     *            Name of jenkins job to parse console
     * @return
     */
    String getLastJobSlaveName(String jenkinsHostName,
            String jenkinsBaseDirectory, String jobName);

    /**
     * @param jenkinsHostName
     *            Jenkins Host Name.
     * @param jenkinsBaseDirectory
     *            Jenkins Base Directory, the translates to the directory that
     *            is appended to the Jenkins URL example:
     *            http://<jenkinsHostName
     *            >:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param jenkinsRestTimeout
     *            The timeout for jenkins commands
     * @param slaves
     *            The list of slaves to online
     * @return
     */
    void onlineSlaves(String jenkinsHostName, String jenkinsBaseDirectory, int jenkinsRestTimeout, ArrayList<String> slaves);
}
