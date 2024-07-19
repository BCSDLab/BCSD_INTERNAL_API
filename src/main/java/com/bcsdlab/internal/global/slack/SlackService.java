package com.bcsdlab.internal.global.slack;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.dues.MemberDuesCount;
import com.bcsdlab.internal.dues.controller.dto.request.SendSlackMessage;
import com.bcsdlab.internal.global.exception.ExternalApiException;
import com.bcsdlab.internal.global.slack.model.SlackChannel;
import com.bcsdlab.internal.global.slack.model.SlackMessage;
import com.bcsdlab.internal.global.slack.model.SlackNotificationFactory;
import com.bcsdlab.internal.global.slack.repository.SlackChannelRepository;
import com.bcsdlab.internal.global.slack.repository.SlackMessageRepository;
import com.bcsdlab.internal.member.model.Member;
import com.bcsdlab.internal.member.repository.MemberRepository;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.users.UsersListRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.Message;
import com.slack.api.model.User;
import com.slack.api.webhook.Payload;

@Service
public class SlackService {

    private final Slack slack;
    private final String token;
    private final SlackNotificationFactory slackNotificationFactory;
    private final SlackChannelRepository slackChannelRepository;
    private final SlackMessageRepository slackMessageRepository;
    private final MemberRepository memberRepository;
    private final String notification;
    private final String bBot;

    public SlackService(
        Slack slack,
        SlackNotificationFactory slackNotificationFactory,
        SlackChannelRepository slackChannelRepository,
        SlackMessageRepository slackMessageRepository,
        MemberRepository memberRepository,
        @Value("${slack.api.token}") String token,
        SlackMessageRepository slackMessageRepository1, MemberRepository memberRepository1, @Value("${slack.notification}") String notification,
        @Value("{slack.bbot-test}") String bBot
    ) {
        this.slack = slack;
        this.token = token;
        this.slackNotificationFactory = slackNotificationFactory;
        this.slackChannelRepository = slackChannelRepository;
        this.slackMessageRepository = slackMessageRepository1;
        this.memberRepository = memberRepository1;
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

    public void syncSlackChannel() {
        try {
            slack.methods(token).conversationsList(req -> req)
                .getChannels()
                .stream()
                .filter(Conversation::isMember)
                .forEach(it -> {
                    Optional<SlackChannel> slackChannel = slackChannelRepository.findByChannelId(it.getId());
                    if (!slackChannel.isPresent()) {
                        slackChannelRepository.save(
                            SlackChannel.builder()
                            .channelName(it.getName())
                            .channelId(it.getId())
                            .isPublic(!it.isPrivate())
                            .build()
                        );
                    }
                });
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SlackApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void syncSlackMessage() {
        List<SlackChannel> slackChannels = slackChannelRepository.findAll();
        for (SlackChannel slackChannel: slackChannels) {
            try {
                syncChannelMessage(slackChannel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private long getOldest(int hours) {
        Instant hoursAgo = Instant.now().minus(hours, ChronoUnit.HOURS);
        return hoursAgo.getEpochSecond();
    }

    public List<Message> getChannelMessage(String channelId) throws IOException {
        BigDecimal maxTs = slackMessageRepository.findMaxTs();
        String oldestTs;
        if (maxTs == null) {
            oldestTs = String.valueOf(getOldest(24));
        }else {
            oldestTs = maxTs.toString();
        }
        try {
            ConversationsHistoryResponse response = slack.methods(token).conversationsHistory(req -> req
                .channel(channelId)
                .oldest(oldestTs)
            );
            if (response.isOk()) {
                return response.getMessages();
            } else {
                throw new IOException("Slack API error: " + response.getError());
            }
        } catch (IOException e) {
            throw new IOException("Error retrieving messages from Slack API: " + e.getMessage(), e);
        } catch (SlackApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void syncChannelMessage(SlackChannel slackChannel) throws IOException {
        BigDecimal maxTs = slackMessageRepository.findMaxTs();
        String oldestTs;
        if (maxTs == null) {
            oldestTs = String.valueOf(getOldest(24));
        }else {
            oldestTs = maxTs.toString();
        }
        try {
            ConversationsHistoryResponse response = slack.methods(token).conversationsHistory(req -> req
                .channel(slackChannel.getChannelId())
                .oldest(oldestTs)
            );
            if (response.isOk()) {
                for (Message message: response.getMessages()) {
                    Optional<Member> member = memberRepository.findBySlackId(message.getUser());
                    slackMessageRepository.save(
                        SlackMessage.builder()
                            .content(message.getText())
                            .slackChannel(slackChannel)
                            .ts(new BigDecimal(message.getTs()))
                            .member(member.isPresent()? member.get(): null)
                            .build()
                    );
                }
            } else {
                throw new IOException("Slack API error: " + response.getError());
            }
        } catch (IOException e) {
            throw new IOException("Error retrieving messages from Slack API: " + e.getMessage(), e);
        } catch (SlackApiException e) {
            throw new RuntimeException(e);
        }
    }
}
