package com.bcsdlab.internal.job.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.job.Job;
import com.bcsdlab.internal.job.JobRepository;
import com.bcsdlab.internal.job.controller.dto.request.JobCreateQueryRequest;
import com.bcsdlab.internal.job.controller.dto.request.JobDeleteQueryRequest;
import com.bcsdlab.internal.job.controller.dto.request.JobQueryRequest;
import com.bcsdlab.internal.job.controller.dto.request.JobUpdateQueryRequest;
import com.bcsdlab.internal.job.controller.dto.response.JobGroupResponse;
import com.bcsdlab.internal.job.controller.dto.response.JobResponse;
import com.bcsdlab.internal.member.Member;
import com.bcsdlab.internal.member.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobService {

    private final JobRepository jobRepository;
    private final MemberRepository memberRepository;

    public JobGroupResponse getAll(JobQueryRequest request) {
        List<Job> jobs = jobRepository.searchJob(request.year(), request.trackId());
        return JobGroupResponse.of(request.year(), jobs);
    }

    public JobResponse create(JobCreateQueryRequest request) {
        Member member = memberRepository.getById(request.memberId());
        Job savedJob = jobRepository.save(request.toEntity(member));
        return JobResponse.from(savedJob);
    }

    @Transactional
    public JobResponse update(JobUpdateQueryRequest request) {
        Member member = memberRepository.getById(request.memberId());
        Job job = jobRepository.getById(request.id());
        job.update(request.toEntity(member));
        return JobResponse.from(job);
    }

    @Transactional
    public void delete(JobDeleteQueryRequest request) {
        Job foundJob = jobRepository.getById(request.id());
        jobRepository.delete(foundJob);
    }
}
