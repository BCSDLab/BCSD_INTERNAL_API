package com.bcsdlab.internal.dues;

import java.time.YearMonth;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bcsdlab.internal.dues.exception.DuesException;

import static com.bcsdlab.internal.dues.exception.DuesExceptionType.DUES_NOT_FOUND;

public interface DuesRepository extends JpaRepository<Dues, Long>, CustomDuesRepository {

    default Dues getById(Long id) {
        return findById(id).orElseThrow(() -> new DuesException(DUES_NOT_FOUND));
    }

    @Query("SELECT d FROM Dues d WHERE d.date = :date AND d.member.id = :memberId")
    Optional<Dues> findByDateAndMemberId(YearMonth date, Long memberId);
}
