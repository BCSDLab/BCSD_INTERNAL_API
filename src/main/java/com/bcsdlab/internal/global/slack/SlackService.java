package com.bcsdlab.internal.global.slack;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bcsdlab.internal.dues.MemberDuesCount;
import com.bcsdlab.internal.dues.controller.dto.request.SendSlackMessage;
import com.bcsdlab.internal.global.exception.ExternalApiException;
import com.bcsdlab.internal.global.slack.model.SlackNotificationFactory;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.users.UsersListRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.methods.response.conversations.ConversationsRepliesResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.Channel;
import com.slack.api.model.Conversation;
import com.slack.api.model.Message;
import com.slack.api.model.User;
import com.slack.api.webhook.Payload;

@Service
public class SlackService {

    private final Slack slack;
    private final String token;
    private final SlackNotificationFactory slackNotificationFactory;
    private final String notification;
    private final String bBot;

    public SlackService(
        Slack slack,
        SlackNotificationFactory slackNotificationFactory,
        @Value("${slack.api.token}") String token,
        @Value("${slack.notification}") String notification,
        @Value("{slack.bbot-test}") String bBot
    ) {
        this.slack = slack;
        this.token = token;
        this.slackNotificationFactory = slackNotificationFactory;
        this.notification = notification;
        this.bBot = bBot;
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
        sendChannelMessage(notification, payload);
    }

    public void sendDuesNotificationByDM(List<MemberDuesCount> dues, String presidentId, String vicePresidentId) {
        dues.forEach(it -> {
            Payload payload = slackNotificationFactory.generateSlackDuesNotificationByDM(
                (int)(it.getCount() * 10000),
                (int)it.getCount(),
                it.getMember().getName(),
                presidentId,
                vicePresidentId
            );
            sendDM(it.getMember().getSlackId(), payload);
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
    public void sendDM(String userSlackId, Payload payload) {
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

    public void getStatistics() {
        Map<String, Integer> teamMessage = Map.of(
            "CGQ6BJWBT", 0, // UIUX
            "CGQK77GCB", 0, // back_end
            "CGQKFN0KV", 0, // game
            "CGQM7A3S8", 0, // front_end
            "CGRQXU9PZ", 0, // android
            "C06KP7Y53DJ", 0, // data
            "C06N40APJAK", 0 // pm
        );
        Map<String, Integer> trackMessage = Map.of(
            "C06NQT2TY9X", 0, // campus
            "C06P3C96P9R", 0, // 인프라
            "C06N99Z3D45", 0, // business
            "C06NEM6EY3W", 0 // user
        );
        List<Message> messages = getAllChannelMessages();
        for (Message message: messages) {
            // message.
        }
    }

    public List<Conversation> getChannels() {
        try {
            return slack.methods(token).conversationsList(req -> req)
                .getChannels()
                .stream()
                .filter(Conversation::isMember)
                .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SlackApiException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getChannelIds() {
        try {
            return slack.methods(token).conversationsList(req -> req)
                .getChannels()
                .stream()
                .filter(Conversation::isMember)
                .map(Conversation::getId)
                .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SlackApiException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Message> getAllChannelMessages() {
        List<String> channelIds = getChannelIds();
        return channelIds.stream().flatMap(channelId -> getChannelMessages(
                    channelId,
                    Instant.now().minus(30, ChronoUnit.DAYS).getEpochSecond()
                ).stream()
            ).toList();
    }

    public List<Message> getChannelMessages(String channelId, long oldest) {
        List<Message> allMessages = new ArrayList<>();
        String cursor = null;
        do {
            String finalCursor = cursor;
            ConversationsHistoryResponse response = null;
            try {
                response = slack.methods(token).conversationsHistory(req -> req
                    .channel(channelId)
                    .oldest(String.valueOf(oldest))
                    .cursor(finalCursor)
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SlackApiException e) {
                throw new RuntimeException(e);
            }
            if (!response.isOk()) {
                throw new ExternalApiException("Slack API 실패");
            }
            allMessages.addAll(response.getMessages());
            if (response.getResponseMetadata() == null) break;
            cursor = response.getResponseMetadata().getNextCursor();
        } while (cursor != null && !cursor.isEmpty());
        return allMessages;
    }
}
