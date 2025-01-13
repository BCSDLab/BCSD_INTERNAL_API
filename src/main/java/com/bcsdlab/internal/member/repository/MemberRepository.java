package com.bcsdlab.internal.member.repository;

import static com.bcsdlab.internal.member.exception.MemberExceptionType.EMAIL_NOT_FOUND;
import static com.bcsdlab.internal.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bcsdlab.internal.member.MemberStatus;
import com.bcsdlab.internal.member.exception.MemberException;
import com.bcsdlab.internal.member.model.Member;

import io.lettuce.core.dynamic.annotation.Param;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    List<Member> findAllByTrackId(Long id);

    Optional<Member> findByStudentNumber(String studentNumber);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    Optional<Member> findBySlackId(String slackId);

    // 동명이인이 동일 트랙에 존재할 경우 문제 발생
    Optional<Member> findByNameAndTrackIdAndStatusIsNot(String name, Long trackId, String status);

    List<Member> findAllByIsDeletedFalse();

    default Member getByEmail(String email) {
        return findByEmail(email)
            .orElseThrow(() -> new MemberException(EMAIL_NOT_FOUND));
    }

    default Member getById(Long id) {
        return findById(id)
            .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    default Member getBySlackId(String id) {
        return findBySlackId(id)
            .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    default Member getByStudentNumber(String studentNumber) {
        return findByStudentNumber(studentNumber)
            .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    @Query("SELECT m FROM Member m WHERE FUNCTION('MONTH', m.birthday) = :month AND FUNCTION('DAY', m.birthday) = :day")
    List<Member> findAllByBirthday(@Param("month") int month, @Param("day") int day);
}
