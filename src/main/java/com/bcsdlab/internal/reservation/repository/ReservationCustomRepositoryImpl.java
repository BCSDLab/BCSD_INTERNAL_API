package com.bcsdlab.internal.reservation.repository;

import static com.bcsdlab.internal.reservation.model.QReservation.reservation;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReservationCustomRepositoryImpl implements ReservationCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isExistReservation(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        int existCount = queryFactory.select(reservation)
            .from(reservation)
            .where(reservation.startDateTime.between(startDateTime, endDateTime)
                .or(reservation.endDateTime.between(startDateTime, endDateTime)))
            .fetch()
            .size();
        return existCount > 0;
    }

    @Override
    public boolean isExistReservationNotId(LocalDateTime startDateTime, LocalDateTime endDateTime, Long id) {
        int existCount = queryFactory.select(reservation)
            .from(reservation)
            .where((reservation.startDateTime.between(startDateTime, endDateTime)
                .or(reservation.endDateTime.between(startDateTime, endDateTime))
                .and(reservation.id.ne(id))))
            .fetch()
            .size();
        return existCount > 0;
    }
}
