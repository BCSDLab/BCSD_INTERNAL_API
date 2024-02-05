package com.bcsdlab.internal.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.member.controller.dto.MemberLoginRequest;
import com.bcsdlab.internal.member.controller.dto.MemberLoginResponse;
import com.bcsdlab.internal.member.controller.dto.MemberRegisterRequest;
import com.bcsdlab.internal.member.controller.dto.MemberResponse;
import com.bcsdlab.internal.member.service.MemberService;

import static com.bcsdlab.internal.auth.Authority.ADMIN;
import static com.bcsdlab.internal.auth.Authority.MANAGER;
import static com.bcsdlab.internal.auth.Authority.NORMAL;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(
        @RequestBody @Valid MemberLoginRequest request
    ) {
        return ResponseEntity.ok(memberService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
        @RequestBody @Valid MemberRegisterRequest request
    ) {
        memberService.register(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMember(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId
    ) {
        return ResponseEntity.ok(memberService.getById(memberId));
    }
}
