package com.bcsdlab.internal.global.slack.model;

import static lombok.AccessLevel.PROTECTED;

import com.bcsdlab.internal.global.RootEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "slack_channel")
@NoArgsConstructor(access = PROTECTED)
public class SlackChannel extends RootEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "channel_id", nullable = false, unique = true, length = 10)
    private String channelId;

    @Column(name = "channel_name")
    private String channelName;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Builder
    private SlackChannel(Long id, String channelId, String channelName, Boolean isPublic) {
        this.id = id;
        this.channelId = channelId;
        this.channelName = channelName;
        this.isPublic = isPublic;
    }
}
