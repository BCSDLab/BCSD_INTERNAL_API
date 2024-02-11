package com.bcsdlab.internal.track.controller;

import java.util.List;

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
import com.bcsdlab.internal.track.Track;
import com.bcsdlab.internal.track.TrackRepository;
import com.bcsdlab.internal.track.controller.dto.request.TrackCreateRequest;
import com.bcsdlab.internal.track.controller.dto.request.TrackUpdateRequest;
import com.bcsdlab.internal.track.controller.dto.response.TrackResponse;

import static com.bcsdlab.internal.auth.Authority.ADMIN;
import lombok.RequiredArgsConstructor;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping("/tracks")
public class TrackController implements TrackApi {

    private final TrackRepository trackRepository;

    @GetMapping
    public ResponseEntity<List<TrackResponse>> getTrack() {
        var tracks = trackRepository.findAll();
        var result = tracks.stream().map(TrackResponse::from).toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Void> createTrack(
        @Auth(permit = {ADMIN}) Long memberId,
        @RequestBody TrackCreateRequest request
    ) {
        trackRepository.save(new Track(request.name()));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrackResponse> updateTrack(
        @Auth(permit = {ADMIN}) Long memberId,
        @PathVariable Long id,
        @RequestBody TrackUpdateRequest request
    ) {
        Track track = trackRepository.getById(id);
        track.update(request.name());
        return ResponseEntity.ok(TrackResponse.from(track));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(
        @Auth(permit = {ADMIN}) Long memberId,
        @PathVariable Long id
    ) {
        trackRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
