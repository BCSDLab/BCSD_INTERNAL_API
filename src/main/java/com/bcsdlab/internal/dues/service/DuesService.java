package com.bcsdlab.internal.dues.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.dues.Dues;
import com.bcsdlab.internal.dues.DuesRepository;
import com.bcsdlab.internal.dues.controller.dto.request.DuesCreateRequest;
import com.bcsdlab.internal.dues.controller.dto.request.DuesUpdateRequest;
import com.bcsdlab.internal.dues.controller.dto.response.DuesGroupResponse;
import com.bcsdlab.internal.dues.controller.dto.response.DuesResponse;
import com.bcsdlab.internal.member.Member;
import com.bcsdlab.internal.member.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DuesService {

    private final DuesRepository duesRepository;
    private final MemberRepository memberRepository;

    public DuesGroupResponse getAll() {
        List<Dues> dues = duesRepository.findAll();
        return DuesGroupResponse.of(dues);
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
        Member member = memberRepository.getById(request.memberId());
        Dues dues = request.toEntity(member);
        duesRepository.save(dues);
        return DuesResponse.from(dues);
    }
}
