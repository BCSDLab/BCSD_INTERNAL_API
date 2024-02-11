package com.bcsdlab.internal.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {

    Page<Member> searchMembers(String name, Long trackId, Boolean deleted, Boolean authed, Pageable pageable);
}
