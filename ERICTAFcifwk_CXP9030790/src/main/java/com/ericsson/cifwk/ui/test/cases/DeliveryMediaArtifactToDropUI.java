package com.ericsson.cifwk.ui.test.cases;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.*;
import org.testng.annotations.Test;
import javax.inject.Inject;
import com.ericsson.sut.test.operators.*;

public class DeliveryMediaArtifactToDropUI extends TorTestCaseHelper implements
        TestCase {

    @Inject
    private OperatorRegistry<CIPortalOperator> deliveryProvider;

    @TestId(id = "CIP-5011", title = "Testing the Media Artifact Delivery To Drop (Deliver This UI Function)")
    @Context(context = { Context.UI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "deliverymediaartifacttodropui")
    public void testingTheDeliverThisUILinkMedia(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("host") String host, 
            @Input("username") String username,
            @Input("password") String password, 
            @Input("drop") String drop,
            @Input("product") String product,
            @Input("productSet") String productSet,
            @Input("rstate") String rstate, 
            @Input("email") String email,
            @Input("deliverDrop") String deliverDrop,
            @Output("expected") String expected,
            @Input("timeoutInMillis") long timeOut) {
        setTestCase(Id, testCaseDescription);
        CIPortalOperator deliveryOperator = deliveryProvider.provide(CIPortalOperator.class);
        deliveryOperator.startBrowser(host, null, null);
        String result = deliveryOperator.deliverMedia(username, password, drop, product, productSet, rstate, email, deliverDrop, timeOut);
        assert (result.contains(expected));
    }
}
