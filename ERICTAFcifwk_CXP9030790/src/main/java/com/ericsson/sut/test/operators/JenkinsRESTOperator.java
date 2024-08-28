package com.ericsson.sut.test.operators;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.Ports;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.tools.http.HttpTool;
import com.ericsson.cifwk.taf.tools.http.HttpToolBuilder;
import com.ericsson.cifwk.taf.tools.http.RequestBuilder;
import com.ericsson.cifwk.taf.tools.http.constants.ContentType;
import com.ericsson.cifwk.utils.XMLHandler;
import com.ericsson.cifwk.utils.jenkinsSlaveHandler;

/**
 * JenkinsRESTOperator is a Jenkins TAF Operator class, which allows users to
 * perform various actions using Jenkins API REST calls. The purpose of
 * JenkinsRESTOperator is to separate generic Jenkins use cases into one TAF
 * operator that can be called by test cases.
 *
 *
 * @see JenkinsRESTOperator
 */

@Operator(context = Context.REST)
public class JenkinsRESTOperator implements JenkinsOperator {

    private Host jenkinsHost;
    private HttpTool tool;
    private String jenkinsURLPost;
    private HttpResponse response;

    private String[] parameters;

    Logger logger = Logger.getLogger(JenkinsRESTOperator.class);

    @Override
    public HttpResponse createJenkinsJob(String jenkinsHostName,
                                         String jenkinsBaseDirectory,
                                         String jenkinsJobName,
                                         String jenkinsJobConfigurationFileName) {
        File jenkinsJobConfigurationFile = new File(jenkinsJobConfigurationFileName);
        jenkinsHost = DataHandler.getHostByName(jenkinsHostName);
        tool = HttpToolBuilder.newBuilder(jenkinsHost).build();
        jenkinsURLPost = "/" + jenkinsBaseDirectory + "/createItem";
        response = tool.request().authenticate(jenkinsHost.getUser(), jenkinsHost.getPass()).header("Accept", "application/xml").contentType(ContentType.APPLICATION_XML)
                .queryParam("name", jenkinsJobName).file(jenkinsJobConfigurationFile).post(jenkinsURLPost);
        return response;
    }

    @Override
    public HttpResponse buildJenkinsJob(String jenkinsHostName,
                                        String jenkinsBaseDirectory,
                                        String jenkinsJobName,
                                        String jenkinsJobParameters) {
        jenkinsHost = DataHandler.getHostByName(jenkinsHostName);
        tool = HttpToolBuilder.newBuilder(jenkinsHost).build();
        jenkinsURLPost = "/" + jenkinsBaseDirectory + "/job/" + jenkinsJobName + "/build";
        if (jenkinsJobParameters == null) {
            response = tool.request().authenticate(jenkinsHost.getUser(), jenkinsHost.getPass()).header("Accept", "application/xml").contentType(ContentType.APPLICATION_XML).post(jenkinsURLPost);
        } else {
            jenkinsURLPost = "/" + jenkinsBaseDirectory + "/job/" + jenkinsJobName + "/buildWithParameters/";
            RequestBuilder request = tool.request();
            parameters = jenkinsJobParameters.split(",");
            for (String parameter : parameters) {
                String[] split = parameter.split("=");
                request.queryParam(split[0], split[1]);
            }
            response = request.post(jenkinsURLPost);
        }
        return response;

    }

    @Override
    public HttpResponse jenkinsJobStatus(String jenkinsHostName,
                                         String jenkinsBaseDirectory,
                                         String jenkinsJobName,
                                         String jenkinsStatusType) {
        jenkinsHost = DataHandler.getHostByName(jenkinsHostName);
        tool = HttpToolBuilder.newBuilder(jenkinsHost).build();
        jenkinsURLPost = "/" + jenkinsBaseDirectory + "/job/" + jenkinsJobName + "/" + jenkinsStatusType + "/api/xml";
        response = tool.request().authenticate(jenkinsHost.getUser(), jenkinsHost.getPass()).header("Accept", "application/xml").contentType(ContentType.APPLICATION_XML).get(jenkinsURLPost);
        return response;
    }

