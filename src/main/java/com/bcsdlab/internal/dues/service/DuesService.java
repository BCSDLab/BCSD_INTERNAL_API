package com.bcsdlab.internal.dues.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.dues.Dues;
import com.bcsdlab.internal.dues.DuesRepository;
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
}
