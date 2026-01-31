package com.bcsdlab.internal.admin.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberCreateRequest;
import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberDeleteRequest;
import com.bcsdlab.internal.admin.controller.dto.request.AdminMemberUpdateRequest;
import com.bcsdlab.internal.auth.Authority;
import com.bcsdlab.internal.global.google.service.GoogleSheetsService;
import com.bcsdlab.internal.global.slack.SlackService;
import com.bcsdlab.internal.member.MemberStatus;
import com.bcsdlab.internal.member.MemberType;
import com.bcsdlab.internal.member.controller.dto.response.MemberResponse;
import com.bcsdlab.internal.member.model.Member;
import com.bcsdlab.internal.member.model.MemberWithdraw;
import com.bcsdlab.internal.member.repository.MemberRepository;
import com.bcsdlab.internal.member.repository.MemberWithdrawRepository;
import com.bcsdlab.internal.track.Track;
import com.bcsdlab.internal.track.repository.TrackRepository;
import com.slack.api.methods.SlackApiException;
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
    private final GoogleSheetsService googleSheetsService;

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

    public void updateMemberActive(Long memberId, Boolean isActive) {
        Member member = memberRepository.getById(memberId);
        member.updateActive(isActive);
    }

    public Long createMember(AdminMemberCreateRequest request) {
        Track track = trackRepository.getById(request.trackId());
        Member member = request.toEntity(track);
        member.register(request.studentNumber(), request.password(), passwordEncoder, request.birthday());
        member.accept();
        memberRepository.save(member);
        return member.getId();
    }

    public Map<String, Member> getAllMemberMap() {
        List<Member> members = memberRepository.findAll();
        Map<String, Member> memberMap = new HashMap<>(members.size());
        for (Member member: members) {
            memberMap.put(member.getEmail(), member);
        }
        return memberMap;
    }

    public Map<String, Track> getAllTrackMap() {
        List<Track> tracks = trackRepository.findAll();
        Map<String, Track> trackMap = new HashMap<>(tracks.size());
        for (Track track: tracks) {
            trackMap.put(track.getName(), track);
        }
        return trackMap;
    }

    @Transactional
    public void syncSlackMember() throws SlackApiException, IOException {
        List<User> slackUsers = slackService.getAllUsers();
        List<List<Object>> googleSheetUsers = googleSheetsService.readSheet();
        Map<String, Member> memberMap = getAllMemberMap();
        Set<String> googleSheetUserEmailSet = new HashSet<>(googleSheetUsers.size());
        Map<String, Track> trackMap = getAllTrackMap();
        final int EMAIL_INDEX = 10;
        final int NAME_INDEX = 5;
        final int TRACK_INDEX = 2;
        final int COMPANY_INDEX = 6;
        final int DEPARTMENT_INDEX = 7;
        final int STUDENT_NUMBER_INDEX = 8;
        final int PHONE_NUMBER_INDEX = 9;
        final int GITHUB_NAME_INDEX = 12;
        final int BIRTHDATE_INDEX = 13;
        final int MEMBER_TYPE_INDEX = 3;
        final int STATUS_INDEX = 4;
        for (List<Object> googleSheetUser: googleSheetUsers) {
            googleSheetUserEmailSet.add(googleSheetUser.get(EMAIL_INDEX).toString());
            Member member = memberMap.getOrDefault(googleSheetUser.get(EMAIL_INDEX), Member.builder()
                .email(googleSheetUser.get(EMAIL_INDEX).toString())
                .authority(Authority.NORMAL)
                .build());
            member.updateMember(
                googleSheetUser.get(NAME_INDEX).toString(),
                trackMap.get(googleSheetUser.get(TRACK_INDEX).toString()),
                googleSheetUser.get(COMPANY_INDEX).toString(),
                googleSheetUser.get(DEPARTMENT_INDEX).toString(),
                googleSheetUser.get(STUDENT_NUMBER_INDEX).toString(),
                googleSheetUser.get(PHONE_NUMBER_INDEX).toString(),
                googleSheetUser.size() > GITHUB_NAME_INDEX? googleSheetUser.get(GITHUB_NAME_INDEX).toString(): null,
                googleSheetUser.size() > BIRTHDATE_INDEX? LocalDate.parse(googleSheetUser.get(BIRTHDATE_INDEX).toString()): null,
                MemberType.from(googleSheetUser.get(MEMBER_TYPE_INDEX).toString()),
                MemberStatus.fromView(googleSheetUser.get(STATUS_INDEX).toString()),
                false
            );
            Member savedMember = memberRepository.save(member);
            memberMap.put(savedMember.getEmail(), savedMember);
        }
        for (User slackUser: slackUsers) {
            String email = slackUser.getProfile().getEmail();
            if (memberMap.containsKey(email)) {
                Member member = memberMap.get(email);
                member.setSlackId(slackUser.getId());
                member.setProfileImage(slackUser.getProfile().getImage512());
            }
        }
        for (Member member : memberMap.values()) {
            if (!googleSheetUserEmailSet.contains(member.getEmail())) {
                memberRepository.deleteById(member.getId());
            }
        }
    }
}
