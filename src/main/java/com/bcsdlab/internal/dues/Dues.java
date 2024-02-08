package com.bcsdlab.internal.dues;

import java.time.LocalDateTime;

import com.bcsdlab.internal.global.RootEntity;
import com.bcsdlab.internal.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import static jakarta.persistence.EnumType.STRING;
import jakarta.persistence.Enumerated;
import static jakarta.persistence.FetchType.LAZY;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import static lombok.AccessLevel.PROTECTED;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Dues extends RootEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "memo")
    private String memo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "date")
    private LocalDateTime date;

    @Enumerated(STRING)
    @Column(name = "status")
    private DuesStatus status;

    @Column(name = "is_delete")
    private boolean isDelete;

    public void update(String memo, DuesStatus status) {
        this.memo = memo;
        this.status = status;
    }
}
