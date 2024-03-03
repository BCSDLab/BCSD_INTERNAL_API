package com.bcsdlab.internal.job;

import static com.bcsdlab.internal.job.QJob.job;
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JobCustomRepositoryImpl implements JobCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Job> searchJob(Integer year, Long trackId) {
        return queryFactory.selectFrom(job)
            .where(
                containYear(year),
                eqTrackId(trackId)
            )
            .fetch();
    }

    @Override
    public List<Job> searchJobWithLeader(Long trackId) {
        return queryFactory.selectFrom(job)
            .join(job.member).fetchJoin()
            .where(
                isNow(),
                eqTrackId(trackId)
            )
            .fetch();
    }

    private BooleanExpression isNow() {
        return job.startDate.loe(YearMonth.now())
            .and(job.endDate.goe(YearMonth.now()));
    }

    private BooleanExpression eqTrackId(Long trackId) {
        if (trackId == null) {
            return null;
        }
        return job.member.track.id.eq(trackId);
    }

    private BooleanExpression containYear(Integer year) {
        if (year == null) {
            return null;
        }
        YearMonth start = YearMonth.of(year, JANUARY);
        YearMonth end = YearMonth.of(year, DECEMBER);

        return job.startDate.between(start, end)
            .or(job.endDate.between(start, end))
            .or(job.startDate.loe(start).and(job.endDate.goe(end)));
    }
}
