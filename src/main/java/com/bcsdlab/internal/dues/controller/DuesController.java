package com.bcsdlab.internal.dues.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.dues.controller.dto.response.DuesGroupResponse;
import com.bcsdlab.internal.dues.service.DuesService;

import static com.bcsdlab.internal.auth.Authority.ADMIN;
import static com.bcsdlab.internal.auth.Authority.MANAGER;
import static com.bcsdlab.internal.auth.Authority.NORMAL;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dues")
public class DuesController {

    private final DuesService duesService;

    @GetMapping
    public ResponseEntity<DuesGroupResponse> getAll(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId

    ) {
        var result = duesService.getAll();
        return ResponseEntity.ok(result);
    }
}