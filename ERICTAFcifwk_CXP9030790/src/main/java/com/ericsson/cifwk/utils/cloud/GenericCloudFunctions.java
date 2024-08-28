package com.ericsson.cifwk.utils.cloud;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.utils.XMLHandler;

public class GenericCloudFunctions {
    /**
     * @param inputParentElementName
     * @param inputChildElementName
     * @param inputGrandchildElementName
     * @param expectedParentResult
     * @param expectedChildResult
     * @param httpResponse
     * @return
     */
    public static boolean[] validatedCloudPortalRestRAAndDataCenterXML(
            String inputParentElementName,
            String inputChildElementName, 
            String inputGrandchildElementName, 
            String expectedParentResult,
            String expectedChildResult,
            HttpResponse httpResponse){

        boolean parentResult = false;
        boolean childResult = false;     
        Document xmlDocument = XMLHandler.createDocument(httpResponse.getBody().toString());
        NodeList nodelist = xmlDocument.getChildNodes();       
        for(int i=0; i<nodelist.getLength(); i++){  
            for(int j=0; j<nodelist.item(i).getChildNodes().getLength(); j++){
                for(int k=0; k<nodelist.item(i).getChildNodes().item(j).getChildNodes().getLength(); k++){
                    for(int l=0; l<nodelist.item(i).getChildNodes().item(j).getChildNodes().item(k).getChildNodes().getLength(); l++){
                        String childElementName = nodelist.item(i).getChildNodes().item(j).getChildNodes().item(k).getNodeName().toString();
                        String grandchildElementName = nodelist.item(i).getChildNodes().item(j).getChildNodes().item(k).getChildNodes().item(l).getNodeName().toString();
                        String grandchildNodeValue = nodelist.item(i).getChildNodes().item(j).getChildNodes().item(k).getChildNodes().item(l).getTextContent().toString(); 
                        if (childElementName.equals(inputParentElementName) && grandchildElementName.equals(inputGrandchildElementName) && grandchildNodeValue.equalsIgnoreCase(expectedParentResult)){                  
                            parentResult = true;
                            continue;
                        }
                        if (childElementName.equals(inputChildElementName) && grandchildElementName.equals(inputGrandchildElementName) && grandchildNodeValue.equalsIgnoreCase(expectedChildResult)){
                            childResult = true;
                            break;
                        }
                    }
                }
            }
        }
        return new boolean[] {parentResult, childResult};
    }

    public static int eventCount(String response) {
        int count = 0;
        Pattern pat = Pattern.compile("\"Event\":");
        Matcher mat = pat.matcher(response);
        while (mat.find()) {
            count++;
        }
        return count;
    }
}
