package com.ericsson.cifwk.ui.test.cases ;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.*;
import org.testng.annotations.Test;
import javax.inject.Inject;
import com.ericsson.sut.test.operators.*;

public class ProductUI extends TorTestCaseHelper implements TestCase {

    @Inject
    private OperatorRegistry<CIPortalOperator> productProvider;

    @TestId(id = "CIP-6191", title = "Check that portal is displaying the various Types on the Product Summary Page")
    @Context(context = {Context.UI})
    @Test(groups={"Acceptance"})
    @DataDriven(name = "productUI")
    public void testProductSummary(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("host") String host,
            @Input("tagname") String tagName,
            @Output("expected") String expected,
            @Input("timeoutInMillis") long timeOut) {
        setTestCase(Id, testCaseDescription);
        CIPortalOperator productOperator = productProvider.provide(CIPortalOperator.class);
        productOperator.startBrowser(host, null, null);

        String result = productOperator.checkProductSummary(tagName, timeOut);
        assert (result.contains(expected));    
    }

    @TestId(id = "CIP-6329_Func_1", title = "Check that portal is displaying the CDB Types on the Product Set Release Page")
    @Context(context = {Context.UI})
    @Test(groups={"Acceptance"})
    @DataDriven(name = "productReleaseUI")
    public void testProductRelease(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("host") String host,
            @Input("prodRelease") String prodRelease,
            @Input("tagname") String tagName,
            @Output("expected") String expected,
            @Input("timeoutInMillis") long timeOut) {
        if(prodRelease != null && !prodRelease.isEmpty()){
            setTestCase(Id, testCaseDescription);
            CIPortalOperator productOperator = productProvider.provide(CIPortalOperator.class);
            productOperator.startBrowser(host, null, null);

            String result = productOperator.checkProductRelease(prodRelease, tagName, timeOut);
            assert (result.contains(expected));
        }
    }
}

