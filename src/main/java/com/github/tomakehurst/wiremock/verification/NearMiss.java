package com.github.tomakehurst.wiremock.verification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class NearMiss implements Comparable<NearMiss> {

    private final LoggedRequest request;
    private final StubMapping mapping;
    private final RequestPattern requestPattern;
    private final MatchResult matchResult;

    @JsonCreator
    public NearMiss(@JsonProperty("request") LoggedRequest request,
                    @JsonProperty("stubMapping") StubMapping mapping,
                    @JsonProperty("requestPattern") RequestPattern requestPattern,
                    @JsonProperty("matchResult") MatchResult matchResult) {
        this.request = request;
        this.mapping = mapping;
        this.requestPattern = requestPattern;
        this.matchResult = matchResult;
    }

    public NearMiss(LoggedRequest request,
                    StubMapping mapping,
                    MatchResult matchResult) {
        this(request, mapping, null, matchResult);
    }

    public NearMiss(LoggedRequest request,
                    RequestPattern requestPattern,
                    MatchResult matchResult) {
        this(request, null, requestPattern, matchResult);
    }

    public LoggedRequest getRequest() {
        return request;
    }

    public StubMapping getStubMapping() {
        return mapping;
    }

    public RequestPattern getRequestPattern() {
        return requestPattern;
    }

    public MatchResult getMatchResult() {
        return matchResult;
    }

    @Override
    public int compareTo(NearMiss o) {
        return o.getMatchResult().compareTo(matchResult);
    }
}
