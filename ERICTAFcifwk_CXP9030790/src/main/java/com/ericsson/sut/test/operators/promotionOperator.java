package com.ericsson.sut.test.operators;

import com.ericsson.cifwk.taf.tools.http.HttpResponse;

public interface promotionOperator {

	HttpResponse executeGet(String id, String description, String artifact, String version, String groupId, String signum, String execVersion, String execGroupId, String execArtifact, String url);
}
