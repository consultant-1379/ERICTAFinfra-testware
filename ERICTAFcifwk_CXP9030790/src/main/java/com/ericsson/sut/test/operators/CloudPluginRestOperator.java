package com.ericsson.sut.test.operators;

import org.apache.log4j.Logger;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.http.*;
import com.ericsson.cifwk.taf.tools.http.constants.ContentType;
/**
 * JenkinsRESTOperator is a Jenkins TAF Operator class, which allows users to
 * perform various actions using Jenkins API REST calls. The purpose of
 * JenkinsRESTOperator is to separate generic Jenkins use cases into one TAF
 * operator that can be called by test cases.
 * 
 * 
 * @see CloudPluginRestOperator
 */

@Operator(context = Context.REST)
public class CloudPluginRestOperator implements CloudPluginOperator {

    private Host jenkinsHost;
    private HttpTool tool;
    private String jenkinsURLPost;
    private HttpResponse response;

    Logger logger = Logger.getLogger(CloudPluginRestOperator.class);

    @Override
    public HttpResponse deleteJenkinsSlave(String jenkinsHostName,
                                            String jenkinsBaseDirectory,
                                            String slaveName,
                                            int timeout) {
        jenkinsHost = DataHandler.getHostByName(jenkinsHostName.toString());
        tool = HttpToolBuilder.newBuilder(jenkinsHost).build();
        jenkinsURLPost = "/" + jenkinsBaseDirectory + "/computer/" + slaveName + "/doDelete";
        response = tool.request().authenticate(jenkinsHost.getUser(), jenkinsHost.getPass()).timeout(timeout).header("Accept", "application/xml").contentType(ContentType.APPLICATION_XML).post(jenkinsURLPost);
        return response;
    }

    @Override
    public HttpResponse disconnectJenkinsSlave(String jenkinsHostName,
                                            String jenkinsBaseDirectory,
                                            String slaveName,
                                            int timeout) {
        jenkinsHost = DataHandler.getHostByName(jenkinsHostName.toString());
        tool = HttpToolBuilder.newBuilder(jenkinsHost).build();
        jenkinsURLPost = "/" + jenkinsBaseDirectory + "/computer/" + slaveName + "/doDisconnect";
        response = tool.request().authenticate(jenkinsHost.getUser(), jenkinsHost.getPass()).timeout(timeout).header("Accept", "application/xml").contentType(ContentType.APPLICATION_XML).post(jenkinsURLPost);
        return response;
    }
}
