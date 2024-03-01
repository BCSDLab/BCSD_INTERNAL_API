package com.bcsdlab.internal.dues.service;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.dues.Dues;
import com.bcsdlab.internal.dues.DuesRepository;
import com.bcsdlab.internal.dues.controller.dto.request.DuesCreateRequest;
import com.bcsdlab.internal.dues.controller.dto.request.DuesDeleteQueryRequest;
import com.bcsdlab.internal.dues.controller.dto.request.DuesQueryRequest;
import com.bcsdlab.internal.dues.controller.dto.request.DuesUpdateQueryRequest;
import com.bcsdlab.internal.dues.controller.dto.request.DuesUpdateRequest;
import com.bcsdlab.internal.dues.controller.dto.response.DuesGroupResponse;
import com.bcsdlab.internal.dues.controller.dto.response.DuesResponse;
import com.bcsdlab.internal.dues.exception.DuesException;
import com.bcsdlab.internal.member.Member;
import com.bcsdlab.internal.member.MemberRepository;

import static com.bcsdlab.internal.dues.exception.DuesExceptionType.DUES_ALREADY_EXIST;
import static com.bcsdlab.internal.dues.exception.DuesExceptionType.DUES_NOT_FOUND;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DuesService {

    private final DuesRepository duesRepository;
    private final MemberRepository memberRepository;

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
}
