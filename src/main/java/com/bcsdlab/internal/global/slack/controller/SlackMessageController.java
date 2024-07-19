package com.bcsdlab.internal.global.slack.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slack.api.bolt.App;
import com.slack.api.bolt.request.RequestHeaders;
import com.slack.api.bolt.request.builtin.EventRequest;
import com.slack.api.bolt.response.Response;

@RestController
public class SlackMessageController {

    private final App slackApp;

    @Autowired
    public SlackMessageController(App slackApp) {
        this.slackApp = slackApp;
    }

    @PostMapping("/api-slack.internal.bcsdlab.com/event")
    public void slackEvents(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // HTTP 요청을 Slack Bolt의 Request 객체로 변환
        System.out.println("dsa");
        Map<String, List<String>> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, Collections.list(request.getHeaders(headerName)));
        }

        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        RequestHeaders reqHeaders = new RequestHeaders(headers);
        EventRequest slackRequest = new EventRequest(requestBody.toString(), reqHeaders);

        // Slack 이벤트를 처리
        Response slackResponse;
        try {
            slackResponse = slackApp.run(slackRequest);
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("Error processing request: " + e.getMessage());
            return;
        }

        // 응답 전송
        response.setStatus(slackResponse.getStatusCode());
        response.getWriter().write(slackResponse.getBody());
    }
}
