package com.bcsdlab.internal.team.controller;

import static com.bcsdlab.internal.auth.Authority.ADMIN;
import static com.bcsdlab.internal.auth.Authority.MANAGER;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.team.controller.dto.Request.TeamCreateRequest;
import com.bcsdlab.internal.team.controller.dto.Request.TeamMapCreateRequest;
import com.bcsdlab.internal.team.controller.dto.Request.TeamModifyRequest;
import com.bcsdlab.internal.team.controller.dto.Request.TeamRequest;
import com.bcsdlab.internal.team.controller.dto.Response.TeamMemberResponse;
import com.bcsdlab.internal.team.controller.dto.Response.TeamResponse;
import com.bcsdlab.internal.team.service.TeamMemberService;
import com.bcsdlab.internal.team.service.TeamService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamController implements TeamApi{

    private final TeamService teamService;
    private final TeamMemberService teamMemberService;

    @Override
    @GetMapping
    public ResponseEntity<List<TeamResponse>> getTeam(
        @ModelAttribute TeamRequest teamRequest
    ) {
        List<TeamResponse> teamResponses = teamService.getTeam(teamRequest);
        return ResponseEntity.ok(teamResponses);
    }

    @Override
    @PutMapping
    public ResponseEntity<Void> modifyTeam(
        @Auth(permit = {MANAGER,ADMIN}) Long memberId,
        @RequestBody @Valid TeamModifyRequest teamModifyRequest
    ) {
        teamService.modifyTeam(teamModifyRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(
        @Auth(permit = {MANAGER,ADMIN}) Long memberId,
        @PathVariable Long id
    ) {
        teamService.deleteTeam(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> createTeam(
        @Auth(permit = {MANAGER,ADMIN}) Long memberId,
        @RequestBody TeamCreateRequest teamCreateRequest
    ) {
        teamService.createTeam(teamCreateRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/members")
    public ResponseEntity<List<TeamMemberResponse>> getTeamMember(
        @PageableDefault Pageable pageable
    ) {
        List<TeamMemberResponse> teamMemberResponses = teamMemberService.getTeamMember(pageable);
        return ResponseEntity.ok(teamMemberResponses);
    }

    @Override
    @GetMapping("/members/{teamId}")
    public ResponseEntity<List<TeamMemberResponse>> getTeamMemberByTeamId(
        @PathVariable Long teamId,
        @PageableDefault Pageable pageable
    ) {
        List<TeamMemberResponse> teamMemberResponses = teamMemberService.getTeamMemberByTeamId(teamId, pageable);
        return ResponseEntity.ok(teamMemberResponses);
    }

    @Override
    @PostMapping("/members")
    public ResponseEntity<Void> createTeamMember(
        @Auth(permit = {MANAGER,ADMIN}) Long memberId,
        @RequestBody TeamMapCreateRequest teamMapCreateRequest
    ) {
        teamMemberService.createTeamMember(teamMapCreateRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteTeamMember(
        @Auth(permit = {MANAGER,ADMIN}) Long memberId,
        @PathVariable Long id
    ) {
        teamMemberService.deleteTeamMember(id);
        return ResponseEntity.ok().build();
    }
}
