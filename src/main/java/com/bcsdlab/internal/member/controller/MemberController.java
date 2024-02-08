package com.bcsdlab.internal.member.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.global.controller.dto.PageResponse;
import com.bcsdlab.internal.member.controller.dto.request.MemberLoginRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberRegisterRequest;
import com.bcsdlab.internal.member.controller.dto.response.MemberLoginResponse;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;
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
    public ResponseEntity<PageResponse<MemberResponse>> getAll(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId,
        @PageableDefault() Pageable pageable
    ) {
        var result = memberService.findAll(pageable);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMemberById(
        @Auth(permit = {ADMIN}) Long id,
        @PathVariable Long memberId
    ) {
        var result = memberService.getById(memberId);
        return ResponseEntity.ok(result);
    }
}
