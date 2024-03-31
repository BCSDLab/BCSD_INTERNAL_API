package com.bcsdlab.internal.bbot.service;

import static com.bcsdlab.internal.bbot.exception.ThreadExceptionType.THREAD_NOT_FOUND;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bcsdlab.internal.bbot.controller.dto.ThreadCreateRequest;
import com.bcsdlab.internal.bbot.controller.dto.ThreadResponse;
import com.bcsdlab.internal.bbot.exception.ThreadException;
import com.bcsdlab.internal.bbot.model.Thread;
import com.bcsdlab.internal.bbot.repository.ThreadRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ThreadService {

    private final ThreadRepository threadRepository;

    public void createThread(ThreadCreateRequest threadCreateRequest) {
        for(String reviewer: threadCreateRequest.reviewers()) {
            Thread thread = Thread.builder()
                .ts(threadCreateRequest.ts())
                .prLink(threadCreateRequest.pullRequestLink())
                .reviewer(reviewer)
                .writer(threadCreateRequest.writer())
                .build();
            threadRepository.save(thread);
        }
    }

    public ThreadResponse getByPrLink(String pullRequestLink) {
        List<Thread> threads = threadRepository.findAllByPrLink(pullRequestLink);
        if(threads.isEmpty()) throw new ThreadException(THREAD_NOT_FOUND);
        return ThreadResponse.from(threads);
    }
}
