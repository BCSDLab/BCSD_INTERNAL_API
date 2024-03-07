package com.bcsdlab.internal.track;

import static com.bcsdlab.internal.track.exception.TrackExceptionType.TRACK_NOT_FOUND;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcsdlab.internal.track.exception.TrackException;

public interface TrackRepository extends JpaRepository<Track, Long> {

    default Track getById(Long id) {
        return this.findById(id).orElseThrow(() -> new TrackException(TRACK_NOT_FOUND));
    }

    List<Track> findAllByIsDeleted(Boolean isDeleted);
}
