package com.ericsson.sut.test.operators;

import com.ericsson.cifwk.taf.tools.http.HttpResponse;

public interface DependencyWebOperator {

	HttpResponse executeGetDep(String url, String path, String artifact, String version);
}
