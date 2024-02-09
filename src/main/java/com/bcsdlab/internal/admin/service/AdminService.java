package com.bcsdlab.internal.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberUpdateRequest;
import com.bcsdlab.internal.member.Member;
import com.bcsdlab.internal.member.MemberRepository;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    public void acceptMember(Long adminId, Long memberId) {
        Member admin = memberRepository.getById(adminId);
        Member member = memberRepository.getById(memberId);
        admin.accept(member);
    }

    public void withdrawMember(Long adminId, Long memberId) {
        Member admin = memberRepository.getById(adminId);
        Member member = memberRepository.getById(memberId);
        admin.withdraw(member);
    }

    public MemberResponse updateMember(Long memberId, AdminMemberUpdateRequest request) {
        Member member = memberRepository.getById(memberId);
        Member updated = request.toEntity();
        member.update(updated);
        return MemberResponse.from(member);
    }
}
