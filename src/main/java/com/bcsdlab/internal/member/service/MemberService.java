package com.bcsdlab.internal.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.auth.JwtProvider;
import com.bcsdlab.internal.auth.PasswordEncoder;
import com.bcsdlab.internal.member.Member;
import com.bcsdlab.internal.member.MemberRepository;
import com.bcsdlab.internal.member.controller.dto.MemberLoginRequest;
import com.bcsdlab.internal.member.controller.dto.MemberLoginResponse;
import com.bcsdlab.internal.member.controller.dto.MemberRegisterRequest;
import com.bcsdlab.internal.member.controller.dto.MemberResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
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

    public void register(MemberRegisterRequest request) {
        Member member = request.toEntity();
        member.register(request.studentNumber(), request.studentNumber(), passwordEncoder);
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse getById(Long memberId) {
        Member member = memberRepository.getById(memberId);
        return MemberResponse.from(member);
    }
}
