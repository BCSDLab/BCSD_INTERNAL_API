package com.bcsdlab.internal.global.slack.model;

import static lombok.AccessLevel.PROTECTED;

import java.math.BigDecimal;

import com.bcsdlab.internal.global.RootEntity;
import com.bcsdlab.internal.member.model.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "slack_thread")
@NoArgsConstructor(access = PROTECTED)
public class SlackThread extends RootEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ts", nullable = false, precision = 16, scale = 6)
    private BigDecimal ts;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private SlackMessage slackMessage;

    @Builder
    public SlackThread(Long id, BigDecimal ts, String content, Member member, SlackMessage slackMessage) {
        this.id = id;
        this.ts = ts;
        this.content = content;
        this.member = member;
        this.slackMessage = slackMessage;
    }
}
