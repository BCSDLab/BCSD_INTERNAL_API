package com.bcsdlab.internal.track;

import static com.bcsdlab.internal.track.exception.TrackExceptionType.TRACK_NOT_FOUND;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bcsdlab.internal.track.exception.TrackException;

public interface TrackRepository extends JpaRepository<Track, Long> {

    default Track getById(Long id) {
        return this.findById(id).orElseThrow(() -> new TrackException(TRACK_NOT_FOUND));
    }

    List<Track> findAllByIsDeleted(Boolean isDeleted);

    Optional<Track> findByNameAndIsDeleted(String name, Boolean isDeleted);
}
