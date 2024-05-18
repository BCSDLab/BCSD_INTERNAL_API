package com.bcsdlab.internal.dues.service;

import static com.bcsdlab.internal.dues.exception.DuesExceptionType.DUES_ALREADY_EXIST;
import static com.bcsdlab.internal.dues.exception.DuesExceptionType.DUES_NOT_FOUND;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.dues.Dues;
import com.bcsdlab.internal.dues.MemberDuesCount;
import com.bcsdlab.internal.dues.controller.dto.request.DuesCreateRequest;
import com.bcsdlab.internal.dues.controller.dto.request.DuesDeleteQueryRequest;
import com.bcsdlab.internal.dues.controller.dto.request.DuesQueryRequest;
import com.bcsdlab.internal.dues.controller.dto.request.DuesUpdateQueryRequest;
import com.bcsdlab.internal.dues.controller.dto.request.DuesUpdateRequest;
import com.bcsdlab.internal.dues.controller.dto.request.SendSlackMessage;
import com.bcsdlab.internal.dues.controller.dto.response.DuesGroupResponse;
import com.bcsdlab.internal.dues.controller.dto.response.DuesResponse;
import com.bcsdlab.internal.dues.exception.DuesException;
import com.bcsdlab.internal.dues.repository.DuesRepository;
import com.bcsdlab.internal.global.slack.SlackService;
import com.bcsdlab.internal.global.slack.model.SlackNotificationFactory;
import com.bcsdlab.internal.job.repository.JobRepository;
import com.bcsdlab.internal.member.model.Member;
import com.bcsdlab.internal.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DuesService {

    private final DuesRepository duesRepository;
    private final MemberRepository memberRepository;
    private final JobRepository jobRepository;
    private final SlackNotificationFactory slackNotificationFactory;
    private final SlackService slackService;

    public DuesGroupResponse getAll(DuesQueryRequest request) {
        List<Member> members = memberRepository.findAllByIsDeletedFalse();
        List<Dues> dues = duesRepository.searchDues(request.year(), request.trackId());
        return DuesGroupResponse.of(request.year(), members, dues);
    }

    @Transactional
    public void delete(DuesDeleteQueryRequest queryRequest) {
        Dues dues = duesRepository.findByDateAndMemberId(
                YearMonth.of(queryRequest.year(), queryRequest.month()), queryRequest.memberId())
            .orElseThrow(() -> new DuesException(DUES_NOT_FOUND));
        duesRepository.delete(dues);
    }

    @Transactional
    public DuesResponse updateDues(DuesUpdateQueryRequest queryRequest, DuesUpdateRequest updateRequest) {
        Dues dues = duesRepository.findByDateAndMemberId(
                YearMonth.of(queryRequest.year(), queryRequest.month()), queryRequest.memberId())
            .orElseThrow(() -> new DuesException(DUES_NOT_FOUND));
        dues.update(updateRequest.status(), updateRequest.memo());
        duesRepository.save(dues);
        return DuesResponse.from(dues);
    }

    @Transactional
    public DuesResponse create(DuesCreateRequest request) {
        duesRepository.findByDateAndMemberId(
                YearMonth.of(request.year(), request.month()),
                request.memberId()
            )
            .ifPresent(dues -> {
                throw new DuesException(DUES_ALREADY_EXIST);
            });
        Member member = memberRepository.getById(request.memberId());
        Dues dues = request.toEntity(member);
        duesRepository.save(dues);
        return DuesResponse.from(dues);
    }

    public void sendSlackMessage(SendSlackMessage request) {
        Member president = jobRepository.getActiveJobByType("회장").getMember();
        Member vicePresident = jobRepository.getActiveJobByType("부회장").getMember();
        slackService.sendDuesNotificationByGlobal(
            request,
            president.getSlackId(),
            vicePresident.getSlackId()
        );
    }

    public void sendDuesDM() {
        List<MemberDuesCount> dues = duesRepository.countNotPaidDuesByMember();
        Member president = jobRepository.getActiveJobByType("회장").getMember();
        Member vicePresident = jobRepository.getActiveJobByType("부회장").getMember();
        slackService.sendDuesNotificationByDM(
            dues,
            president.getSlackId(),
            vicePresident.getSlackId()
        );
    }
}
