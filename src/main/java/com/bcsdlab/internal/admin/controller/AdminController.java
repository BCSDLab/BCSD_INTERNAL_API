package com.bcsdlab.internal.admin.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberUpdateRequest;
import com.bcsdlab.internal.admin.service.AdminService;
import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;

import static com.bcsdlab.internal.auth.Authority.ADMIN;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController implements AdminApi {

    private final AdminService adminService;

    @PutMapping("/members/{memberId}")
    public ResponseEntity<MemberResponse> changeMemberStatus(
        @Auth(permit = ADMIN) Long adminId,
        @PathVariable Long memberId,
        @RequestParam AdminMemberUpdateRequest request
    ) {
        var result = adminService.updateMember(memberId, request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Void> withdrawMember(
        @Auth(permit = ADMIN) Long adminId,
        @PathVariable Long memberId
    ) {
        adminService.withdrawMember(adminId, memberId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/members/{memberId}/accept")
    public ResponseEntity<Void> acceptMember(
        @Auth(permit = ADMIN) Long adminId,
        @PathVariable Long memberId
    ) {
        adminService.acceptMember(adminId, memberId);
        return ResponseEntity.ok().build();
    }
}
