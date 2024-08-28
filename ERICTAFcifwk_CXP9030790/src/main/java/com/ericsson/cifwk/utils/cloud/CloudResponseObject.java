package com.ericsson.cifwk.utils.cloud;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.utils.XMLHandler;

/**
 * CloudResponseHandler is a Handler that to help wrap logic of retrieving
 * searching through Cloud REST call responses so that testcases can be made
 * shorter
 * 
 */

public class CloudResponseObject {
    XMLHandler xmlHandler = new XMLHandler();
    private HttpResponse httpResponse;
    private Document document;

    public CloudResponseObject(HttpResponse inputResponse) {
        httpResponse = inputResponse;
        document = XMLHandler.createDocument(inputResponse.getBody());
    }

    public String getResponseCode() {
        return httpResponse.getResponseCode().toString();
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public String getValueBySibling(String parentTagName,
            String searchNodeName, String searchNodeValue,
            String requiredNodeName) {
        return xmlHandler.getValueBySibling(document, parentTagName, searchNodeName, searchNodeValue, requiredNodeName);
    }

    public List<Map<String, String>> getSimpleValues(String parentTagName) {
        return xmlHandler.getSimpleValues(document, parentTagName);
    }

    public boolean doesTagExist(String tagName) {
        return xmlHandler.doesTagExist(document, tagName);
    }
}