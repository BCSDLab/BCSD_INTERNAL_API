package com.bcsdlab.internal.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcsdlab.internal.member.exception.MemberException;
import com.bcsdlab.internal.member.exception.MemberExceptionType;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByStudentNumber(String studentNumber);

    default Member getById(Long id) {
        return findById(id).orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));
    }

    default Member getByStudentNumber(String studentNumber) {
        return findByStudentNumber(studentNumber)
            .orElseThrow(() -> new MemberException(MemberExceptionType.MEMBER_NOT_FOUND));
    }
}
