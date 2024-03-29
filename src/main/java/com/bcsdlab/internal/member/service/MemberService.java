package com.bcsdlab.internal.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.auth.JwtProvider;
import com.bcsdlab.internal.global.ses.CertificationCodeGenerator;
import com.bcsdlab.internal.global.ses.MailSender;
import com.bcsdlab.internal.member.MemberRepository;
import com.bcsdlab.internal.member.PasswordResetToken;
import com.bcsdlab.internal.member.PasswordResetTokenRepository;
import com.bcsdlab.internal.member.controller.dto.request.MemberEmailRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberLoginRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberQueryRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberRegisterRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberResetPasswordRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberResetTokenRequest;
import com.bcsdlab.internal.member.controller.dto.request.MemberUpdateRequest;
import com.bcsdlab.internal.member.controller.dto.response.MemberLoginResponse;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;
import com.bcsdlab.internal.member.exception.MemberException;
import com.bcsdlab.internal.member.model.Member;
import com.bcsdlab.internal.track.Track;
import com.bcsdlab.internal.track.TrackRepository;

import static com.bcsdlab.internal.member.exception.MemberExceptionType.CERTIFICATION_CODE_NOT_MATCH;
import static com.bcsdlab.internal.member.exception.MemberExceptionType.MEMBER_ALREADY_EXISTS_EMAIL;
import static com.bcsdlab.internal.member.exception.MemberExceptionType.MEMBER_ALREADY_EXISTS_STUDENT_NUMBER;
import static com.bcsdlab.internal.member.exception.MemberExceptionType.MEMBER_NOT_AUTHORIZED;
import static com.bcsdlab.internal.member.exception.MemberExceptionType.PASSWORD_EMPTY;
import static com.bcsdlab.internal.member.exception.MemberExceptionType.PASSWORD_SAME_AS_BEFORE;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final TrackRepository trackRepository;
    private final MailSender mailSender;

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
        Page<Member> members = memberRepository
            .searchMembers(request.name(), request.trackId(), request.deleted(), request.authed(), pageable);
        return members.map(MemberResponse::from);
    }

    @Transactional
    public MemberResponse update(Long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.getById(memberId);
        validateDuplication(request, member);
        Track track = trackRepository.getById(request.trackId());
        Member updated = request.toEntity(track, member.getMemberType(), member.getAuthority());
        member.update(updated);
        return MemberResponse.from(member);
    }

    private void validateDuplication(MemberUpdateRequest request, Member member) {
        memberRepository.findByStudentNumber(request.studentNumber())
            .ifPresent(it -> {
                if (!it.getId().equals(member.getId())) {
                    throw new MemberException(MEMBER_ALREADY_EXISTS_STUDENT_NUMBER);
                }
            });

        memberRepository.findByEmail(request.email())
            .ifPresent(it -> {
                if (!it.getId().equals(member.getId())) {
                    throw new MemberException(MEMBER_ALREADY_EXISTS_EMAIL);
                }
            });
    }

    private void checkAuthorized(Member member) {
        if (member == null || !member.isAuthed() || member.isDeleted()) {
            throw new MemberException(MEMBER_NOT_AUTHORIZED);
        }
    }

    @Transactional
    public void requestResetPassword(MemberEmailRequest request) {
        Member foundMember = memberRepository.getByEmail(request.email());
        String certificationCode = CertificationCodeGenerator.get();
        PasswordResetToken token = PasswordResetToken.create(foundMember.getId(), certificationCode);
        passwordResetTokenRepository.save(token);
        mailSender.send(
            MailSender.PASSWORD_RESET_SUBJECT,
            request.email(),
            certificationCode
        );
    }

    public void certificateResetToken(MemberResetTokenRequest request) {
        Member foundMember = memberRepository.getByEmail(request.email());
        validateToken(foundMember.getId(), request.token());
    }

    private void validateToken(Long memberId, String token) {
        PasswordResetToken foundToken = passwordResetTokenRepository.getById(memberId);
        if (!foundToken.getCertificationCode().equals(token)) {
            throw new MemberException(CERTIFICATION_CODE_NOT_MATCH);
        }
    }

    @Transactional
    public void resetPassword(MemberResetPasswordRequest request) {
        Member foundMember = memberRepository.getByEmail(request.email());
        validateToken(foundMember.getId(), request.token());
        validateNewPassword(request.password(), foundMember);
        foundMember.resetPassword(request.password(), passwordEncoder);
        passwordResetTokenRepository.deleteById(foundMember.getId());
    }

    private void validateNewPassword(String password, Member foundMember) {
        if (passwordEncoder.matches(password, foundMember.getPassword())) {
            throw new MemberException(PASSWORD_SAME_AS_BEFORE);
        }
        if (password.isEmpty()) {
            throw new MemberException(PASSWORD_EMPTY);
        }
    }
}
