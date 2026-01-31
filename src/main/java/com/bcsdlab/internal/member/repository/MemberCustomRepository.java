package com.bcsdlab.internal.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bcsdlab.internal.member.model.Member;

public interface MemberCustomRepository {

    Page<Member> searchMembers(String name, Long trackId, Boolean deleted, Boolean authed, Boolean isActive, Pageable pageable);
}
