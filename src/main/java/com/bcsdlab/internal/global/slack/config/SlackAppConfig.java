package com.bcsdlab.internal.global.slack.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.model.event.MessageEvent;

@Configuration
public class SlackAppConfig {

    private final String SLACK_BOT_TOKEN;
    private final String SLACK_BOT_SIGNING_SECRET;
    private final String SLACK_BOT_CLIENT_ID;
    private final String SLACK_BOT_CLIENT_SECRET;

    public SlackAppConfig(
        @Value("${slack.bot-token}") String botToken,
        @Value("${slack.signing-secret}") String signingSecret,
        @Value("${slack.client-id}") String clientId,
        @Value("${slack.client-secret}") String clientSecret
    ) {
        this.SLACK_BOT_TOKEN = botToken;
        this.SLACK_BOT_SIGNING_SECRET = signingSecret;
        this.SLACK_BOT_CLIENT_ID = clientId;
        this.SLACK_BOT_CLIENT_SECRET = clientSecret;
    }

    // If you would like to run this app for a single workspace,
    // enabling this Bean factory should work for you.
    @Bean
    public AppConfig loadSingleWorkspaceAppConfig() {
        return AppConfig.builder()
            .singleTeamBotToken(SLACK_BOT_TOKEN)
            .signingSecret(SLACK_BOT_SIGNING_SECRET)
            .build();
    }

    // If you would like to run this app for multiple workspaces,
    // enabling this Bean factory should work for you.
    // @Bean
    // public AppConfig loadOAuthConfig() {
    //     return AppConfig.builder()
    //         .singleTeamBotToken(null)
    //         .clientId(SLACK_BOT_CLIENT_ID)
    //         .clientSecret(SLACK_BOT_CLIENT_SECRET)
    //         .signingSecret(SLACK_BOT_SIGNING_SECRET)
    //         .scope("app_mentions:read,channels:history,channels:read,chat:write")
    //         .oauthInstallPath("/slack/install")
    //         .oauthRedirectUriPath("/slack/oauth_redirect")
    //         .build();
    // }

    @Bean
    public App initSlackApp(AppConfig config) {
        App app = new App(config).asOAuthApp(true);
        if (config.getClientId() != null) {
            app.asOAuthApp(true);
        }
        app.command("/test", (req, ctx) -> {
            return ctx.ack("What's up?");
        });
        return app;
        // app.command(MessageEvent.class, (payload, ctx) -> {
        //     String text = payload.getEvent().getText();
        //     // 특정 키워드 또는 조건에 따라 응답
        //     System.out.println("dd");
        //     if (text.contains("hello")) {
        //         System.out.println("dsa");
        //         ctx.say("Hello! How can I assist you today?");
        //     } else if (text.contains("help")) {
        //         ctx.say("Sure, what do you need help with?");
        //     }
        //     return ctx.ack();
        // });
        //
        // return app;
    }

}
