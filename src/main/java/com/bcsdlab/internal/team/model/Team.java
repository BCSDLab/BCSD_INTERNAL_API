package com.bcsdlab.internal.team.model;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.bcsdlab.internal.global.RootEntity;
import com.bcsdlab.internal.member.model.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE team SET is_deleted = true WHERE id = ?")
public class Team extends RootEntity<Long> {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private Member member;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public void setName(String name) {
        this.name = name;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Builder
    public Team(Long id, String name, Member member, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.member = member;
        this.isDeleted = isDeleted;
    }
}
