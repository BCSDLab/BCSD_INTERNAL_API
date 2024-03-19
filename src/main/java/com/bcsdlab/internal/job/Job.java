package com.bcsdlab.internal.job;

import static lombok.AccessLevel.PROTECTED;

import java.time.YearMonth;

import com.bcsdlab.internal.global.RootEntity;
import com.bcsdlab.internal.global.config.YearMonthDateAttributeConverter;
import com.bcsdlab.internal.member.model.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "jobs")
@NoArgsConstructor(access = PROTECTED)
public class Job extends RootEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Size(max = 255)
    @Column(name = "type")
    private String type;

    @Column(name = "start_date")
    @Convert(converter = YearMonthDateAttributeConverter.class)
    private YearMonth startDate;

    @Column(name = "end_date")
    @Convert(converter = YearMonthDateAttributeConverter.class)
    private YearMonth endDate;

    public Job(Member member, String type, YearMonth startDate, YearMonth endDate) {
        this.member = member;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void update(Job updated) {
        this.member = updated.member;
        this.type = updated.type;
        this.startDate = updated.startDate;
        this.endDate = updated.endDate;
    }
}
