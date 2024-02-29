package com.bcsdlab.internal.dues;

import java.time.YearMonth;

import com.bcsdlab.internal.global.RootEntity;
import com.bcsdlab.internal.global.config.YearMonthDateAttributeConverter;
import com.bcsdlab.internal.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
    @Convert(converter = YearMonthDateAttributeConverter.class)
    private YearMonth date;

    @Enumerated(STRING)
    @Column(name = "status", columnDefinition = "varchar(255)")
    private DuesStatus status;

    @Column(name = "is_delete")
    private boolean isDelete;

    public Dues(String memo, Member member, YearMonth date, DuesStatus status, boolean isDelete) {
        this.memo = memo;
        this.member = member;
        this.date = date;
        this.status = checkNone(status);
        this.isDelete = isDelete;
    }

    public void update(DuesStatus status, String memo) {
        this.status = checkNone(status);
        this.memo = memo;
    }

    private DuesStatus checkNone(DuesStatus status) {
        if (status.equals(DuesStatus.NONE)) {
            return null;
        }
        return status;
    }
}
