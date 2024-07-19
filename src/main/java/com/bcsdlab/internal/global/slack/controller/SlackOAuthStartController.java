package com.bcsdlab.internal.global.slack.controller;

import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackOAuthAppServlet;

import jakarta.servlet.annotation.WebServlet;

@WebServlet("/api-slack.internal.bcsdlab.com/oauth/start")
public class SlackOAuthStartController extends SlackOAuthAppServlet {
    public SlackOAuthStartController(App app) {
        super(app);
    }
}
