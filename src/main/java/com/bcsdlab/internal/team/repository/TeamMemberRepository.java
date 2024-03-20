package com.bcsdlab.internal.team.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bcsdlab.internal.team.model.TeamMap;

public interface TeamMemberRepository extends JpaRepository<TeamMap, Long> {

    @Query("SELECT tm FROM TeamMap tm WHERE tm.team.id = :teamId AND tm.team.isDeleted = false")
    Page<TeamMap> findAllByTeamIdAndNotDeleted(Long teamId, Pageable pageable);

    @Query("SELECT tm FROM TeamMap tm WHERE tm.team.isDeleted = false")
    Page<TeamMap> findAllByNotDeleted(Pageable pageable);
}
