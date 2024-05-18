package com.bcsdlab.internal.global.slack;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bcsdlab.internal.dues.Dues;
import com.bcsdlab.internal.dues.controller.dto.request.SendSlackMessage;
import com.bcsdlab.internal.dues.repository.DuesRepository;
import com.bcsdlab.internal.global.exception.ExternalApiException;
import com.bcsdlab.internal.global.slack.model.SlackNotificationFactory;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.users.UsersListRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.User;
import com.slack.api.webhook.Payload;

@Service
public class SlackService {

    private final Slack slack;
    private final String token;
    private final DuesRepository duesRepository;
    private final SlackNotificationFactory slackNotificationFactory;
    private final String internal;

    public SlackService(
        Slack slack,
        DuesRepository duesRepository,
        SlackNotificationFactory slackNotificationFactory,
        @Value("${slack.api.token}") String token,
        @Value("${slack.internal}") String internal
    ) {
        this.slack = slack;
        this.token = token;
        this.duesRepository = duesRepository;
        this.slackNotificationFactory = slackNotificationFactory;
        this.internal = internal;
    }

    public List<User> getMembers() {
        try {
            MethodsClient methods = slack.methods(token);
            UsersListResponse usersListResponse = methods.usersList(UsersListRequest.builder().token(token).build());
            return usersListResponse.getMembers();
        } catch (Exception e) {
            throw new ExternalApiException("Slack 사용자 호출 과정에서 문제가 발생했습니다.");
        }
    }

    public void sendDuesNotificationByGlobal(SendSlackMessage request, String presidentId, String vicePresidentId) {
        Payload payload = slackNotificationFactory.generateSlackDuesNotificationByGlobal(
            request,
            presidentId,
            vicePresidentId
        );
        sendChannelMessage(internal, payload);
    }

    public void sendDuesNotificationByDM(List<Dues> dues, String presidentId, String vicePresidentId) {
        dues.forEach(it -> {
            int count = duesRepository.findAllByMemberId(it.getMember().getId()).size();
            Payload payload = slackNotificationFactory.generateSlackDuesNotificationByDM(
                count * 10000,
                count,
                presidentId,
                vicePresidentId
            );
            snedDM(it.getMember().getSlackId(), payload);
        });
    }

    private void sendChannelMessage(String url, Payload payload) {
        try {
            slack.send(url, payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void snedDM(String userSlackId, Payload payload) {
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
            .channel(userSlackId)
            .blocks(payload.getBlocks())
            .build();
        try {
            ChatPostMessageResponse response = slack.methods(token).chatPostMessage(request);
            if (!response.isOk()) {
                throw new RuntimeException("Slack API error: " + response.getError());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
