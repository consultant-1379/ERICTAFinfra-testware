package com.ericsson.cifwk.utils;

public class SutHost {

    private String ip;
    private String hostname;
    private String type;
    private SutHost[] nodes;
    private SutHost[] interfaces;
    private String ipv4;

    public SutHost() {
    }

    public SutHost(String ip, String hostname, String type, SutHost[] nodes,
            SutHost[] interfaces, String ipv4) {
        this.ip = ip;
        this.hostname = hostname;
        this.type = type;
        this.nodes = nodes;
        this.interfaces = interfaces;
        this.ipv4 = ipv4;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SutHost[] getNodes() {
        return nodes;
    }

    public void setNodes(SutHost[] nodes) {
        this.nodes = nodes;
    }

    public SutHost[] getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(SutHost[] interfaces) {
        this.interfaces = interfaces;
    }

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }
}