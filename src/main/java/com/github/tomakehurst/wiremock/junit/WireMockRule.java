/*
 * Copyright (C) 2011 Thomas Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tomakehurst.wiremock.junit;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.VerificationException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;


import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WireMockRule extends WireMockServer implements MethodRule, TestRule {

    private final boolean failOnUnmatchedStubs;

    public WireMockRule(Options options) {
        this(options, true);
    }

    public WireMockRule(Options options, boolean failOnUnmatchedStubs) {
        super(options);
        this.failOnUnmatchedStubs = failOnUnmatchedStubs;
    }

    public WireMockRule(int port) {
		this(wireMockConfig().port(port));
	}

    public WireMockRule(int port, Integer httpsPort) {
        this(wireMockConfig().port(port).httpsPort(httpsPort));
    }
	
	public WireMockRule() {
		this(wireMockConfig());
	}

    @Override
    public Statement apply(final Statement base, Description description) {
        return apply(base, null, null);
    }

	@Override
	public Statement apply(final Statement base, FrameworkMethod method, Object target) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				start();
				WireMock.configureFor("localhost", port());
				try {
                    before();
                    base.evaluate();
                    checkForUnmatchedRequests();
                } finally {
                    after();
                    stop();
                }
			}

		};
	}

    private void checkForUnmatchedRequests() {
        if (failOnUnmatchedStubs) {
            List<LoggedRequest> unmatchedRequests = findAllUnmatchedRequests();
            if (!unmatchedRequests.isEmpty()) {
                throw new VerificationException(unmatchedRequests.size() + " requests were unmatched by any stub mapping. See the log for details.");
            }
        }
    }

    protected void before() {
        // NOOP
    }

    protected void after() {
        // NOOP
    }
}
