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
import com.ericsson.sut.test.operators.WebOperator;
import com.google.inject.Inject;

public class WebCommand extends TorTestCaseHelper implements TestCase {
    @Inject
    OperatorRegistry<WebOperator> operatorRegistry;

    @Context(context = { Context.REST })
    @DataDriven(name = "restCommands")
    @Test
    public void shouldReturnWebPageFromGetCall(@Input("Url") String url,
            @Output("Product") String product,
            @Output("Release") String release, @Output("Drop") String drop,
            @Output("Expected") String expected) {
        WebOperator websiteOperator = operatorRegistry
                .provide(WebOperator.class);

        HttpResponse result = websiteOperator.executeGet(url, product, release,
                drop);

        Pattern regExp;
        // check here for a regular expression
        if (result.getBody().contains("None")) {
            regExp = Pattern.compile("^\\w+$");
        } else {
            regExp = Pattern.compile("^\\d+.\\d+(?:\\.\\d+)+");
        }
        Matcher match = regExp.matcher(result.getBody());

        assert (match.matches());
    }
}
