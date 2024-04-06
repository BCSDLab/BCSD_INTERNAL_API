package com.bcsdlab.internal.reservation.repository;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.bcsdlab.internal.reservation.model.QReservation.reservation;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReservationCustomRepositoryImpl implements ReservationCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isExistReservation(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalDateTime adjustedEndDateTime = endDateTime.minusSeconds(1);

        int existCount = queryFactory.select(reservation)
            .from(reservation)
            .where(reservation.startDateTime.between(startDateTime, adjustedEndDateTime)
                .or(reservation.endDateTime.between(startDateTime, adjustedEndDateTime)))
            .fetch()
            .size();
        return existCount > 0;
    }

    @Override
    public boolean isExistReservationNotId(LocalDateTime startDateTime, LocalDateTime endDateTime, Long id) {
        LocalDateTime adjustedEndDateTime = endDateTime.minusSeconds(1);

        int existCount = queryFactory.select(reservation)
            .from(reservation)
            .where((reservation.startDateTime.between(startDateTime, adjustedEndDateTime)
                .or(reservation.endDateTime.between(startDateTime, adjustedEndDateTime))
                .and(reservation.id.ne(id))))
            .fetch()
            .size();
        return existCount > 0;
    }
}
