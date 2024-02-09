package com.bcsdlab.internal.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.auth.JwtProvider;
import com.bcsdlab.internal.auth.PasswordEncoder;
import com.bcsdlab.internal.member.Member;
import com.bcsdlab.internal.member.MemberRepository;
import com.bcsdlab.internal.member.controller.dto.request.MemberFindAllRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberLoginRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberRegisterRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberUpdateRequest;
import com.bcsdlab.internal.member.controller.dto.response.MemberLoginResponse;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;
import com.bcsdlab.internal.member.exception.MemberException;

import static com.bcsdlab.internal.member.exception.MemberExceptionType.MEMBER_ALREADY_EXISTS;
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
        member.checkAuthorized();
        String token = jwtProvider.createToken(member);
        return new MemberLoginResponse(token);
    }

    @Transactional
    public void register(MemberRegisterRequest request) {
        memberRepository.findByStudentNumber(request.studentNumber()).ifPresent(member -> {
            throw new MemberException(MEMBER_ALREADY_EXISTS);
        });
        Member member = request.toEntity();
        member.register(request.studentNumber(), request.password(), passwordEncoder);
        memberRepository.save(member);
    }

    public MemberResponse getById(Long memberId) {
        Member member = memberRepository.getById(memberId);
        return MemberResponse.from(member);
    }

    public Page<MemberResponse> getMembers(MemberFindAllRequest request, Pageable pageable) {
        Page<Member> members = memberRepository
            .searchMembers(request.name(), request.deleted(), request.authorized(), pageable);
        return members.map(MemberResponse::from);
    }

    @Transactional
    public MemberResponse update(Long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.getById(memberId);
        Member updated = request.toEntity();
        member.update(updated);
        return MemberResponse.from(member);
    }
}
