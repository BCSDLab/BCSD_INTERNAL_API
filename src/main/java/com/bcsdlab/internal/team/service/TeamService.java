package com.bcsdlab.internal.team.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.team.controller.dto.request.TeamCreateRequest;
import com.bcsdlab.internal.team.controller.dto.request.TeamModifyRequest;
import com.bcsdlab.internal.team.controller.dto.request.TeamRequest;
import com.bcsdlab.internal.team.controller.dto.response.TeamResponse;
import com.bcsdlab.internal.team.model.Team;
import com.bcsdlab.internal.team.model.TeamMap;
import com.bcsdlab.internal.team.repository.TeamMapRepository;
import com.bcsdlab.internal.team.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapRepository teamMapRepository;

    public List<TeamResponse> getTeam(TeamRequest teamRequest) {
        List<Team> teams = teamRepository.findAllByIsDeleted(teamRequest.isDeleted());
        List<TeamResponse> teamResponses = new ArrayList<>();
        for (Team team : teams) {
            List<TeamMap> teamMaps = teamMapRepository.findAllByTeamIdAndIsLeader(
                team.getId(),
                true
            );
            teamResponses.add(TeamResponse.of(team, teamMaps));
        }
        return teamResponses;
    }

    @Transactional
    public void modifyTeam(TeamModifyRequest teamModifyRequest) {
        Team team = teamRepository.getById(teamModifyRequest.id());
        team.setName(teamModifyRequest.name());
        team.setIsDeleted(teamModifyRequest.isDeleted());
    }

    @Transactional
    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }

    @Transactional
    public void createTeam(TeamCreateRequest teamCreateRequest) {
        Team newTeam = Team.builder()
            .name(teamCreateRequest.name())
            .isDeleted(false)
            .build();
        teamRepository.save(newTeam);
    }
}
