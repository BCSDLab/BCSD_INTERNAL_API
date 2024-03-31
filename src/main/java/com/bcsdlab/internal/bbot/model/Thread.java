package com.bcsdlab.internal.bbot.model;

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
@Table(name = "thread")
@NoArgsConstructor(access = PROTECTED)
public class Thread extends RootEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "pr_link")
    private String prLink;

    @Size(max = 255)
    @Column(name = "ts")
    private String ts;

    @Size(max = 255)
    @Column(name = "reviewer")
    private String reviewer;

    @Size(max = 255)
    @Column(name = "writer")
    private String writer;

    public Thread(String prLink, String ts, String reviewer, String writer) {
        this.prLink = prLink;
        this.ts = ts;
        this.reviewer = reviewer;
        this.writer = writer;
    }
}
