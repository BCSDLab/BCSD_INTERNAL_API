package com.bcsdlab.internal.job;

import static com.bcsdlab.internal.job.exception.JobExceptionType.JOB_NOT_FOUND;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcsdlab.internal.job.exception.JobException;

public interface JobRepository extends JpaRepository<Job, Long>, JobCustomRepository {

    Job save(Job job);

    Optional<Job> findById(Long id);

    default Job getById(Long id) {
        return findById(id).orElseThrow(() -> new JobException(JOB_NOT_FOUND));
    }
}
