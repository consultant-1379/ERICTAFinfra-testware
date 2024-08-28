package com.ericsson.sut.test.operators;

import javax.inject.Singleton;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.http.*;
import com.ericsson.cifwk.taf.tools.http.constants.ContentType;

@Operator(context = Context.REST)
@Singleton
public class GenericRESTOperatorHttp implements GenericRESTOperator {

    String[] parameters;
    HttpResponse response;
    Logger logger = Logger.getLogger(GenericRESTOperatorHttp.class);

    @Override
    public HttpResponse executeREST(String baseUrl, String path, String restParameters, String type) {
        Host host = null;
        try {
            host = DataHandler.getHostByName(baseUrl);
        } catch (Exception error) {
            logger.debug("BaseUrl: " + baseUrl + " Unable to get Host object");
            error.printStackTrace();
        }
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false)
                .trustSslCertificates(true).build();

        String url = "/" + path;

        if (type.equals("POST")) {
            RequestBuilder request = tool.request().header("Accept", "*/*")
                    .contentType(ContentType.APPLICATION_FORM_URLENCODED);
            if (restParameters != null) {
                restParameters = restParameters.replaceAll("'", "\"");
                request.body(restParameters);
            }
            response = request.post(url);
        } else {
            RequestBuilder request = tool.request().header("Accept", "*/*")
                    .contentType(ContentType.APPLICATION_XML);
            if (restParameters != null) {
                parameters = restParameters.split(",");
                for (String parameter : parameters) {
                    String[] split = parameter.split("=");
                    request.queryParam(split[0], split[1]);
                }
            }
            response = request.get(url);
        }
        return response;
    }

    @Override
    public HttpResponse executeRESTPOST(String baseUrl, String path,
            String dataBody, String type) {
        Host host = null;
        try {
            host = DataHandler.getHostByName(baseUrl);
        } catch (Exception error) {
            logger.debug("BaseUrl: " + baseUrl + " Unable to get Host object");
            error.printStackTrace();
        }
        HttpTool tool = HttpToolBuilder.newBuilder(host)
                .useHttpsIfProvided(false).
                trustSslCertificates(true).
                build();

        String url = "/" + path;
        RequestBuilder request = tool.request().header("Accept", "*/*").contentType(ContentType.APPLICATION_FORM_URLENCODED);
        
        dataBody = dataBody.replaceAll("'", "\"") ;
              
        request.body(dataBody) ;
        
        response = request.post(url);
        return response;

    }
    @Override
    public HttpResponse executeRESTPOST(String baseUrl, String path,
            String dataBody, String type, int timeout) {
        Host host = null;
        try {
            host = DataHandler.getHostByName(baseUrl);
        } catch (Exception error) {
            logger.debug("BaseUrl: " + baseUrl + " Unable to get Host object");
            error.printStackTrace();
        }
        HttpTool tool = HttpToolBuilder.newBuilder(host)
                .useHttpsIfProvided(false).
                trustSslCertificates(true).
                timeout(timeout).
                build();

        String url = "/" + path;
        RequestBuilder request = tool.request().header("Accept", "*/*").contentType(ContentType.APPLICATION_FORM_URLENCODED);
        dataBody = dataBody.replaceAll("'", "\"");
        request.body(dataBody);
        response = request.post(url);
        return response;
    }


    @Override
    public HttpResponse executeRESTwithJSONBody(String baseUrl, String path, String type,
            String jsonBody) {
        Host host = null;
        try {
            host = DataHandler.getHostByName(baseUrl);
        } catch (Exception error) {
            logger.debug("BaseUrl: " + baseUrl + " Unable to get Host object");
            error.printStackTrace();
        }
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false).trustSslCertificates(true).build();
        String url = "/" + path;

        RequestBuilder request = tool.request().header("Accept", "application/json")
                .contentType(ContentType.APPLICATION_JSON).body(jsonBody);

        if (type == "GET") {
            response = request.get(url);
        } else {
            response = request.post(url);
        }
        return response;
    }

    @Override
    public HttpResponse executeRESTWithTimeOut(String baseUrl, String path, String restParameters,
            String type, int timeOut) {
        Host host = null;
        try {
            host = DataHandler.getHostByName(baseUrl);
        } catch (Exception error) {
            logger.debug("BaseUrl: " + baseUrl + " Unable to get Host object");
            error.printStackTrace();
        }
        HttpTool tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(false)
                .trustSslCertificates(true).build();

        String url = "/" + path;

        if (type.equals("POST")) {
            RequestBuilder request = tool.request().header("Accept", "*/*")
                    .contentType(ContentType.APPLICATION_FORM_URLENCODED).timeout(timeOut);
            if (restParameters != null) {
                restParameters = restParameters.replaceAll("'", "\"");
                request.body(restParameters);
            }
            response = request.post(url);
        } else {
            RequestBuilder request = tool.request().header("Accept", "application/xml")
                    .contentType(ContentType.APPLICATION_XML).timeout(timeOut);
            if (restParameters != null) {
                parameters = restParameters.split(",");
                for (String parameter : parameters) {
                    String[] split = parameter.split("=");
                    request.queryParam(split[0], split[1]);
                }
            }
            response = request.get(url);
        }
        return response;
    }
}
