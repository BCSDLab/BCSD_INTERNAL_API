package com.bcsdlab.internal.global.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bcsdlab.internal.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BirthdayScheduler {

    private final MemberService memberService;

    @Scheduled(cron = "0 0 0 * * *")
    public void celebrateBirthday() {
        memberService.celebrateBirthday();
    }
}
