package com.bcsdlab.internal.admin.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberCreateRequest;
import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberDeleteRequest;
import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberUpdateRequest;
import com.bcsdlab.internal.member.MemberRepository;
import com.bcsdlab.internal.member.MemberWithdrawRepository;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;
import com.bcsdlab.internal.member.model.Member;
import com.bcsdlab.internal.member.model.MemberWithdraw;
import com.bcsdlab.internal.track.Track;
import com.bcsdlab.internal.track.TrackRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;
    private final TrackRepository trackRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberWithdrawRepository memberWithdrawRepository;

    public void acceptMember(Long memberId) {
        Member member = memberRepository.getById(memberId);
        member.accept();
    }

    @Transactional
    public void withdrawMember(AdminMemberDeleteRequest adminMemberDeleteRequest) {
        Member member = memberRepository.getById(adminMemberDeleteRequest.id());
        if (!member.isDeleted()) {
            MemberWithdraw memberWithdraw = MemberWithdraw.builder()
                .withdrawDate(LocalDateTime.now())
                .reason(adminMemberDeleteRequest.reason())
                .member(member)
                .build();
            memberWithdrawRepository.save(memberWithdraw);
            member.withdraw();
        }
    }

    public MemberResponse updateMember(Long memberId, AdminMemberUpdateRequest request) {
        Member member = memberRepository.getById(memberId);
        Track track = trackRepository.getById(request.trackId());
        Member updated = request.toEntity(track);
        if (request.isDeleted() == false) {
            memberWithdrawRepository.deleteAllByMemberId(memberId);
        }
        member.updateAll(updated);
        return MemberResponse.from(member);
    }

    public Long createMember(AdminMemberCreateRequest request) {
        Track track = trackRepository.getById(request.trackId());
        Member member = request.toEntity(track);
        member.register(request.studentNumber(), request.password(), passwordEncoder);
        member.accept();
        memberRepository.save(member);
        return member.getId();
    }
}
