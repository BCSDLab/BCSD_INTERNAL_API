package com.bcsdlab.internal.global.slack.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bcsdlab.internal.global.slack.model.SlackMessage;

public interface SlackMessageRepository extends JpaRepository<SlackMessage, Long> {

    @Query("SELECT MAX(s.ts) FROM SlackMessage s")
    BigDecimal findMaxTs();
}
