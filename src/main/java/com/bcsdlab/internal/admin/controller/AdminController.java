package com.bcsdlab.internal.admin.controller;


import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberCreateRequest;
import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberDeleteRequest;
import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberUpdateRequest;
import com.bcsdlab.internal.admin.service.AdminService;
import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;

import static com.bcsdlab.internal.auth.Authority.ADMIN;
import static com.bcsdlab.internal.auth.Authority.MANAGER;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController implements AdminApi {

    private final AdminService adminService;

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(
        @Auth(permit = {MANAGER, ADMIN}) Long adminId,
        @RequestBody @Valid AdminMemberCreateRequest request
    ) {
        var memberId = adminService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + memberId)).build();
    }

    @PutMapping("/members/{memberId}")
    public ResponseEntity<MemberResponse> changeMemberStatus(
        @Auth(permit = {MANAGER, ADMIN}) Long adminId,
        @PathVariable Long memberId,
        @RequestBody @Valid AdminMemberUpdateRequest request
    ) {
        var result = adminService.updateMember(memberId, request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Void> withdrawMember(
        @Auth(permit = {MANAGER, ADMIN}) Long adminId,
        @PathVariable Long memberId,
        @RequestBody @Valid AdminMemberDeleteRequest adminMemberDeleteRequest
    ) {
        adminService.withdrawMember(memberId, adminMemberDeleteRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/members/{memberId}/accept")
    public ResponseEntity<Void> acceptMember(
        @Auth(permit = {MANAGER, ADMIN}) Long adminId,
        @PathVariable Long memberId
    ) {
        adminService.acceptMember(memberId);
        return ResponseEntity.ok().build();
    }
}
