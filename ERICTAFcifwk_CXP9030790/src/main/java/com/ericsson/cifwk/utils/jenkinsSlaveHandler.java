package com.ericsson.cifwk.utils;

import java.util.ArrayList;

import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.sut.test.operators.JenkinsOperator;
import com.google.inject.Inject;

/**
 * his class is used to help dealing with the list of Slaves that are in Jenkins
 * and helps to access them between test cases
 */
public class jenkinsSlaveHandler {
    private static ArrayList<String> currentJenkinsSlaves = new ArrayList<String>();

    private static ArrayList<String> jenkinsSlaveHistory = new ArrayList<String>();

    @Inject
    OperatorRegistry<JenkinsOperator> operatorRegistry;

    /**
     * Function to get list of currentJenkinsSlaves
     * 
     * @return list of current jenkins slaves
     */
    public static ArrayList<String> getCurrentJenkinsSlaves() {
        return currentJenkinsSlaves;
    }

    /**
     * Function to set list of current jenkins slaves
     * 
     * @param slaves
     *            list of slaves to set our currentJenkinsSlaves variable to
     */
    public static void setCurrentJenkinsSlaves(ArrayList<String> slaves) {
        currentJenkinsSlaves = new ArrayList<String>(slaves) ;
    }

    /**
     * Function to a slave string to our list containing all names of previous
     * jenkins slaves
     * 
     * @param slave
     *            String containing name of slave to add to history
     */
    public static void addToJenkinsSlaveHistory(String slave) {
        jenkinsSlaveHistory.add(slave);
    }

    /**
     * Function to return list of strings containing names of all previous
     * slaves
     * 
     * @return list of jenkinsSlaveHistory
     */
    public static ArrayList<String> getJenkinsSlaveHistory() {
        return jenkinsSlaveHistory;
    }

    /**
     * Function to return number of jenkins slaves in list
     * 
     * @return integer number of slaves
     */
    public static int getTotalJenkinsSlaves() {
        return currentJenkinsSlaves.size();
    }

    /**
     * Function to compare a list of strings with our currentJenkinsSlaves list
     * 
     * @param slaves
     *            list to be compared with
     * @return return true if lists are the same or false otherwise
     */
    public static boolean compareSlaveLists(ArrayList<String> slaves) {
        if (currentJenkinsSlaves.size() > 0 && currentJenkinsSlaves.size() == slaves.size()) {
            for (String slave : currentJenkinsSlaves) {
                if (!slaves.contains(slave)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Function to check if a slave is in our currentJenkinsSlaves list
     * 
     * @param slave
     *            string to check
     * @return returns true if string is in our list
     */
    public static boolean checkIfSlaveExists(String slave) {
        if (currentJenkinsSlaves.contains(slave)) {
            return true;
        }
        return false;
    }

    /**
     * Function to parse a block of text for the string content between two
     * strings
     * 
     * @param text
     *            block of text to be parsed
     * @param startString
     *            start index for returned string
     * @param endString
     *            end index for returned string
     * @return string between two given strings
     */
    public static String parseTextForExpectedString(String text, String startString, String endString) {
        String result = "";
        String lines[] = text.split("\n");
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].contains(startString)) {
                int startIndex = lines[i].indexOf(startString) + startString.length();
                int endIndex;
                if (lines[i].contains(endString)) {
                    endIndex = lines[i].indexOf(endString, startIndex);

                } else {
                    endIndex = lines[i].length();
                }
                result = lines[i].substring(startIndex, endIndex);
                break;
            }
        }
        return result;
    }
}
