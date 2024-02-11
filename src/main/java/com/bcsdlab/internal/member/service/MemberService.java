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
import com.bcsdlab.internal.member.controller.dto.request.MemberQueryRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberRegisterRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberUpdateRequest;
import com.bcsdlab.internal.member.controller.dto.response.MemberLoginResponse;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;
import com.bcsdlab.internal.member.exception.MemberException;
import com.bcsdlab.internal.track.Track;
import com.bcsdlab.internal.track.TrackRepository;

import static com.bcsdlab.internal.member.exception.MemberExceptionType.MEMBER_ALREADY_EXISTS_EMAIL;
import static com.bcsdlab.internal.member.exception.MemberExceptionType.MEMBER_ALREADY_EXISTS_STUDENT_NUMBER;
import static com.bcsdlab.internal.member.exception.MemberExceptionType.MEMBER_NOT_AUTHORIZED;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TrackRepository trackRepository;

    public MemberLoginResponse login(MemberLoginRequest request) {
        Member member = memberRepository.getByStudentNumber(request.studentNumber());
        checkAuthorized(member);
        member.matchPassword(request.password(), passwordEncoder);
        String token = jwtProvider.createToken(member);
        return new MemberLoginResponse(token);
    }

    @Transactional
    public void register(MemberRegisterRequest request) {
        memberRepository.findByStudentNumber(request.studentNumber()).ifPresent(member -> {
            throw new MemberException(MEMBER_ALREADY_EXISTS_STUDENT_NUMBER);
        });
        memberRepository.findByEmail(request.email()).ifPresent(member -> {
            throw new MemberException(MEMBER_ALREADY_EXISTS_EMAIL);
        });
        Track track = trackRepository.getById(request.trackId());
        Member member = request.toEntity(track);
        member.register(request.studentNumber(), request.password(), passwordEncoder);
        memberRepository.save(member);
    }

    public MemberResponse getById(Long memberId) {
        Member member = memberRepository.getById(memberId);
        return MemberResponse.from(member);
    }

    public Page<MemberResponse> getMembers(MemberQueryRequest request, Pageable pageable) {
        Track track = trackRepository.getById(request.trackId());
        Page<Member> members = memberRepository
            .searchMembers(request.name(), track.getId(), request.deleted(), request.authed(), pageable);
        return members.map(MemberResponse::from);
    }

    @Transactional
    public MemberResponse update(Long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.getById(memberId);
        Track track = trackRepository.getById(request.trackId());
        Member updated = request.toEntity(track);
        member.update(updated);
        return MemberResponse.from(member);
    }

    private void checkAuthorized(Member member) {
        if (!member.isAuthed() || member.isDeleted()) {
            throw new MemberException(MEMBER_NOT_AUTHORIZED);
        }
    }
}