    @Override
    public HttpResponse deleteJenkinsJob(String jenkinsHostName,
                                         String jenkinsBaseDirectory,
                                         String jenkinsJobName) {
        jenkinsHost = DataHandler.getHostByName(jenkinsHostName);
        tool = HttpToolBuilder.newBuilder(jenkinsHost).build();
        jenkinsURLPost = "/" + jenkinsBaseDirectory + "/job/" + jenkinsJobName + "/doDelete";
        response = tool.request().authenticate(jenkinsHost.getUser(), jenkinsHost.getPass()).header("Accept", "application/xml").contentType(ContentType.APPLICATION_XML).post(jenkinsURLPost);
        return response;
    }

    @Override
    public HttpResponse getJenkinsJobLog(String jenkinsHostName,
                                         String jenkinsBaseDirectory,
                                         String jenkinsJobName,
                                         String jenkinsStatusType) {
        jenkinsHost = DataHandler.getHostByName(jenkinsHostName);
        tool = HttpToolBuilder.newBuilder(jenkinsHost).build();
        jenkinsURLPost = "/" + jenkinsBaseDirectory + "/job/" + jenkinsJobName + "/" + jenkinsStatusType + "/consoleText";
        response = tool.request().authenticate(jenkinsHost.getUser(), jenkinsHost.getPass()).header("Accept", "application/xml").contentType(ContentType.APPLICATION_XML).get(jenkinsURLPost);
        return response;
    }

    @Override
    public boolean jenkinsJobQueueStatus(String jenkinsHostName,
                                         String jenkinsBaseDirectory,
                                         String jenkinsJobName) {
        jenkinsHost = DataHandler.getHostByName(jenkinsHostName);
        tool = HttpToolBuilder.newBuilder(jenkinsHost).build();
        jenkinsURLPost = "/" + jenkinsBaseDirectory + "/job/" + jenkinsJobName + "/api/xml";
        response = tool.request().authenticate(jenkinsHost.getUser(), jenkinsHost.getPass()).header("Accept", "application/xml").contentType(ContentType.APPLICATION_XML).get(jenkinsURLPost);
        if (response.getBody().contains("<inQueue>true</inQueue>")) {
            return true;
        }
        return false;
    }

    @Override
    public String getvAppTemplateNameFromConsole(String jenkinsHostName,
                                                 String jenkinsBaseDirectory,
                                                 String jobName,
                                                 String jobType) {
        String console = getJenkinsJobLog(jenkinsHostName, jenkinsBaseDirectory, jobName, jobType).getBody().toString();
        return jenkinsSlaveHandler.parseTextForExpectedString(console, "vAppTemplate:", " in the cloud");
    }

    @Override
    public String getJenkinsJobSlaveName(String jenkinsHostName,
                                         String jenkinsBaseDirectory,
                                         String jobName,
                                         String jobType) {
        String jenkinsSlavename = null;
        jenkinsHost = DataHandler.getHostByName(jenkinsHostName.toString());
        tool = HttpToolBuilder.newBuilder(jenkinsHost).build();
        String urlToGet = "";
        urlToGet += jenkinsBaseDirectory + "/job/" + jobName + "/" + jobType + "/api/xml?depth=1";
        HttpResponse response = tool.request().authenticate(jenkinsHost.getUser(), jenkinsHost.getPass()).header("Accept", "application/xml").contentType(ContentType.APPLICATION_XML).post(urlToGet);

        Document test = XMLHandler.createDocument(response.getBody().toString());
        NodeList nodes = test.getElementsByTagName("builtOn");
        if (nodes.getLength() == 1) {
            jenkinsSlavename = nodes.item(0).getTextContent();
        }
        return jenkinsSlavename;
    }

    @Override
    public ArrayList<String> getAllJenkinsSlaves(String jenkinsHostName,
                                                 String jenkinsBaseDirectory,
                                                 String jenkinsLabel,
                                                 int timeout) {
        ArrayList<String> jenkinsSlaveName = new ArrayList<String>();
        jenkinsHost = DataHandler.getHostByName(jenkinsHostName);
        tool = HttpToolBuilder.newBuilder(jenkinsHost).build();
        String urlToGet = "";
        urlToGet += jenkinsBaseDirectory + "/computer/api/xml";
        HttpResponse response = tool.request().authenticate(jenkinsHost.getUser(), jenkinsHost.getPass()).timeout(timeout).header("Accept", "application/xml").contentType(ContentType.APPLICATION_XML)
                .post(urlToGet);
        Document test = XMLHandler.createDocument(response.getBody().toString());
        NodeList nodes = test.getElementsByTagName("displayName");
        for (int i = 1; i < nodes.getLength(); i++) {
            String slaveName = nodes.item(i).getTextContent();
            if (slaveName.contains(jenkinsLabel)) {
                jenkinsSlaveName.add(slaveName);
            }
        }
        return jenkinsSlaveName;
    }

