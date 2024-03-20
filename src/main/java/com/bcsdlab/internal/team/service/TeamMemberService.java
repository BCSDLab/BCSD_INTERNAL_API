package com.bcsdlab.internal.team.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.member.MemberRepository;
import com.bcsdlab.internal.member.model.Member;
import com.bcsdlab.internal.team.controller.dto.Request.TeamMapCreateRequest;
import com.bcsdlab.internal.team.controller.dto.Response.TeamMemberResponse;
import com.bcsdlab.internal.team.model.Team;
import com.bcsdlab.internal.team.model.TeamMap;
import com.bcsdlab.internal.team.repository.TeamMemberRepository;
import com.bcsdlab.internal.team.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    public List<TeamMemberResponse> getTeamMember(Pageable pageable) {
        return teamMemberRepository.findAllByNotDeleted(pageable).stream().map(TeamMemberResponse::of).toList();
    }

    @Transactional
    public void createTeamMember(TeamMapCreateRequest teamMapCreateRequest) {
        Team team = teamRepository.getByIdAndIsDeleted(teamMapCreateRequest.teamId(), false);
        Member member = memberRepository.getById(teamMapCreateRequest.memberId());
        TeamMap newTeamMap = TeamMap.builder()
            .team(team)
            .member(member)
            .build();
        teamMemberRepository.save(newTeamMap);
    }

    @Transactional
    public void deleteTeamMember(Long id) {
        teamMemberRepository.deleteById(id);
    }

    public List<TeamMemberResponse> getTeamMemberByTeamId(Long teamId, Pageable pageable) {
        return teamMemberRepository.findAllByTeamIdAndNotDeleted(teamId, pageable).stream().map(TeamMemberResponse::of).toList();
    }
}
