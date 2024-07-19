package com.bcsdlab.internal.global.slack.controller;

import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackAppServlet;
import com.slack.api.bolt.servlet.SlackOAuthAppServlet;

import jakarta.servlet.annotation.WebServlet;

@WebServlet("/api-slack.internal.bcsdlab.com/oauth/callback")
public class SlackOAuthCallbackController extends SlackOAuthAppServlet {
    public SlackOAuthCallbackController(App app) {
        super(app);
    }
}
