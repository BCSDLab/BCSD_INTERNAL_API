package com.bcsdlab.internal.track.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcsdlab.internal.auth.Auth;
import com.bcsdlab.internal.job.JobRepository;
import com.bcsdlab.internal.member.MemberRepository;
import com.bcsdlab.internal.member.model.Member;
import com.bcsdlab.internal.track.Track;
import com.bcsdlab.internal.track.TrackRepository;
import com.bcsdlab.internal.track.controller.dto.request.TrackCreateRequest;
import com.bcsdlab.internal.track.controller.dto.request.TrackUpdateRequest;
import com.bcsdlab.internal.track.controller.dto.response.TrackResponse;
import com.bcsdlab.internal.track.controller.dto.response.TrackWithLeaderResponse;

import static com.bcsdlab.internal.auth.Authority.ADMIN;
import static com.bcsdlab.internal.auth.Authority.MANAGER;
import lombok.RequiredArgsConstructor;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/tracks")
public class TrackController implements TrackApi {

    private final TrackRepository trackRepository;
    private final JobRepository jobRepository;
    private final MemberRepository memberRepository;

    @GetMapping
    public ResponseEntity<List<TrackWithLeaderResponse>> getTrack() {
        List<TrackWithLeaderResponse> result = new ArrayList<>();
        var tracks = trackRepository.findAllByIsDeleted(false);
        for (long trackId = 1; trackId <= tracks.size(); trackId++) {
            Optional<Member> trackLeader = jobRepository.searchJobWithLeader(trackId)
                .stream()
                .map(job -> memberRepository.getById(job.getMember().getId()))
                .findFirst();

            if (trackLeader.isEmpty()) {
                result.add(TrackWithLeaderResponse.of(tracks.get((int) trackId - 1), null));
            } else {
                result.add(TrackWithLeaderResponse.of(tracks.get((int) trackId - 1), trackLeader.get()));
            }
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Void> createTrack(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,
        @RequestBody TrackCreateRequest request
    ) {
        Optional<Track> deletedTrack = trackRepository.findByNameAndIsDeleted(request.name(), true);
        if (deletedTrack.isPresent()) {
            deletedTrack.get().undelete();
        } else {
            trackRepository.save(new Track(request.name()));
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrackResponse> updateTrack(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,
        @PathVariable Long id,
        @RequestBody TrackUpdateRequest request
    ) {
        Track track = trackRepository.getById(id);
        track.update(request.name());
        return ResponseEntity.ok(TrackResponse.from(track));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(
        @Auth(permit = {MANAGER, ADMIN}) Long memberId,
        @PathVariable Long id
    ) {
        List<Member> members = memberRepository.findAllByTrackId(id);
        members.forEach(Member::deleteTrack);
        trackRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