    @Override
    public boolean isSlaveOnline(String jenkinsHostName,
                                 String jenkinsBaseDirectory,
                                 String slaveDisplayName,
                                 int timeout) {
        jenkinsHost = DataHandler.getHostByName(jenkinsHostName);
        tool = HttpToolBuilder.newBuilder(jenkinsHost).build();
        String urlToGet = "";
        urlToGet += jenkinsBaseDirectory + "/computer/api/xml";
        HttpResponse response = tool.request().authenticate(jenkinsHost.getUser(), jenkinsHost.getPass()).timeout(timeout).header("Accept", "application/xml").contentType(ContentType.APPLICATION_XML)
                .post(urlToGet);
        XMLHandler xmlHandler = new XMLHandler();
        Document document = XMLHandler.createDocument(response.getBody().toString());
        return xmlHandler.getValueBySibling(document, "computer", "displayName", slaveDisplayName, "offline").equals("true") ? false : true;
    }

    @Override
    public boolean isSlaveTemporarilyOnline(String jenkinsHostName,
                                 String jenkinsBaseDirectory,
                                 String slaveDisplayName,
                                 int timeout) {
        jenkinsHost = DataHandler.getHostByName(jenkinsHostName);
        tool = HttpToolBuilder.newBuilder(jenkinsHost).build();
        String urlToGet = "";
        urlToGet += jenkinsBaseDirectory + "/computer/api/xml";
        HttpResponse response = tool.request().authenticate(jenkinsHost.getUser(), jenkinsHost.getPass()).timeout(timeout).header("Accept", "application/xml").contentType(ContentType.APPLICATION_XML)
                .post(urlToGet);
        XMLHandler xmlHandler = new XMLHandler();
        Document document = XMLHandler.createDocument(response.getBody().toString());
        return xmlHandler.getValueBySibling(document, "computer", "displayName", slaveDisplayName, "temporarilyOffline").equals("true") ? false : true;
    }

    @Override
    public int getNextSlaveNumberInt(ArrayList<String> slaveList) {
        int max_Increment = 0;
        for (int i = 0; i < slaveList.size(); i++) {
            String slaveNumStr = slaveList.get(i);
            int slaveNum = getSlaveNumberFromString(slaveNumStr);
            if (slaveNum > max_Increment) {
                max_Increment = slaveNum;
            }
        }
        return max_Increment + 1;
    }

    @Override
    public String getNextSlaveNumberString(ArrayList<String> slaveList) {
        int max_Increment = 0;
        String maxString = getSlavePartStringFromString(slaveList.get(0));
        for (int i = 0; i < slaveList.size(); i++) {
            String slavePartStr = slaveList.get(i);
            int slaveNum = getSlaveNumberFromString(slavePartStr);
            if (slaveNum > max_Increment) {
                max_Increment = slaveNum;
            }
        }
        return maxString + String.valueOf(max_Increment);
    }

    @Override
    public Boolean checkIfSlaveWithNumberExists(ArrayList<String> slaveList,
                                                int slaveNumber) {
        ArrayList<Integer> slaveNumbers = new ArrayList<>();
        for (int i = 0; i < slaveList.size(); i++) {
            String slaveNumStr = slaveList.get(i);
            int slaveNum = getSlaveNumberFromString(slaveNumStr);
            slaveNumbers.add(slaveNum);
        }
        if (slaveNumbers.contains(slaveNumber)) {
            return true;
        }
        return false;
    }

    @Override
    public int getSlaveNumberFromString(String slaveName) {
        String slaveNumStr = slaveName;
        int startSlaveNum = slaveNumStr.lastIndexOf("_");
        slaveNumStr = slaveNumStr.substring(startSlaveNum + 1, slaveNumStr.length());
        int slaveNum = Integer.parseInt(slaveNumStr);
        return slaveNum;
    }

