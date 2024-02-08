package com.bcsdlab.internal.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.auth.JwtProvider;
import com.bcsdlab.internal.auth.PasswordEncoder;
import com.bcsdlab.internal.member.Member;
import com.bcsdlab.internal.member.MemberRepository;
import com.bcsdlab.internal.member.controller.dto.request.MemberLoginRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberRegisterRequest;
import com.bcsdlab.internal.member.controller.dto.response.MemberLoginResponse;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public MemberLoginResponse login(MemberLoginRequest request) {
        Member member = memberRepository.getByStudentNumber(request.studentNumber());
        member.matchPassword(request.password(), passwordEncoder);
        String token = jwtProvider.createToken(member);
        return new MemberLoginResponse(token);
    }

    @Transactional
    public void register(MemberRegisterRequest request) {
        Member member = request.toEntity();
        member.register(request.studentNumber(), request.password(), passwordEncoder);
        memberRepository.save(member);
    }

    public MemberResponse getById(Long memberId) {
        Member member = memberRepository.getById(memberId);
        return MemberResponse.from(member);
    }

    public Page<MemberResponse> findAll(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        return members.map(MemberResponse::from);
    }
}
