package com.ericsson.cifwk.utils;

public class TestEnvironmentDetails {

    private static final String UNDEFINED_VALUE = "undefined";

    private String lmsHostIpAddress = UNDEFINED_VALUE;
    private String uiHostIpAddress = UNDEFINED_VALUE;
    private String gatewayHostName = UNDEFINED_VALUE;
    private String eventbasedclient1IpAddress = UNDEFINED_VALUE;

    public String getLmsHostIpAddress() {
        return lmsHostIpAddress;
    }

    public void setLmsHostIpAddress(String lmsHostIpAddress) {
        this.lmsHostIpAddress = lmsHostIpAddress;
    }

    public String getGatewayHostName() {
        return gatewayHostName;
    }

    public void setGatewayHostName(String gatewayHostName) {
        this.gatewayHostName = gatewayHostName;
    }

    public String getUiHostIpAddress() {
        return uiHostIpAddress;
    }

    public void eventbasedclient1IpAddress(String eventbasedclient1IpAddress) {
        this.eventbasedclient1IpAddress = eventbasedclient1IpAddress;
    }

    public String geteventbasedclient1IpAddress() {
        return eventbasedclient1IpAddress;
    }

    public void setUiHostIpAddress(String uiHostIpAddress) {
        this.uiHostIpAddress = uiHostIpAddress;
    }
}

