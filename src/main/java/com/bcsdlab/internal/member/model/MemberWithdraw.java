package com.bcsdlab.internal.member.model;

import java.time.LocalDateTime;

import com.bcsdlab.internal.global.RootEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import static lombok.AccessLevel.PROTECTED;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class MemberWithdraw extends RootEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "reason")
    private String reason;

    @Column(name = "withdraw_date", nullable = false)
    private LocalDateTime withdrawDate;

    @Builder
    private MemberWithdraw(Long id, Member member, String reason, LocalDateTime withdrawDate) {
        this.id = id;
        this.member = member;
        this.reason = reason;
        this.withdrawDate = withdrawDate;
    }
}
