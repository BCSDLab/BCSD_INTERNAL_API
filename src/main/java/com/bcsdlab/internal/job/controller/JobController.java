package com.bcsdlab.internal.job.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.job.controller.dto.request.JobCreateQueryRequest;
import com.bcsdlab.internal.job.controller.dto.request.JobDeleteQueryRequest;
import com.bcsdlab.internal.job.controller.dto.request.JobQueryRequest;
import com.bcsdlab.internal.job.controller.dto.request.JobUpdateQueryRequest;
import com.bcsdlab.internal.job.controller.dto.response.JobGroupResponse;
import com.bcsdlab.internal.job.controller.dto.response.JobResponse;
import com.bcsdlab.internal.job.service.JobService;

import static com.bcsdlab.internal.auth.Authority.ADMIN;
import static com.bcsdlab.internal.auth.Authority.MANAGER;
import static com.bcsdlab.internal.auth.Authority.NORMAL;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JobController implements JobApi {

    private final JobService jobService;

    @Override
    public ResponseEntity<JobGroupResponse> getAll(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId,
        @ParameterObject
        @ModelAttribute JobQueryRequest request
    ) {
        var result = jobService.getAll(request);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<JobResponse> createJob(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,
        @ParameterObject
        @ModelAttribute JobCreateQueryRequest request
    ) {
        var result = jobService.create(request);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<JobResponse> updateJob(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,
        @ParameterObject
        @ModelAttribute JobUpdateQueryRequest request
    ) {
        var result = jobService.update(request);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> deleteJob(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,
        @ParameterObject
        @ModelAttribute JobDeleteQueryRequest request
    ) {
        jobService.delete(request);
        return ResponseEntity.noContent().build();
    }
}
