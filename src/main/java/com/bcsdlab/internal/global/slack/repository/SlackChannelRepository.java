package com.bcsdlab.internal.global.slack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcsdlab.internal.global.slack.model.SlackChannel;

public interface SlackChannelRepository extends JpaRepository<SlackChannel, Long> {

    Optional<SlackChannel> findByChannelId(String id);
}
