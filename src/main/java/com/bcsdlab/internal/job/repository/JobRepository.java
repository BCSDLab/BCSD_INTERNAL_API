package com.bcsdlab.internal.job.repository;

import static com.bcsdlab.internal.job.exception.JobExceptionType.JOB_NOT_FOUND;

import java.time.YearMonth;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bcsdlab.internal.job.Job;
import com.bcsdlab.internal.job.exception.JobException;

public interface JobRepository extends JpaRepository<Job, Long>, JobCustomRepository {

    Job save(Job job);

    Optional<Job> findById(Long id);

    @Query("SELECT j FROM Job j WHERE j.type = :type AND j.startDate <= :now AND j.endDate >= :now")
    Optional<Job> findActiveJobByType(@Param("type") String type, @Param("now") YearMonth now);

    @Query("""
        SELECT j
        FROM Job j
        JOIN j.member m
        WHERE j.type = '트랙장'
        AND j.startDate <= now()
        AND FUNCTION('MONTH', j.endDate) = FUNCTION('MONTH', now())
        AND j.member.track.id = :trackId
        """)
    Optional<Job> findTrackLeaderByTrackId(@Param("trackId") Long trackId);

    default Job getById(Long id) {
        return findById(id).orElseThrow(() -> new JobException(JOB_NOT_FOUND));
    }

    default Job getActiveJobByType(String type) {
        return findActiveJobByType(type, YearMonth.now()).orElseThrow(() -> new JobException(JOB_NOT_FOUND));
    }
}
