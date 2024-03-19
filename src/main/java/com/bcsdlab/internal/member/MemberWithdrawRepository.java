package com.bcsdlab.internal.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcsdlab.internal.member.model.MemberWithdraw;

public interface MemberWithdrawRepository extends JpaRepository<MemberWithdraw, Long> {

    Optional<MemberWithdraw> findByMemberId(Long memberId);

    void deleteAllByMemberId(Long memberId);
}
