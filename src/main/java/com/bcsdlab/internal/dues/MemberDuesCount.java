package com.bcsdlab.internal.dues;

import com.bcsdlab.internal.member.model.Member;

import lombok.Getter;

@Getter
public class MemberDuesCount {

    private final Member member;
    private final long count;

    public MemberDuesCount(Member member, long count) {
        this.member = member;
        this.count = count;
    }
}
