package com.ericsson.sut.test.operators;

import javax.inject.Singleton;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.tools.http.HttpTool;
import com.ericsson.cifwk.taf.tools.http.HttpToolBuilder;

@Operator(context = Context.REST)
@Singleton
public class WebOperatorHttp implements WebOperator {

	@Override
	public HttpResponse executeGet(String url, String product, String release, String drop) {
		// Get a Host object - using a DataHandler method
		Host host = DataHandler.getHostByName(url);

		// Instantiate a HttpTool passing it the Host
	    HttpTool tool = HttpToolBuilder.newBuilder(host)
		 		.useHttpsIfProvided(false)
		 		.trustSslCertificates(true)
		 		.build();
				
		// Use the HttpTool to send a get request and assign
		// the returned object to a variable named response
	    HttpResponse response = tool.request()
	            .queryParam("product", product)
	            .queryParam("release", release)
	            .queryParam("drop", drop).get("/getlatestiso/");
	    
	    // return the http object
		return response;
	}
}
