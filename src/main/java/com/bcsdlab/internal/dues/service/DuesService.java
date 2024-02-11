package com.bcsdlab.internal.dues.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.dues.Dues;
import com.bcsdlab.internal.dues.DuesRepository;
import com.bcsdlab.internal.dues.controller.dto.request.DuesCreateRequest;
import com.bcsdlab.internal.dues.controller.dto.request.DuesFindRequest;
import com.bcsdlab.internal.dues.controller.dto.request.DuesUpdateRequest;
import com.bcsdlab.internal.dues.controller.dto.response.DuesGroupResponse;
import com.bcsdlab.internal.dues.controller.dto.response.DuesResponse;
import com.bcsdlab.internal.dues.exception.DuesException;
import com.bcsdlab.internal.member.Member;
import com.bcsdlab.internal.member.MemberRepository;

import static com.bcsdlab.internal.dues.exception.DuesExceptionType.DUES_ALREADY_EXIST;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DuesService {

    private final DuesRepository duesRepository;
    private final MemberRepository memberRepository;

    public DuesGroupResponse getAll(DuesFindRequest request) {
        List<Member> members = memberRepository.findAllByIsDeletedFalse();
        List<Dues> dues = duesRepository.searchDues(request.year(), request.track());
        return DuesGroupResponse.of(request.year(), members, dues);
    }

    @Transactional
    public DuesResponse updateDues(Long duesId, DuesUpdateRequest request) {
        Dues dues = duesRepository.getById(duesId);
        dues.update(request.status(), request.memo());
        duesRepository.save(dues);
        return DuesResponse.from(dues);
    }

    @Transactional
    public DuesResponse create(DuesCreateRequest request) {
        duesRepository.findByDateAndMemberId(request.date(), request.memberId())
            .ifPresent(dues -> {
                throw new DuesException(DUES_ALREADY_EXIST);
            });
        Member member = memberRepository.getById(request.memberId());
        Dues dues = request.toEntity(member);
        duesRepository.save(dues);
        return DuesResponse.from(dues);
    }
}
