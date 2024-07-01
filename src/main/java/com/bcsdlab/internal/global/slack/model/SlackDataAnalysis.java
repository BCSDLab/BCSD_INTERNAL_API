package com.bcsdlab.internal.global.slack.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.slack.api.Slack;

@Component
public class SlackDataAnalysis {

    private final String bBot;
    private final Slack slack;

    public SlackDataAnalysis(
        @Value("{slack.bbot-test}") String bBot,
        Slack slack
    ) {
        this.bBot = bBot;
        this.slack = slack;
    }
}


