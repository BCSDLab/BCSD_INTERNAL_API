package com.bcsdlab.internal.track;

import org.hibernate.annotations.SQLDelete;

import com.bcsdlab.internal.global.RootEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.persistence.Id;
import static lombok.AccessLevel.PROTECTED;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE track SET is_deleted = true WHERE id = ?")
public class Track extends RootEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public Track(String name) {
        this.name = name;
    }

    public void update(String name) {
        this.name = name;
    }
}
