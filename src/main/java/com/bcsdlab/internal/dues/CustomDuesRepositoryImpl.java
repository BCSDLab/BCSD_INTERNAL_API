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
    public List<Dues> searchDues(String year, String track) {
        return jpaQueryFactory.selectFrom(dues)
            .where(
                containYear(year),
                containTrack(track)
            )
            .fetch();
    }

    private BooleanExpression containTrack(String track) {
        return track == null ? null : dues.member.track.stringValue().equalsIgnoreCase(track);
    }

    private BooleanExpression containYear(String year) {
        if (year == null) {
            return null;
        }
        YearMonth start = YearMonth.of(Integer.parseInt(year), JANUARY);
        YearMonth end = YearMonth.of(Integer.parseInt(year), DECEMBER);
        return dues.date.between(start, end);
    }
}
