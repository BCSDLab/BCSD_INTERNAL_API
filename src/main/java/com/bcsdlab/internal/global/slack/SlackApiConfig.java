package com.bcsdlab.internal.global.slack;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.slack.api.Slack;
import com.slack.api.SlackConfig;

@Configuration
public class SlackApiConfig {

    @Bean
    public Slack slack() {
        SlackConfig config = new SlackConfig();
        config.setPrettyResponseLoggingEnabled(true);
        return Slack.getInstance(config);
    }
}
