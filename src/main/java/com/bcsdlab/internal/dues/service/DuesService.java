package com.bcsdlab.internal.dues.service;

import static com.bcsdlab.internal.dues.exception.DuesExceptionType.DUES_ALREADY_EXIST;
import static com.bcsdlab.internal.dues.exception.DuesExceptionType.DUES_NOT_FOUND;

import java.io.IOException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.dues.Dues;
import com.bcsdlab.internal.dues.DuesStatus;
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
import com.bcsdlab.internal.global.google.service.GoogleSheetsService;
import com.bcsdlab.internal.global.slack.SlackService;
import com.bcsdlab.internal.global.slack.model.SlackNotificationFactory;
import com.bcsdlab.internal.job.repository.JobRepository;
import com.bcsdlab.internal.member.model.Member;
import com.bcsdlab.internal.member.repository.MemberRepository;
import com.bcsdlab.internal.track.Track;
import com.bcsdlab.internal.track.repository.TrackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DuesService {

    private final DuesRepository duesRepository;
    private final MemberRepository memberRepository;
    private final TrackRepository trackRepository;
    private final JobRepository jobRepository;
    private final SlackNotificationFactory slackNotificationFactory;
    private final SlackService slackService;
    private final GoogleSheetsService googleSheetsService;
    @Value("${google.spreadsheet.dues-payment}") String spreadsheetId;

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

    @Transactional
    public void syncGoogleSheet() throws IOException {
        final int TRACK_INDEX = 0;
        final int NAME_INDEX = 1;
        // final int NOTE_INDEX = 2;
        final int MONTH_START_INDEX = 3;

        YearMonth current = YearMonth.now();
        // 1년 전 1월부터 시작 (e.g. 2025년 03월에 동기화 실행할 경우, 2024년 1월 ~ 2024년 03월 데이터 동기화)
        YearMonth start = current.withMonth(1).minusYears(1);

        List<List<Object>> googleSheetUsers = new ArrayList<>();
        for (YearMonth date = start; !date.isAfter(current); date = date.plusMonths(1)) {
            // 1월일 때만(년도가 바뀔때만) 데이터를 가져옴
            if (date.getMonthValue() == 1) {
                googleSheetUsers = getGoogleSheetUsers(date.getYear());
            }

            for (List<Object> googleSheetUser : googleSheetUsers) {
                String name = googleSheetUser.get(NAME_INDEX).toString();
                String trackName = googleSheetUser.get(TRACK_INDEX).toString().replace("-", "");
                Track track = trackRepository.getByName(trackName);
                // String note = googleSheetUser.get(NOTE_INDEX).toString();

                Member member = memberRepository.getByNameAndTrackId(name, track.getId());

                String duesStringStatus = googleSheetUser.get(MONTH_START_INDEX + date.getMonthValue() - 1).toString();

                // 납부 정보가 없을 경우 제거
                if (duesStringStatus == null || duesStringStatus.isBlank()) {
                    duesRepository.findByDateAndMemberId(date, member.getId())
                        .ifPresent(duesRepository::delete);
                    continue;
                }

                // 납부 정보가 있을 경우 동기화
                DuesStatus duesStatus = switch (duesStringStatus) {
                    case "O" -> DuesStatus.PAID;
                    case "-" -> DuesStatus.SKIP;
                    default -> DuesStatus.NOT_PAID;
                };

                Optional<Dues> dues = duesRepository.findByDateAndMemberId(date, member.getId());
                if (dues.isEmpty()) {
                    duesRepository.save(new Dues(null, member, date, duesStatus, false));
                } else {
                    Dues existingDues = dues.get();
                    existingDues.update(duesStatus, existingDues.getMemo());
                }
            }
        }
    }

    private List<List<Object>> getGoogleSheetUsers(int year) throws IOException {
        final String range = "!C5:Q";

        return googleSheetsService.readSheetData(spreadsheetId, year + range);
    }
}
