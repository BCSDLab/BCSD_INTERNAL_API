package com.bcsdlab.internal.global.slack.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bcsdlab.internal.bbot.service.MessageService;
import com.slack.api.bolt.App;
import com.slack.api.model.event.MessageEvent;


@Configuration
public class SlackAppConfig {

    private final String botToken;
    private final String signingSecret;

    public SlackAppConfig(
        @Value("${slack.SLACK_BOT_TOKEN}") String botToken,
        @Value("${slack.SLACK_SIGNING_SECRET}") String signingSecret
    ) {
        this.botToken = botToken;
        this.signingSecret = signingSecret;
    }

    @Bean
    public App initSlackApp(MessageService messageService) {

        App app = new App();

        app.config().setSingleTeamBotToken(botToken);
        app.config().setSigningSecret(signingSecret);

        app.event(MessageEvent.class, (payload, ctx) -> {
            String text = payload.getEvent().getText();

            // 특정 키워드 또는 조건에 따라 응답
            if (text.contains("hello")) {
                System.out.println("Hello! How can I assist you today?");
                // ctx.say("Hello! How can I assist you today?");
            } else if (text.contains("help")) {
                System.out.println("Sure, what do you need help with?");
                // ctx.say("Sure, what do you need help with?");
            }

            return ctx.ack();
        });

        return app;
    }
}
