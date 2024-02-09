package com.bcsdlab.internal.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {

    Page<Member> searchMembers(String name, Boolean deleted, Boolean authed, Pageable pageable);
}
