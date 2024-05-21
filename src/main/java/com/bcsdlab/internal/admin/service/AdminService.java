package com.bcsdlab.internal.admin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberCreateRequest;
import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberDeleteRequest;
import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberUpdateRequest;
import com.bcsdlab.internal.admin.controller.dto.response.AdminSlackSyncResponse;
import com.bcsdlab.internal.global.slack.SlackService;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;
import com.bcsdlab.internal.member.model.Member;
import com.bcsdlab.internal.member.model.MemberWithdraw;
import com.bcsdlab.internal.member.repository.MemberRepository;
import com.bcsdlab.internal.member.repository.MemberWithdrawRepository;
import com.bcsdlab.internal.track.Track;
import com.bcsdlab.internal.track.repository.TrackRepository;
import com.slack.api.model.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;
    private final TrackRepository trackRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberWithdrawRepository memberWithdrawRepository;
    private final SlackService slackService;

    public void acceptMember(Long memberId) {
        Member member = memberRepository.getById(memberId);
        member.accept();
    }

    public void withdrawMember(Long memberId, AdminMemberDeleteRequest request) {
        Member member = memberRepository.getById(memberId);
        memberWithdrawRepository.findByMemberId(memberId).ifPresent(memberWithdrawRepository::delete);
        MemberWithdraw memberWithdraw = MemberWithdraw.builder()
            .withdrawDate(LocalDateTime.now())
            .reason(request.reason())
            .member(member)
            .build();
        memberWithdrawRepository.save(memberWithdraw);
        member.withdraw();
    }

    public MemberResponse updateMember(Long memberId, AdminMemberUpdateRequest request) {
        Member member = memberRepository.getById(memberId);
        Track track = trackRepository.getById(request.trackId());
        Member updated = request.toEntity(track, member);
        if (!request.isDeleted()) {
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

    @Transactional
    public AdminSlackSyncResponse syncWithSlack() {
        List<User> users = slackService.getMembers();
        List<Member> members = memberRepository.findAll();
        int idSyncCount = 0;
        int imageSyncCount = 0;
        for (Member member : members) {
            User emailMatched = users.stream()
                .filter(user -> Objects.equals(user.getProfile().getEmail(), member.getEmail()))
                .findAny()
                .orElse(null);

            if (emailMatched != null) {
                idSyncCount++;
                member.updateSlackId(emailMatched.getId());
                if (emailMatched.isDeleted()) {
                    member.withdraw();
                }
            }

            User slackIdMatched = users.stream()
                .filter(user -> Objects.equals(user.getId(), member.getSlackId()))
                .findAny()
                .orElse(null);

            if (slackIdMatched != null) {
                imageSyncCount++;
                member.updateImage(slackIdMatched.getProfile().getImage512());
                if (slackIdMatched.isDeleted()) {
                    member.withdraw();
                }
            }
        }

        return new AdminSlackSyncResponse(idSyncCount, imageSyncCount);
    }
}
