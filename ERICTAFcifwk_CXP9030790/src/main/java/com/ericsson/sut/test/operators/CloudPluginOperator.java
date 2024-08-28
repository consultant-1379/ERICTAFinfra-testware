package com.ericsson.sut.test.operators;

import com.ericsson.cifwk.taf.tools.http.HttpResponse;

/**
 * JenkinsOperator is a Jenkins TAF Operator interface.
 * JenkinsOperatorInterface is a Jenkins TAF Operator interface.
 */

public interface CloudPluginOperator {

    /**
     * deleteJenkinsSlave deletes a defined Jenkins slave based on a defined Jenkins Instance:
     *
     * @param jenkinsHostName                   Jenkins Host Name.
     * @param jenkinsBaseDirectory              Jenkins Base Directory, the translates to the
     *                                          directory that is appended to the Jenkins URL
     *                                          example: http://<jenkinsHostName>:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param  slaveName                        slave name to be deleted in jenkins
     * @param timeout                           timeout for response
     * @return HttpResponse                     HttpResponse Object returned to test case.
     */
    HttpResponse deleteJenkinsSlave(String jenkinsHostName,
                                  String jenkinsBaseDirectory,
                                  String slaveName,
                                  int timeout);

    /**
     * disconnectJenkinsSlave disconnects a defined Jenkins slave based on a defined Jenkins Instance:
     *
     * @param jenkinsHostName                   Jenkins Host Name.
     * @param jenkinsBaseDirectory              Jenkins Base Directory, the translates to the
     *                                          directory that is appended to the Jenkins URL
     *                                          example: http://<jenkinsHostName>:<jenkinsPort>/<jenkinsBaseDirectory>/...
     * @param  slaveName                        slave name to be disconnected in jenkins
     * @param timeout                           timeout for response
     * @return HttpResponse                     HttpResponse Object returned to test case.
     */
    HttpResponse disconnectJenkinsSlave(String jenkinsHostName, String jenkinsBaseDirectory, String slaveName, int timeout);
}