    @Override
    public String getSlavePartStringFromString(String slaveName) {
        String slavePartStr = slaveName;
        int startSlaveNum = slavePartStr.lastIndexOf("_");
        slavePartStr = slavePartStr.substring(0, startSlaveNum + 1);
        return slavePartStr;
    }

    @Override
    public boolean deleteJenkinsPendingJobFromQueue(String jenkinsHostName,
                                                    String jenkinsBaseDirectory,
                                                    String jenkinsJobName) {
        jenkinsHost = DataHandler.getHostByName(jenkinsHostName.toString());
        tool = HttpToolBuilder.newBuilder(jenkinsHost).build();

        String urlToGet = "";
        urlToGet += jenkinsBaseDirectory + "/job/" + jenkinsJobName + "/api/xml";
        HttpResponse response = tool.request().authenticate(jenkinsHost.getUser(), jenkinsHost.getPass()).header("Accept", "application/xml").contentType(ContentType.APPLICATION_XML).post(urlToGet);

        if (response.getBody().contains("inQueue")) {
            Document test = XMLHandler.createDocument(response.getBody().toString());
            NodeList nodes = test.getElementsByTagName("inQueue");

            String value = "";
            if (nodes.getLength() == 1) {
                value = nodes.item(0).getTextContent();
            }
            if (value.equals("true")) {
                nodes = test.getElementsByTagName("queueItem");
                String id = "";
                if (nodes.getLength() == 1) {
                    Element element = (Element) nodes.item(0);
                    NodeList idEle = element.getElementsByTagName("id");
                    id = idEle.item(0).getTextContent();
                    urlToGet = "";
                    urlToGet += jenkinsBaseDirectory + "/queue/cancelItem?id=" + id;
                    response = tool.request().authenticate(jenkinsHost.getUser(), jenkinsHost.getPass()).header("Accept", "application/xml").contentType(ContentType.APPLICATION_XML).post(urlToGet);
                    if (response.getResponseCode().toString().equals("MOVED_TEMPORARILY") || response.getResponseCode().toString().equals("OK")) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    @Override
    public HttpResponse stopBuildingJenkinsJob(String jenkinsHostName,
                                               String jenkinsBaseDirectory,
                                               String jenkinsJobName) {
        jenkinsHost = DataHandler.getHostByName(jenkinsHostName);
        tool = HttpToolBuilder.newBuilder(jenkinsHost).build();

        jenkinsURLPost = "/" + jenkinsBaseDirectory + "/job/" + jenkinsJobName + "/lastBuild/stop";
        response = tool.request().authenticate(jenkinsHost.getUser(), jenkinsHost.getPass()).header("Accept", "application/xml").contentType(ContentType.APPLICATION_XML).post(jenkinsURLPost);
        return response;
    }

    @Override
    public boolean anySlaveStillExistsAfterTime(String jenkinsHostName,
                                                String jenkinsBaseDirectory,
                                                String jenkinsSlaveLabel,
                                                int timeoutGetSlaves,
                                                int slaveDeleteTimeout,
                                                ArrayList<String> deletedJenkinsSlaves) {

        ArrayList<String> jenkinsSlaveList = new ArrayList<String>();
        boolean foundSlave = false;
        for (int i = 1; i <= (int) (slaveDeleteTimeout / 10000); i++) {
            foundSlave = false;
            jenkinsSlaveList = getAllJenkinsSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, timeoutGetSlaves);
            jenkinsSlaveHandler.setCurrentJenkinsSlaves(jenkinsSlaveList);
            for (String slave : deletedJenkinsSlaves) {
                if (jenkinsSlaveHandler.checkIfSlaveExists(slave)) {
                    foundSlave = true;
                    break;
                }
            }
            if (!foundSlave) {
                return false;
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean waitForSlavesToComeOnline(String jenkinsHostName,
                                             String jenkinsBaseDirectory,
                                             String jenkinsSlaveLabel,
                                             int timeoutGetSlaves,
                                             int slaveOnlineTimeout,
                                             ArrayList<String> deletedJenkinsSlaves) {
        boolean allSlavesOnline = false;
        ArrayList<String> jenkinsSlaveList = jenkinsSlaveHandler.getCurrentJenkinsSlaves();
        boolean foundSlaveStillOfflineJenkins = false;
        for (int i = 1; i <= (int) (slaveOnlineTimeout / 1000); i++) {
            foundSlaveStillOfflineJenkins = false;
            jenkinsSlaveList = getAllJenkinsSlaves(jenkinsHostName, jenkinsBaseDirectory, jenkinsSlaveLabel, timeoutGetSlaves);
            for (String slave : jenkinsSlaveList) {
                if (!isSlaveOnline(jenkinsHostName, jenkinsBaseDirectory, slave, 100000)) {
                    foundSlaveStillOfflineJenkins = true;
                    break;
                }
            }
            if (!foundSlaveStillOfflineJenkins) {
                allSlavesOnline = true;
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return allSlavesOnline;
    }

    @Override
    public void onlineSlaves(String jenkinsHostName,
                                             String jenkinsBaseDirectory,
                                             int jenkinsRestTimeout,
                                             ArrayList<String> slaves) {

        Host jenkinsHost = DataHandler.getHostByName(jenkinsHostName);
        String path = "http://" + jenkinsHost.getIp() + ":" + jenkinsHost.getPort(Ports.HTTP).toString() + "/" + jenkinsBaseDirectory;
        for (String slave : slaves) {
            if (!isSlaveOnline(jenkinsHostName, jenkinsBaseDirectory, slave, jenkinsRestTimeout))
            {
                if (!isSlaveTemporarilyOnline(jenkinsHostName, jenkinsBaseDirectory, slave, jenkinsRestTimeout))
                {
                    String param = "url" + "=" + path + "/computer/" + slave + "/toggleOffline";
                    buildJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, "ReconnectSlaves", param);
                    String onlineJobResult = waitForBuildJobToFinish(jenkinsHostName, jenkinsBaseDirectory, "ReconnectSlaves");
                }

                String param2 = "url" + "=" + path + "/computer/" + slave + "/launchSlaveAgent?json=%7B%7D&Submit=Launch+slave+agent";
                buildJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, "ReconnectSlaves", param2);
                String launchJobResult = waitForBuildJobToFinish(jenkinsHostName, jenkinsBaseDirectory, "ReconnectSlaves");
            }
        }
    }

    //
    public String getStringFromConsole(String jenkinsHostName,
                                       String jenkinsBaseDirectory,
                                       String jobName,
                                       String jobType,
                                       String startString,
                                       String endString) {
        String console = getJenkinsJobLog(jenkinsHostName, jenkinsBaseDirectory, jobName, jobType).getBody().toString();
        return jenkinsSlaveHandler.parseTextForExpectedString(console, startString, endString);
    }

    @Override
    public String getLastJobSlaveName(String jenkinsHostName,
            String jenkinsBaseDirectory,
            String jobName) {
        return getStringFromConsole(jenkinsHostName, jenkinsBaseDirectory, jobName, "lastBuild", "Building remotely on ", " (");
    }

    @Override
    public String waitForBuildJobToFinish(String jenkinsHostName,
                                           String jenkinsBaseDirectory,
                                           String jenkinsJobName) {
        int counter = 0;
        int sleepTime = 1000;
        int defaultEstimatedDuration = 300 * 1000;
        int estimatedDuration = 0;
        while (XMLHandler.getNodeValue("building", jenkinsJobStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, "lastBuild").getBody()).equalsIgnoreCase("true")) {
            response = jenkinsJobStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, "lastBuild");
            try {
                estimatedDuration = Integer.parseInt(XMLHandler.getNodeValue("estimatedDuration", response.getBody())) + 6000;
            } catch (Exception e)
            {
                estimatedDuration = defaultEstimatedDuration;
            }
            if (counter <= estimatedDuration) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter = counter + (sleepTime / 1000);
            } else {
                logger.error("Jenkins Job: " + jenkinsJobName + " has exceeded Estimated Build Time therefore Aborting Job.");
                counter = 0;
                response = stopBuildingJenkinsJob(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName);
                return "TIMEOUT";
            }
        }
        return XMLHandler.getNodeValue("result", jenkinsJobStatus(jenkinsHostName, jenkinsBaseDirectory, jenkinsJobName, "lastBuild").getBody());
    }
}
