package com.bcsdlab.internal.bbot.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcsdlab.internal.bbot.controller.dto.ThreadCreateRequest;
import com.bcsdlab.internal.bbot.controller.dto.ThreadResponse;
import com.bcsdlab.internal.bbot.service.ThreadService;
import com.bcsdlab.internal.global.slack.SlackService;
import com.slack.api.methods.SlackApiException;
import com.slack.api.model.Message;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class BBotController implements BBotApi {

    private final ThreadService threadService;
    private final SlackService slackService;

    @PostMapping("/b-bot/pull-request/thread")
    public ResponseEntity<Void> createThread(
        @RequestBody @Valid ThreadCreateRequest threadCreateRequest
    ) {
        threadService.createThread(threadCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/b-bot/pull-request/thread")
    public ResponseEntity<ThreadResponse> getThread(
        @RequestParam("pullRequestLink") String pullRequestLink
    ) {
        ThreadResponse threadResponse = threadService.getByPrLink(pullRequestLink);
        return ResponseEntity.ok(threadResponse);
    }

    @PostMapping("/b-bot/sync/slack/channal")
    public ResponseEntity<Void> syncSlackChannel(
    ) throws SlackApiException, IOException {
        slackService.syncSlackChannel();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/b-bot/sync/slack/message")
    public ResponseEntity<Void> syncSlackMessage(
    ) throws SlackApiException, IOException {
        slackService.syncSlackMessage();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/b-bot/slack/message")
    public ResponseEntity<List<Message>> getSlackMessage(
    ) throws SlackApiException, IOException {
        return ResponseEntity.ok(slackService.getChannelMessage("C4A8YJ66P"));
    }

    // @GetMapping("/b-bot/top-poster")
    // public ResponseEntity<Void> getTopFivePoster() throws SlackApiException, IOException {
    //
    // }
    //
    // @GetMapping("/b-bot/top-reactor")
    // public ResponseEntity<Void> getTopFiveReactor() throws SlackApiException, IOException {
    //
    // }
    //
    // @GetMapping("/b-bot/most-used-emojis")
    // public ResponseEntity<Void> getTopFiveUsedEmojis() throws SlackApiException, IOException {
    //
    // }
    //
    // @GetMapping("/b-bot/most-active-track-or-team")
    // public ResponseEntity<Void> getMostActiveTrackOrTeam() throws SlackApiException, IOException {
    //
    // }
}
