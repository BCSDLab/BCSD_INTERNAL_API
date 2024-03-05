package com.bcsdlab.internal.member;

import static com.bcsdlab.internal.member.exception.MemberExceptionType.EMAIL_NOT_FOUND;
import static com.bcsdlab.internal.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcsdlab.internal.member.exception.MemberException;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    Optional<Member> findByStudentNumber(String studentNumber);

    Optional<Member> findByEmail(String email);

    default Member getByEmail(String email) {
        return findByEmail(email)
            .orElseThrow(() -> new MemberException(EMAIL_NOT_FOUND));
    }

    default Member getById(Long id) {
        return findById(id)
            .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    default Member getByStudentNumber(String studentNumber) {
        return findByStudentNumber(studentNumber)
            .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    List<Member> findAllByIsDeletedFalse();
}
