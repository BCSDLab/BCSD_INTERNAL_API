package com.bcsdlab.internal.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcsdlab.internal.member.exception.MemberException;

import static com.bcsdlab.internal.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    Optional<Member> findByStudentNumber(String studentNumber);

    Optional<Member> findByEmail(String email);

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
