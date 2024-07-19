package com.bcsdlab.internal.global.slack.controller;

import com.slack.api.bolt.servlet.SlackAppServlet;
import com.slack.api.bolt.App;

import jakarta.servlet.annotation.WebServlet;

@WebServlet("/api-slack.internal.bcsdlab.com/event")
public class SlackAppController extends SlackAppServlet {

    public SlackAppController(App app) {
        super(app);
    }
}
