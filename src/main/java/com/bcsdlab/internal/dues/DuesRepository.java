package com.bcsdlab.internal.dues;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcsdlab.internal.dues.exception.DuesException;

import static com.bcsdlab.internal.dues.exception.DuesExceptionType.DUES_NOT_FOUND;

public interface DuesRepository extends JpaRepository<Dues, Long> {

    default Dues getById(Long id) {
        return findById(id).orElseThrow(() -> new DuesException(DUES_NOT_FOUND));
    }
}
