package com.bcsdlab.internal.global.log;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.jboss.logging.MDC;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.StopWatch;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestLoggingFilter implements Filter {

    public static final String REQUEST_ID = "requestId";
    private final ObjectProvider<PathMatcher> pathMatcherProvider;
    private final Set<String> setIgnoredUrlPatterns = new HashSet<>();

    public void setIgnoredUrlPatterns(String... ignoredUrlPatterns) {
        setIgnoredUrlPatterns.addAll(Arrays.asList(ignoredUrlPatterns));
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (!CorsUtils.isPreFlightRequest(httpRequest) && !isIgnoredUrl(httpRequest)) {
            ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
            ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper(
                (HttpServletResponse) response);
            StopWatch stopWatch = new StopWatch();

            try {
                MDC.put(REQUEST_ID, getRequestId(httpRequest));
                stopWatch.start();
                log.info("[{}] request start [uri: {} {}]",
                    MDC.get(REQUEST_ID), httpRequest.getMethod(), httpRequest.getRequestURI());
                chain.doFilter(cachedRequest, cachedResponse);
            } finally {
                stopWatch.stop();
                log.info("[{}] response body: {}", MDC.get(REQUEST_ID), getResponseBody(cachedResponse));
                log.info("[{}] request end [time: {}ms]", MDC.get(REQUEST_ID), stopWatch.getTotalTimeMillis());
                MDC.clear();
            }

        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isIgnoredUrl(HttpServletRequest request) {
        PathMatcher pathMatcher = pathMatcherProvider.getIfAvailable();
        Objects.requireNonNull(pathMatcher);
        return setIgnoredUrlPatterns.stream()
            .anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()));
    }

    private String getRequestId(HttpServletRequest httpRequest) {
        String requestId = httpRequest.getHeader("X-Request-ID");
        return ObjectUtils.isEmpty(requestId) ? UUID.randomUUID().toString().replace("-", "") : requestId;
    }

    private String getResponseBody(ContentCachingResponseWrapper response) throws IOException {
        String payload = null;
        if (response != null) {
            response.setCharacterEncoding("UTF-8");
            byte[] buf = response.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, response.getCharacterEncoding());
                response.copyBodyToResponse();
            }
        }

        return null == payload ? " - " : payload;
    }
}
