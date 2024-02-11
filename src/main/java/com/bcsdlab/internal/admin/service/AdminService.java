package com.bcsdlab.internal.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberUpdateRequest;
import com.bcsdlab.internal.member.Member;
import com.bcsdlab.internal.member.MemberRepository;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;
import com.bcsdlab.internal.track.Track;
import com.bcsdlab.internal.track.TrackRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;
    private final TrackRepository trackRepository;

    public void acceptMember(Long memberId) {
        Member member = memberRepository.getById(memberId);
        member.accept();
    }

    public void withdrawMember(Long memberId) {
        Member member = memberRepository.getById(memberId);
        member.withdraw();
    }

    public MemberResponse updateMember(Long memberId, AdminMemberUpdateRequest request) {
        Member member = memberRepository.getById(memberId);
        Track track = trackRepository.getById(request.trackId());
        Member updated = request.toEntity(track);
        member.updateAll(updated);
        return MemberResponse.from(member);
    }
}
