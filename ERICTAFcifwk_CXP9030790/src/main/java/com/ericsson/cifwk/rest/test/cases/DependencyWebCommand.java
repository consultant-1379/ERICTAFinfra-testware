package com.ericsson.cifwk.rest.test.cases;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.tools.http.constants.HttpStatus;
import com.ericsson.sut.test.operators.DependencyWebOperator;

import com.google.inject.Inject;

public class DependencyWebCommand extends TorTestCaseHelper implements TestCase {
    @Inject
    OperatorRegistry<DependencyWebOperator> operatorRegistry;

    @Context(context = { Context.REST })
    @DataDriven(name = "dependencyRestCommands")
    @Test
    public void shouldReturnWebPageFromGetCall(@Input("Url") String url,
            @Output("Path") String path, @Output("Artifact") String artifact,
            @Output("Version") String version,
            @Output("Expected") String expected) {
        DependencyWebOperator websiteOperator = operatorRegistry
                .provide(DependencyWebOperator.class);

        HttpResponse result = websiteOperator.executeGetDep(url, path,
                artifact, version);
        Pattern regExp;
        // check here for a regular expression
        regExp = Pattern.compile(expected);
        Matcher match = regExp.matcher(result.getBody());
        assertEquals(result.getResponseCode(), HttpStatus.OK);
        assert (match.matches());
    }
}
