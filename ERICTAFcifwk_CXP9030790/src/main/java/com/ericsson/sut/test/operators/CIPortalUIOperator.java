package com.ericsson.sut.test.operators;

import java.util.ArrayList;

import com.ericsson.cifwk.taf.UiOperator;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.ui.*;
import com.ericsson.cifwk.ui.test.pages.*;

@Operator(context = Context.UI)
public class CIPortalUIOperator implements CIPortalOperator, UiOperator {
    private BrowserTab browserTab;
    private DeliveryPackageToDropViewModel deliverView;
    private DeliveryMediaArtifactToDropViewModel deliverMediaView;
    private CLUEstatusViewModel statusView;
    private PRIMViewModel primView;
    private GenericUIOperator genericUIOperator;
    private MetricsCloudViewModel cloudMetricsView;
    private ProductSetViewModel prodSetView;
    private ProductViewModel productView;
    private CIPortalPerformanceViewModel performView;
    private SummaryPageConfigurationViewModel sumConfigView;

    public CIPortalUIOperator() {

    }

    public void startBrowser(String hostName, String location,
            String packageName) {
        genericUIOperator = new GenericUIOperator();
        browserTab = genericUIOperator.startBrowser(hostName, location,
                packageName);
    }

    public String deliver(String username, String password, String type,
            String product, String pkg, String rstate, String email,
            String drop, long timeOut) {
        deliverView = browserTab.getView(DeliveryPackageToDropViewModel.class);
        String result = deliverView.delivery(browserTab, username, password,
                type, product, pkg, rstate, email, drop, timeOut);
        return result;
    }
    
    @Override
    public String deliverMedia(String username, String password, String drop, String product,
            String productSet, String rstate, String email,
            String deliverDrop, long timeOut) {
        deliverMediaView = browserTab.getView(DeliveryMediaArtifactToDropViewModel.class);
        return deliverMediaView.deliveryMedia(browserTab, username, password,
                drop, product, productSet, rstate, email, deliverDrop, timeOut);
    }

    public ArrayList<String> status(long timeOut) {
        statusView = browserTab.getView(CLUEstatusViewModel.class);
        return statusView.status(browserTab, timeOut);
    }

    public String checkPRIMresults(String username, String password,
            String product, String number, String rstate, String drop,
            String media, String write, String missing, long timeOut) {
        primView = browserTab.getView(PRIMViewModel.class);
        return primView.primResults(browserTab, username, password, product,
                number, rstate, drop, media, write, missing, timeOut);
    }

    public String checkPRIMUserAccess(String username, String password,
            long timeOut) {
        primView = browserTab.getView(PRIMViewModel.class);
        return primView.primUserAccessResults(browserTab, username, password,
                timeOut);
    }

    public boolean checkCloudMetricsForm(String ra, String dataCenter,
            String startDT, String endDT, String eventType, long timeOut,
            String type) {
        cloudMetricsView = browserTab.getView(MetricsCloudViewModel.class);
        return cloudMetricsView.checkMetricsForm(browserTab, ra, dataCenter,
                startDT, endDT, eventType, timeOut, type);
    }

    public String checkProductSetSummary(String tagName, long timeOut) {
        prodSetView = browserTab.getView(ProductSetViewModel.class);
        return prodSetView.checkProductSetSummary(browserTab, tagName, timeOut);
    }

    @Override
    public String getCloudMetrics(String ra, String dataCenter, String startDT,
            String endDT, String eventType, String resultType, long timeOut) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String checkProductSetRelease(String psRelease, String tagName,
            long timeOut) {
        prodSetView = browserTab.getView(ProductSetViewModel.class);
        return prodSetView.checkProductSetRelease(browserTab, psRelease,
                tagName, timeOut);
    }

    @Override
    public String checkProductSummary(String tagName, long timeOut) {

        productView = browserTab.getView(ProductViewModel.class);
        return productView.checkProductSummary(browserTab, tagName, timeOut);
    }

    @Override
    public String checkProductRelease(String prodRelease, String tagName,
            long timeOut) {
        productView = browserTab.getView(ProductViewModel.class);
        return productView.checkProductRelease(browserTab, prodRelease, tagName, timeOut);
    }

    @Override
    public String checkAutoRefresh(String username, long timeOut) {
        performView = browserTab.getView(CIPortalPerformanceViewModel.class);
        return performView.checkAutoRefresh(browserTab, username, timeOut);
    }

    @Override
    public String checkDropPageLoad(String product, String dropName,
            long firstTimeOut, long secondTimeOut) {
        performView = browserTab.getView(CIPortalPerformanceViewModel.class);
        return performView.checkDropPageLoad(browserTab, product, dropName, firstTimeOut, secondTimeOut);
    }

    @Override
    public String checkProductPackagePageLoad(String product, long timeOut) {
        performView = browserTab.getView(CIPortalPerformanceViewModel.class);
        return performView.checkProductPackagePageLoad(browserTab, product, timeOut);
    }

    @Override
    public String checkTestwarePageLoad(String product, long timeOut) {
        performView = browserTab.getView(CIPortalPerformanceViewModel.class);
        return performView.checkTestwarePageLoad(browserTab, product, timeOut);
    }

    @Override
    public String checkProductSummaryPageForm(String username, String password, String productName, String value, String tagName, long timeOut, String header) {
        sumConfigView = browserTab.getView(SummaryPageConfigurationViewModel.class);
        return sumConfigView.checkProductSummaryPageForm(browserTab, username, password, productName, value, tagName, timeOut, header);
    }

    @Override
    public String checkProductSetSummaryPageForm(String username, String password, String productSetName, String value, String tagName, long timeOut, String header) {
        sumConfigView = browserTab.getView(SummaryPageConfigurationViewModel.class);
        return sumConfigView.checkProductSetSummaryPageForm(browserTab, username, password, productSetName, value, tagName, timeOut, header);
    }

    @Override
    public String checkProductSummaryPageFormResult(String username, String password, String productName, ArrayList<String> values, String action, String defaultDrop, long timeOut,String header) {
        sumConfigView = browserTab.getView(SummaryPageConfigurationViewModel.class);
        return sumConfigView.checkProductSummaryPageFormResult(browserTab, username, password, productName, values, action, defaultDrop, timeOut, header);
 
    }

    @Override
    public String checkProductSetSummaryPageFormResult(String username, String password, String productSetName, ArrayList<String> values, String action, String defaultDrop, long timeOut,String header) {
        sumConfigView = browserTab.getView(SummaryPageConfigurationViewModel.class);
        return sumConfigView.checkProductSetSummaryPageFormResult(browserTab, username, password, productSetName, values, action, defaultDrop, timeOut, header);
    }
}
