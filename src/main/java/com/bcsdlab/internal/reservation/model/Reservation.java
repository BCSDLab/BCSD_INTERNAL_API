package com.bcsdlab.internal.reservation.model;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import com.bcsdlab.internal.global.RootEntity;
import com.bcsdlab.internal.member.model.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "club_room_reservations")
@NoArgsConstructor(access = PROTECTED)
public class Reservation extends RootEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "member_count")
    private Integer memberCount;

    @Column(name = "reason")
    private String reason;

    @Column(name = "detailed_reason")
    private String detailedReason;

    @Column(name = "reservation_start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "reservation_end_date_time")
    private LocalDateTime endDateTime;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Builder
    public Reservation(Long id, Member member, Integer memberCount, String reason, String detailedReason,
        LocalDateTime startDateTime, LocalDateTime endDateTime, Boolean isDeleted) {
        this.id = id;
        this.member = member;
        this.memberCount = memberCount;
        this.reason =reason;
        this.detailedReason = detailedReason;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.isDeleted = isDeleted;
    }
}
