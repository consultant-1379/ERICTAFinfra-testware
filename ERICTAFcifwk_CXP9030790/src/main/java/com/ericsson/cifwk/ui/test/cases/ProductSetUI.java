package com.ericsson.cifwk.ui.test.cases ;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.*;
import org.testng.annotations.Test;
import javax.inject.Inject;
import com.ericsson.sut.test.operators.*;

public class ProductSetUI extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<CIPortalOperator> prodSetProvider;

    @TestId(id = "CIP-6191", title = "Check that portal is displaying the various Types on the Product Set Summary Page")
    @Context(context = {Context.UI})
    @Test(groups={"Acceptance"})
    @DataDriven(name = "productSetUI")
    public void testProductSetSummary(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("host") String host,
            @Input("tagname") String tagName,
            @Output("expected") String expected,
            @Input("timeoutInMillis") long timeOut) {
        setTestCase(Id, testCaseDescription);
        CIPortalOperator prodSetOperator = prodSetProvider.provide(CIPortalOperator.class);
        prodSetOperator.startBrowser(host, null, null);

        String result = prodSetOperator.checkProductSetSummary(tagName, timeOut);
        assert (result.contains(expected));    
    }

    @TestId(id = "CIP-6329_Func_1", title = "Check that portal is displaying the CDB Types on the Product Set Release Page")
    @Context(context = {Context.UI})
    @Test(groups={"Acceptance"})
    @DataDriven(name = "productSetReleaseUI")
    public void testProductSetRelease(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("host") String host,
            @Input("psRelease") String psRelease,
            @Input("tagname") String tagName,
            @Output("expected") String expected,
            @Input("timeoutInMillis") long timeOut) {
        if(psRelease != null && !psRelease.isEmpty()){
            setTestCase(Id, testCaseDescription);
            CIPortalOperator prodSetOperator = prodSetProvider.provide(CIPortalOperator.class);
            prodSetOperator.startBrowser(host, null, null);

            String result = prodSetOperator.checkProductSetRelease(psRelease, tagName, timeOut);
            assert (result.contains(expected));
        }
    }
}