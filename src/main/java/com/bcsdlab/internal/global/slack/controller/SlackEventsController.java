package com.bcsdlab.internal.global.slack.controller;

import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackAppServlet;

import jakarta.servlet.annotation.WebServlet;

@WebServlet("/api-slack.internal.bcsdlab.com/event")
public class SlackEventsController extends SlackAppServlet {
    public SlackEventsController(App app) {
        super(app);
    }
}
