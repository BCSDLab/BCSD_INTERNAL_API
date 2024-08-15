package com.bcsdlab.internal.global.slack.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackAppConfig {

    private final String SLACK_BOT_TOKEN;
    private final String SLACK_APP_TOKEN;
    private final String SLACK_BOT_SIGNING_SECRET;
    private final String SLACK_BOT_CLIENT_ID;
    private final String SLACK_BOT_CLIENT_SECRET;

    public SlackAppConfig(
        @Value("${slack.bot-token}") String botToken,
        @Value("${slack.app-token}") String appToken,
        @Value("${slack.signing-secret}") String signingSecret,
        @Value("${slack.client-id}") String clientId,
        @Value("${slack.client-secret}") String clientSecret
    ) {
        this.SLACK_BOT_TOKEN = botToken;
        this.SLACK_APP_TOKEN = appToken;
        this.SLACK_BOT_SIGNING_SECRET = signingSecret;
        this.SLACK_BOT_CLIENT_ID = clientId;
        this.SLACK_BOT_CLIENT_SECRET = clientSecret;
    }

    // @Bean
    // public App initSlackApp() {
    //     App app = new App();
    //
    //     app.event(MessageEvent.class, (payload, ctx) -> {
    //         MessageEvent event = payload.getEvent();
    //         if (!event.getText().equals("hello!")) return ctx.ack();

    //         // 메시지 내용 처리 로직
    //         try {
    //             ctx.client().chatPostMessage(r -> r
    //                 .token(SLACK_BOT_TOKEN)
    //                 .channel(event.getChannel())
    //                 .text("You said: " + event.getText()));
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //         }
    //
    //         return ctx.ack();
    //     });
    //
    //     app.command("/스프링부트테스트", (req, ctx) -> {
    //         // 커맨드 입력한 사용자에게 응답
    //         ctx.ack("스프링부트환경테스트");
    //         // 채널에 메시지 전송
    //         String channelId = req.getPayload().getChannelId();
    //         MethodsClient client = ctx.client();
    //         try {
    //             ChatPostMessageResponse response = client.chatPostMessage(r -> r
    //                 .token(SLACK_BOT_TOKEN)
    //                 .channel(channelId)
    //                 .text("볼트앱 연결 테스트"));
    //             if (!response.isOk()) {
    //                 System.err.println("Error posting message: " + response.getError());
    //             }
    //         } catch (IOException | SlackApiException e) {
    //             e.printStackTrace();
    //         }
    //         return ctx.ack();
    //     });
    //
    //     return app;
    // }
    //
    // @Bean
    // public SocketModeApp socketModeApp(App app) throws Exception {
    //     SocketModeApp socketModeApp = new SocketModeApp(SLACK_APP_TOKEN, app);
    //     socketModeApp.start();
    //     return socketModeApp;
    // }
}
