package com.ericsson.sut.test.operators;

import java.util.ArrayList;

public interface CIPortalOperator {
    /**
     * Starting the Browser for the UI Testcases
     * 
     * @param host
     * @param location
     * @param packageName
     */
    void startBrowser(String host, String location, String packageName);

    /**
     * For Delivery of Package through the UI
     * 
     * @param username
     * @param password
     * @param type
     * @param product
     * @param pkg
     * @param rstate
     * @param email
     * @param drop
     * @param timeOut
     * @return
     */
    String deliver(String username, String password, String type,
            String product, String pkg, String rstate, String email,
            String drop, long timeOut);
    
    /**
     * For Delivery of Media Artifact through the UI
     * 
     * @param username
     * @param password
     * @param drop
     * @param product
     * @param productSet
     * @param rstate
     * @param email
     * @param deliverDrop 
     * @param timeOut
     * @return
     */ 
    
    String deliverMedia(String username, String password, String drop, String product,
            String productSet, String rstate, String email,
            String deliverDrop, long timeOut);
    
    /**
     * For the CLUE Status: need pass in timeout for wait for the UI to display
     * the status
     * 
     * @param timeOut
     * @return
     */
    ArrayList<String> status(long timeOut);

    /**
     * Checking the Results Displaying on the page through UI (CXP Numbers'
     * RStates)
     * 
     * @param username
     * @param password
     * @param product
     * @param pkg
     * @param rstate
     * @param drop
     * @param media
     * @param write
     * @param missing
     * @param timeOut
     * @return
     */
    String checkPRIMresults(String username, String password, String product,
            String pkg, String rstate, String drop, String media, String write,
            String missing, long timeOut);

    /**
     * Checking User Access to the PRIM functions
     * 
     * @param username
     * @param password
     * @param timeOut
     * @return
     */
    String checkPRIMUserAccess(String username, String password, long timeOut);

    /**
     * Using form input to get metrics for Cloud Vapps actions
     * 
     * @param ra
     * @param dataCenter
     * @param startDT
     * @param endDT
     * @param eventType
     * @param resultType
     * @param timeOut
     * @return
     */
    String getCloudMetrics(String ra, String dataCenter, String startDT,
            String endDT, String eventType, String resultType, long timeOut);

    /**
     * Checking that Form is Valid for cloud Metrics
     * 
     * @param ra
     * @param dataCenter
     * @param startDT
     * @param endDT
     * @param eventType
     * @param timeOut
     * @param type
     * @return
     */
    boolean checkCloudMetricsForm(String ra, String dataCenter, String startDT,
            String endDT, String eventType, long timeOut, String type);

    /**
     * Checking Product Set Summary Page
     * 
     * @param tagName
     * @param timeOut
     * @return
     */
    String checkProductSetSummary(String tagName, long timeOut);

    /**
     * Checking Product Set Release Page
     * 
     * @param psRelease
     * @param tagName
     * @param timeOut
     * @return
     */
    String checkProductSetRelease(String psRelease, String tagName, long timeOut);

    /**
     * Checking Product Summary Page
     * 
     * @param tagName
     * @param timeOut
     * @return
     */
    String checkProductSummary(String tagName, long timeOut);

    /**
     * Checking Product Release Page
     * 
     * @param prodRelease
     * @param tagName
     * @param timeOut
     * @return
     */
    String checkProductRelease(String prodRelease, String tagName, long timeOut);

    /**
     * Checking AutoRefresh
     * 
     * @param timeOut
     * @return
     */

    String checkAutoRefresh(String username, long timeOut);

    /**
     * Checking Drop Page Load
     * 
     * @param product
     * @param dropName
     * @param timeOut
     * @return
     */

    String checkDropPageLoad(String product, String dropName,
            long firstTimeOut, long secondTimeOut);

    /**
     * Checking Product Package Page Load
     * 
     * @param product
     * @param timeOut
     * @return
     */

    String checkProductPackagePageLoad(String product, long timeOut);

    /**
     * Checking Testware Page Load
     * 
     * @param product
     * @param timeOut
     * @return
     */

    String checkTestwarePageLoad(String product, long timeOut);
    
 
    /**
     * checking Product Summary Page Form
     * @param username
     * @param password
     * @param productName
     * @param value
     * @param tagName
     * @param header
     * @param timeOut
     * @return
     */
    String checkProductSummaryPageForm(String username, String password, String productName, String value, String tagName, long timeOut, String header);

    /**
     * checking Product Set Summary Page Form
     * @param username
     * @param password
     * @param productSetName
     * @param value
     * @param tagName
     * @param header
     * @param timeOut
     * @return
     */
    String checkProductSetSummaryPageForm(String username, String password, String productSetName, String value, String tagName, long timeOut, String header); 
    

    /**
     * checking Product SummaryPage Form Result
     * @param productName
     * @param values
     * @param action
     * @param defaultDrop 
     * @param header
     * @param timeOut
     * @return
     */
    String checkProductSummaryPageFormResult(String username, String password, String productName, ArrayList<String> values, String action, String defaultDrop, long timeOut, String header);

    /**
     * check Product Set Summary Page Form Result
     * @param productName
     * @param values
     * @param action
     * @param defaultDrop 
     * @param header
     * @param timeOut
     * @return
     */
    String checkProductSetSummaryPageFormResult(String username, String password, String productSetName, ArrayList<String> values, String action, String defaultDrop, long timeOut, String header);
}


