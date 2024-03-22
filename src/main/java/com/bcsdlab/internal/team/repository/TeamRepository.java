package com.bcsdlab.internal.team.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcsdlab.internal.team.exception.TeamException;
import com.bcsdlab.internal.team.model.Team;

import static com.bcsdlab.internal.team.exception.TeamExceptionType.TEAM_NOT_FOUND;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findAllByIsDeleted(Boolean isDeleted);

    Optional<Team> findByIdAndIsDeleted(Long id, Boolean isDeleted);

    default Team getByIdAndIsDeleted(Long id, Boolean isDeleted) {
        return this.findByIdAndIsDeleted(id, isDeleted).orElseThrow(() -> new TeamException(TEAM_NOT_FOUND));
    }

    default Team getById(Long id) {
        return this.findById(id).orElseThrow(() -> new TeamException(TEAM_NOT_FOUND));
    }
}
