package com.bcsdlab.internal.team.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bcsdlab.internal.member.MemberRepository;
import com.bcsdlab.internal.member.model.Member;
import com.bcsdlab.internal.team.controller.dto.request.TeamMapCreateRequest;
import com.bcsdlab.internal.team.controller.dto.response.TeamMemberResponse;
import com.bcsdlab.internal.team.model.Team;
import com.bcsdlab.internal.team.model.TeamMap;
import com.bcsdlab.internal.team.repository.TeamMapRepository;
import com.bcsdlab.internal.team.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamMemberService {

    private final TeamMapRepository teamMapRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    public List<TeamMemberResponse> getTeamMember(Pageable pageable) {
        return teamMapRepository.findAllByNotDeleted(pageable).stream().map(TeamMemberResponse::of).toList();
    }

    @Transactional
    public void createTeamMember(TeamMapCreateRequest teamMapCreateRequest) {
        Team team = teamRepository.getByIdAndIsDeleted(teamMapCreateRequest.teamId(), false);
        Member member = memberRepository.getById(teamMapCreateRequest.memberId());
        TeamMap newTeamMap = TeamMap.builder()
            .team(team)
            .member(member)
            .isLeader(teamMapCreateRequest.isLeader())
            .build();
        teamMapRepository.save(newTeamMap);
    }

    @Transactional
    public void deleteTeamMember(Long id) {
        teamMapRepository.deleteById(id);
    }

    public List<TeamMemberResponse> getTeamMemberByTeamId(Long teamId, Pageable pageable) {
        return teamMapRepository.findAllByTeamIdAndNotDeleted(teamId, pageable).stream().map(TeamMemberResponse::of)
            .toList();
    }
}
