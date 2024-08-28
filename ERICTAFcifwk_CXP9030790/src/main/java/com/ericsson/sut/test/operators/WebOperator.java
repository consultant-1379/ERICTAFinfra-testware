package com.ericsson.sut.test.operators;

import com.ericsson.cifwk.taf.tools.http.HttpResponse;

public interface WebOperator {

	HttpResponse executeGet(String url, String product, String release, String drop);
}
