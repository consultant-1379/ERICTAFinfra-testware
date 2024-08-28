package com.ericsson.cifwk.ui.test.cases;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.ui.test.pages.KGBReadyUI;

import org.testng.annotations.Test;

import com.ericsson.sut.test.operators.*;

public class KGBReadyUITest extends TorTestCaseHelper implements TestCase {
    
    private BrowserTab browserTab;
    private GenericUIOperator genericUIOperator;
    private KGBReadyUI kgbReady;
    
    @TestId(id = "CIP-6143_Func_2", title = "Verify KGB Ready UI functionality on CIFWK Portal based on ISO Verison")
    @Context(context = {Context.UI})
    @Test(groups={"Functional","Heartbeat","vAPP","Physical"})
    @DataDriven(name = "kgbReadyUICommands")
    public void verifiyKGBReadyonISOVersion(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("Host") String host,
            @Input("Product") String product,
            @Input("Release") String release,
            @Input("Version") String version,
            @Output("Expected") String expected,
            @Input("timeoutInMillis") long timeOut) {
        setTestCase(Id, testCaseDescription);
        
        setTestStep("Start Browser for UI tests to Begin");
        genericUIOperator = new GenericUIOperator();
        browserTab = genericUIOperator.startBrowser(host, null, null); 
        
        setTestStep("Verify that the KBGReady Set on Product ISO Version");
        kgbReady = browserTab.getView(KGBReadyUI.class);
        kgbReady.setProduct(product); kgbReady.setRelease(release);kgbReady.setVersion(version);
        
        String isoVersionresult = kgbReady.verifyKGBReadyISO(browserTab, timeOut); 
        assertEquals(expected, isoVersionresult);     
    }
    
    @TestId(id = "CIP-6143_Func_3", title = "Verify KGB Ready UI functionality on CIFWK Portal based on Product Set Version")
    @Context(context = {Context.UI})
    @Test(groups={"Functional","Heartbeat","vAPP","Physical"})
    @DataDriven(name = "kgbReadyUICommands")
    public void verifiyKGBReadyonProductSetVersion(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("Host") String host,
            @Input("ProductSetRA") String productSetRA,
            @Input("ProductSetRelease") String productSetRelease,
            @Output("Expected") String expected,
            @Input("timeoutInMillis") long timeOut) {
        setTestCase(Id, testCaseDescription);
        
        setTestStep("Start Browser for UI tests to Begin");
        genericUIOperator = new GenericUIOperator();
        browserTab = genericUIOperator.startBrowser(host, null, null);
        
        setTestStep("Verify that the KBGReady Set on Product Set Version");
        kgbReady = browserTab.getView(KGBReadyUI.class);
        kgbReady.setProductSetRA(productSetRA); kgbReady.setProductSetRelease(productSetRelease);
        String productSetresult = kgbReady.verifyKGBReadyProductSet(browserTab, timeOut);
        assertEquals(expected, productSetresult);    
    }
}
    
