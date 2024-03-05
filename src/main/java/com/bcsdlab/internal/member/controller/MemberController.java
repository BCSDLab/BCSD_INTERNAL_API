package com.bcsdlab.internal.member.controller;

import static com.bcsdlab.internal.auth.Authority.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.global.controller.dto.PageResponse;
import com.bcsdlab.internal.member.controller.dto.request.MemberEmailRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberLoginRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberQueryRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberRegisterRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberResetPasswordRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberResetTokenRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberUpdateRequest;
import com.bcsdlab.internal.member.controller.dto.response.MemberLoginResponse;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;
import com.bcsdlab.internal.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController implements MemberApi {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(
        @RequestBody @Valid MemberLoginRequest request
    ) {
        var result = memberService.login(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
        @RequestBody @Valid MemberRegisterRequest request
    ) {
        memberService.register(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMemberMe(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId
    ) {
        var result = memberService.getById(memberId);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<PageResponse<MemberResponse>> getMembers(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId,
        @PageableDefault Pageable pageable,
        @ModelAttribute MemberQueryRequest request
    ) {
        var result = memberService.getMembers(request, pageable);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMemberById(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long id,
        @PathVariable Long memberId
    ) {
        var result = memberService.getById(memberId);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<MemberResponse> updateMemberMe(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId,
        @RequestBody @Valid MemberUpdateRequest request
    ) {
        var result = memberService.update(memberId, request);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> requestResetPassword(
        @RequestBody @Valid MemberEmailRequest request
    ) {
        memberService.requestResetPassword(request);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> certificateResetToken(MemberResetTokenRequest request) {
        memberService.certificateResetToken(request);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> resetPassword(MemberResetPasswordRequest request) {
        memberService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}
