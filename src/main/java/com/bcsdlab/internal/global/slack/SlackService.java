package com.bcsdlab.internal.global.slack;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bcsdlab.internal.global.exception.ExternalApiException;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.users.UsersListRequest;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.User;

@Service
public class SlackService {

    private final Slack slack;
    private final String token;

    public SlackService(
        Slack slack,
        @Value("${slack.api.token}") String token
    ) {
        this.slack = slack;
        this.token = token;
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
}
