package com.bcsdlab.internal.bbot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcsdlab.internal.bbot.model.Thread;

public interface ThreadRepository extends JpaRepository<Thread, Long> {

    List<Thread> findAllByPrLink(String pullRequestLink);
}
