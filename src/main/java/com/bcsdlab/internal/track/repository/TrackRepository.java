package com.bcsdlab.internal.track.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcsdlab.internal.track.Track;
import com.bcsdlab.internal.track.exception.TrackException;

import static com.bcsdlab.internal.track.exception.TrackExceptionType.TRACK_NOT_FOUND;

public interface TrackRepository extends JpaRepository<Track, Long> {

    default Track getById(Long id) {
        return this.findById(id).orElseThrow(() -> new TrackException(TRACK_NOT_FOUND));
    }

    List<Track> findAllByIsDeleted(Boolean isDeleted);

    Optional<Track> findByNameAndIsDeleted(String name, Boolean isDeleted);

    Optional<Track> findByName(String name);

    default Track getByName(String name) {
        return findByName(name).orElseThrow(() -> new TrackException(TRACK_NOT_FOUND));
    }
}
