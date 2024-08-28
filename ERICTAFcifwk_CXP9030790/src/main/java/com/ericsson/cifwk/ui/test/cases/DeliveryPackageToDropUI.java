package com.ericsson.cifwk.ui.test.cases ;

import com.ericsson.cifwk.taf.*;
import com.ericsson.cifwk.taf.annotations.*;
import com.ericsson.cifwk.taf.guice.*;
import org.testng.annotations.Test;
import javax.inject.Inject;
import com.ericsson.sut.test.operators.*;

public class DeliveryPackageToDropUI extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<CIPortalOperator> deliveryProvider;

	@TestId(id = "CIP-4770", title = "Testing the Package Delivery To Drop (Deliver This UI Function)")
	@Context(context = {Context.UI})
	@Test(groups={"Acceptance"})
	@DataDriven(name = "deliverypackagetodropui")
	public void testingTheDeliverThisUILink(
			@Input("id") String Id,
			@Input("description") String testCaseDescription,
			@Input("host") String host,
			@Input("username") String username,
			@Input("password") String password,
			@Input("type") String type,
			@Input("product") String product,
			@Input("package") String pkg,
			@Input("rstate") String rstate,
			@Input("email") String email,
			@Input("drop") String drop,
			@Output("expected") String expected,
			@Input("timeoutInMillis") long timeOut) {
		setTestCase(Id, testCaseDescription);
		CIPortalOperator deliveryOperator = deliveryProvider.provide(CIPortalOperator.class);
		deliveryOperator.startBrowser(host, null, null);
		String result = deliveryOperator.deliver(username, password, type, product, pkg, rstate, email, drop, timeOut);
		assert (result.contains(expected));    
	}
}
	
