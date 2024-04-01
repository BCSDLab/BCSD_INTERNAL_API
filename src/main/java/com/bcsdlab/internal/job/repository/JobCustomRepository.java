package com.bcsdlab.internal.job.repository;

import java.util.List;

import com.bcsdlab.internal.job.Job;

public interface JobCustomRepository {

    List<Job> searchJob(Integer year, Long trackId);

    List<Job> searchJobWithLeader(Long trackId);
}
