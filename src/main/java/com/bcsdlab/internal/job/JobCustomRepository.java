package com.bcsdlab.internal.job;

import java.util.List;

public interface JobCustomRepository {

    List<Job> searchJob(Integer year, Long trackId);

    List<Job> searchJobWithLeader(Long trackId);
}
