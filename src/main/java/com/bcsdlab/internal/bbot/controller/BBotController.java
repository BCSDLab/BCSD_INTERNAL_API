package com.bcsdlab.internal.bbot.controller;

import static com.bcsdlab.internal.auth.Authority.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.bbot.controller.dto.ThreadCreateRequest;
import com.bcsdlab.internal.bbot.controller.dto.ThreadResponse;
import com.bcsdlab.internal.bbot.service.ThreadService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class BBotController implements BBotApi {

    private final ThreadService threadService;

    @PostMapping("/b-bot/pull-request/thread")
    public ResponseEntity<Void> createThread(
        @Auth(permit = {MANAGER, ADMIN, NORMAL}) Long adminId,
        @RequestBody @Valid ThreadCreateRequest threadCreateRequest
    ) {
        threadService.createThread(threadCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/b-bot/pull-request/thread")
    public ResponseEntity<ThreadResponse> getThread(
        @Auth(permit = {MANAGER, ADMIN, NORMAL}) Long adminId,
        @RequestParam("pullRequestLink") String pullRequestLink
    ) {
        ThreadResponse threadResponse = threadService.getByPrLink(pullRequestLink);
        return ResponseEntity.ok(threadResponse);
    }
}
