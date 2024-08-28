package com.ericsson.sut.test.operators;

import java.util.Map;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.Ports;
import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserOS;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UI;

public class GenericUIOperator {
    
    private Browser browser;
    
    public BrowserTab startBrowser(String hostName, String location, String packageName){
        browser = UI.newBrowser(BrowserType.FIREFOX, BrowserOS.WINDOWS);
        Host host = DataHandler.getHostByName(hostName);
        Map<Ports, String> portMap = host.getPort();
        String port = portMap.get(Ports.HTTP);
        if (port == null) {
            throw new IllegalArgumentException("HTTP port not defined for host 'ciportal'");
        }
        String url = String.format("http://%s:%s", host.getIp(), port);
        if (location == null && packageName == null) {
            return browser.open(url);
        }else if (location != null && packageName == null){
            return browser.open(url+location+"/");
        }
        else{
            return browser.open(url+location+packageName+"/");
        }
    }
}
