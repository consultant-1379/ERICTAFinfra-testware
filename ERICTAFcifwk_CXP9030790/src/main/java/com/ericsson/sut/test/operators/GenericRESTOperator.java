package com.ericsson.sut.test.operators;

import com.ericsson.cifwk.taf.tools.http.HttpResponse;

public interface GenericRESTOperator {

    /**
     * Executing REST Calls GET or POST
     * 
     * @param baseUrl
     * @param path
     * @param restParameters
     * @param type
     * @return
     */
    HttpResponse executeREST(String baseUrl, String path,
            String restParameters, String type);

    /**
     * Executing REST Calls GET or POST
     * 
     * @param baseUrl
     * @param path
     * @param restParameters
     * @param type
     * @return
     */
    HttpResponse executeRESTPOST(String baseUrl, String path,
            String restParameters, String type);

    /**
     * Executing REST Calls GET or POST
     *
     * @param baseUrl
     * @param path
     * @param restParameters
     * @param type
     * @param timeout
     * @return
     */

    HttpResponse executeRESTPOST(String baseUrl, String path,
            String restParameters, String type, int timeout);

    /**
     * Executing REST Calls GET or POST for REST call taken JSON object as body
     * parameter
     * 
     * @param baseUrl
     * @param path
     * @param type
     * @param Body
     * @return
     */

    HttpResponse executeRESTwithJSONBody(String baseUrl, String path,
            String type, String Body);

    /**
     * Executing REST Calls GET or POST with TimeOut
     * 
     * @param baseUrl
     * @param path
     * @param restParameters
     * @param type
     * @return
     */

    HttpResponse executeRESTWithTimeOut(String baseUrl, String path,
            String restParameters, String type, int timeOut);
}
