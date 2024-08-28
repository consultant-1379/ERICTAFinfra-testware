package com.ericsson.cifwk.ui.test.cases;

import java.util.ArrayList;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.*;

import org.testng.annotations.Test;

import javax.inject.Inject;

import com.ericsson.sut.test.operators.*;

public class SummaryPageConfigurationUI extends TorTestCaseHelper implements
        TestCase {

    @Inject
    private OperatorRegistry<CIPortalOperator> provider;
    private ArrayList<String> values = new ArrayList<String>();
    private String username = "tafTest";
    private String password = "tafTest123";
    private String host = "ciportal";

    @TestId(id = "CIP-6328_Func_1", title = "Testing the configuration Form for Product Summary Page")
    @Context(context = { Context.UI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "productSumPageForm")
    public void testProductSummaryPageForm(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("product") String productName, 
            @Input("value") String value,
            @Input("tagName") String tagName,
            @Input("header") String header,
            @Output("expected") String expected,
            @Input("timeoutInMillis") long timeOut) {
        setTestCase(Id, testCaseDescription);
        CIPortalOperator operator = provider.provide(CIPortalOperator.class);
        operator.startBrowser(host, null, null);
        String result = operator.checkProductSummaryPageForm(username,
                password, productName, value, tagName, timeOut, header);
        assert (result.contains(expected));
    }

    @TestId(id = "CIP-6328_Func_2", title = "Testing the configuration Form for Product Set Summary Page")
    @Context(context = { Context.UI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "productSetSumPageForm")
    public void testProductSetSummaryPageForm(
            @Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("url") String url,
            @Input("productSet") String productSetName,
            @Input("value") String value, 
            @Input("tagName") String tagName,
            @Input("header") String header,
            @Output("expected") String expected,
            @Input("timeoutInMillis") long timeOut) {
        setTestCase(Id, testCaseDescription);
        CIPortalOperator operator = provider.provide(CIPortalOperator.class);
        operator.startBrowser(host, url, null);
        String result = operator.checkProductSetSummaryPageForm(username,
                password, productSetName, value, tagName, timeOut, header);
        assert (result.contains(expected));
    }

    @TestId(id = "CIP-6328_Func_3", title = "Testing Results of Configuration Form for Product Summary Page")
    @Context(context = { Context.UI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "productSumPageFormResult")
    public void testProductSummaryPageFormResults(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("product") String productName,
            @Input("relDropOne") String relDropOne,
            @Input("numberOne") String numberOne,
            @Input("relDropTwo") String relDropTwo,
            @Input("numberTwo") String numberTwo,
            @Input("relDropThree") String relDropThree,
            @Input("numberThree") String numberThree,
            @Input("relDropFour") String relDropFour,
            @Input("numberFour") String numberFour,
            @Input("action") String action,
            @Input("defaultDrop") String defaultDrop,
            @Input("header") String header,
            @Output("expected") String expected,
            @Input("timeoutInMillis") long timeOut) {
        setTestCase(Id, testCaseDescription);
        CIPortalOperator operator = provider.provide(CIPortalOperator.class);
        values.add(relDropOne);
        values.add(numberOne);
        values.add(relDropTwo);
        values.add(numberTwo);
        values.add(relDropThree);
        values.add(numberThree);
        values.add(relDropFour);
        values.add(numberFour);
        operator.startBrowser(host, null, null);
        String result = operator.checkProductSummaryPageFormResult(username, password, productName, values, action, defaultDrop, timeOut, header);
        values.clear();
        assert (result.contains(expected));
    }

    @TestId(id = "CIP-6328_Func_4", title = "Testing Results of Configuration Form for Product Set Summary Page")
    @Context(context = { Context.UI })
    @Test(groups = { "Acceptance" })
    @DataDriven(name = "productSetSumPageFormResult")
    public void testProductSetSummaryPageFormResults(@Input("id") String Id,
            @Input("description") String testCaseDescription,
            @Input("url") String url,
            @Input("productSet") String productSetName,
            @Input("relDropOne") String relDropOne,
            @Input("numberOne") String numberOne,
            @Input("relDropTwo") String relDropTwo,
            @Input("numberTwo") String numberTwo,
            @Input("relDropThree") String relDropThree,
            @Input("numberThree") String numberThree,
            @Input("relDropFour") String relDropFour,
            @Input("numberFour") String numberFour,
            @Input("action") String action,
            @Input("defaultDrop") String defaultDrop,
            @Input("header") String header,
            @Output("expected") String expected,
            @Input("timeoutInMillis") long timeOut) {
        setTestCase(Id, testCaseDescription);
        CIPortalOperator operator = provider.provide(CIPortalOperator.class);
        values.add(relDropOne);
        values.add(numberOne);
        values.add(relDropTwo);
        values.add(numberTwo);
        values.add(relDropThree);
        values.add(numberThree);
        values.add(relDropFour);
        values.add(numberFour);
        operator.startBrowser(host, url, null);
        String result = operator.checkProductSetSummaryPageFormResult(username, password, productSetName, values, action, defaultDrop, timeOut, header);
        values.clear();
        assert (result.contains(expected));
    }
}	
