package com.bcsdlab.internal.dues;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.bcsdlab.internal.dues.QDues.dues;
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomDuesRepositoryImpl implements CustomDuesRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Dues> searchDues(Integer year, Long trackId) {
        return jpaQueryFactory.selectFrom(dues)
            .where(
                containYear(year),
                eqTrackId(trackId)
            )
            .fetch();
    }

    private BooleanExpression eqTrackId(Long trackId) {
        return trackId == null ? null : dues.member.track.id.eq(trackId);
    }

    private BooleanExpression containYear(Integer year) {
        if (year == null) {
            return null;
        }
        YearMonth start = YearMonth.of(year, JANUARY);
        YearMonth end = YearMonth.of(year, DECEMBER);
        return dues.date.between(start, end);
    }
}
