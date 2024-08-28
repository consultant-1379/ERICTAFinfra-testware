package com.ericsson.cifwk.utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * XMLHandler is a handler which provides helper functions for working with XML
 * 
 */

public class XMLHandler {

    /**
     * This creates a document object from an input xml string
     * 
     * @param xmlString
     *            This is an xml string to create a document from
     * @return This returns a document object
     */
    public static Document createDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
     * This checks if an element of a particular tag exists in a document
     * 
     * @param document
     *            This is the document to search through
     * @param tagName
     *            This is the xml tag to search
     * @return It returns true if there is an element with that tag name
     */
    public boolean doesTagExist(Document document, String tagName) {
        NodeList nodes = document.getElementsByTagName(tagName);
        return nodes.getLength() > 0;
    }

    /**
     * This function searches through the document given, for a list of objects
     * matching the parent tag. It then finds a child of each parent, to find if
     * a child exists with node and value matching the search name and search
     * value given. If found it returns the value of the node with name matching
     * the required name. If none is found it returns null
     * 
     * @param document
     *            This is the document to search through
     * @param parentTagName
     *            This is the parent tags to search through
     * @param searchNodeName
     *            This is the name of the child tag to match
     * @param searchNodeValue
     *            This is the value of the child tag to match
     * @param requiredNodeName
     *            This is the required tag to be returned if found
     * @return Returns the value of the required node, or null if not found
     */
    public String getValueBySibling(Document document, String parentTagName,
            String searchNodeName, String searchNodeValue,
            String requiredNodeName) {

        NodeList nodes = document.getElementsByTagName(parentTagName);

        int nodelength = nodes.getLength();
        int childrenlength;

        for (int x = 0; x < nodelength; x++) {
            NodeList children = nodes.item(x).getChildNodes();
            childrenlength = children.getLength();

            for (int y = 0; y < childrenlength; y++) {

                if (children.item(y).getNodeName().equals(searchNodeName)) {

                    if (children.item(y).getTextContent().equals(searchNodeValue)) {

                        for (int z = 0; z < childrenlength; z++) {
                            if (children.item(z).getNodeName().equals(requiredNodeName)) {
                                return children.item(z).getTextContent();
                            }
                        }

                        return null;
                    }

                    continue;
                }
            }
        }

        return null;
    }

    /**
     * This function searches through the document given, for a list of objects
     * matching the parent tag. It then returns a list of maps which are name / value pairs for each child element
     *
     * @param document
     *            This is the document to search through
     * @param parentTagName
     *            This is the parent tags to search through
     * @return Returns the name value pairs in a list of maps
     */
    public List<Map<String, String>> getSimpleValues(Document document, String parentTagName) {

        List<Map<String, String>> finalMaps = new ArrayList<Map<String, String>>();
        NodeList nodes = document.getElementsByTagName(parentTagName);

        int nodelength = nodes.getLength();
        int childrenlength;

        for (int x = 0; x < nodelength; x++) {
            NodeList children = nodes.item(x).getChildNodes();
            childrenlength = children.getLength();

            Map <String, String> themap = new HashMap<String, String>();
            for (int y = 0; y < childrenlength; y++) {
                themap.put(children.item(y).getNodeName(), children.item(y).getTextContent());
            }
            finalMaps.add(themap);
        }

        return finalMaps;
    }

    /**
     * The getNodeValue method is a String Representaion of an XML file that is converted into XML and element value is returned
     * getNodeValue for a xml element and return
     * @param element
     * @param xmlFileBody
     * @return
     */
    public static String getNodeValue(String element, String xmlFileBody){
        Document document = createDocument(xmlFileBody);
        NodeList nodes = document.getElementsByTagName(element);
        if (nodes != null){
            String elementValue = nodes.item(0).getTextContent();
            return elementValue;
        }
        return "NOK";
    }
}

