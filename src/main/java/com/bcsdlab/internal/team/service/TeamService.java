package com.bcsdlab.internal.team.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.member.MemberRepository;
import com.bcsdlab.internal.member.model.Member;
import com.bcsdlab.internal.team.controller.dto.Request.TeamCreateRequest;
import com.bcsdlab.internal.team.controller.dto.Request.TeamModifyRequest;
import com.bcsdlab.internal.team.controller.dto.Request.TeamRequest;
import com.bcsdlab.internal.team.controller.dto.Response.TeamResponse;
import com.bcsdlab.internal.team.model.Team;
import com.bcsdlab.internal.team.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    public List<TeamResponse> getTeam(TeamRequest teamRequest) {
        return teamRepository.findAllByIsDeleted(teamRequest.isDeleted()).stream().map(TeamResponse::of).toList();
    }

    @Transactional
    public void modifyTeam(TeamModifyRequest teamModifyRequest) {
        Team team = teamRepository.getById(teamModifyRequest.id());
        Member member = memberRepository.getById(teamModifyRequest.leaderId());
        team.setName(teamModifyRequest.name());
        team.setIsDeleted(teamModifyRequest.isDeleted());
        team.setMember(member);
    }

    @Transactional
    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }

    @Transactional
    public void createTeam(TeamCreateRequest teamCreateRequest) {
        Member member = memberRepository.getById(teamCreateRequest.leaderId());
        Team newTeam = Team.builder()
            .name(teamCreateRequest.name())
            .member(member)
            .isDeleted(false)
            .build();
        teamRepository.save(newTeam);
    }
}
