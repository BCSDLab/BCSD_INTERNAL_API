package com.bcsdlab.internal.dues.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.dues.Dues;
import com.bcsdlab.internal.dues.DuesRepository;
import com.bcsdlab.internal.dues.DuesStatus;
import com.bcsdlab.internal.dues.controller.dto.request.DuesUpdateRequest;
import com.bcsdlab.internal.dues.controller.dto.response.DuesGroupResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DuesService {

    private final DuesRepository duesRepository;

    public DuesGroupResponse getAll() {
        List<Dues> dues = duesRepository.findAll();
        return DuesGroupResponse.of(dues);
    }

    @Transactional
    public void updateDues(Long duesId, DuesUpdateRequest request) {
        Dues dues = duesRepository.getById(duesId);
        dues.update(request.memo(), DuesStatus.from(request.status()));
        duesRepository.save(dues);
    }
}
