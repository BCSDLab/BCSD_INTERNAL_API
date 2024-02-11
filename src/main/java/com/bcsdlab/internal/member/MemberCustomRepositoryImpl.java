package com.bcsdlab.internal.member;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.bcsdlab.internal.member.QMember.member;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Member> searchMembers(String name, Long trackId, Boolean deleted, Boolean authed, Pageable pageable) {
        List<Member> result = jpaQueryFactory.selectFrom(member)
            .where(
                containName(name),
                eqTrackId(trackId),
                inDeleted(deleted),
                isAuthed(authed)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        return new PageImpl<>(result, pageable, countMembers(name, trackId, deleted, authed));
    }

    private Long countMembers(String name, Long trackId, Boolean deleted, Boolean authed) {
        return jpaQueryFactory
            .select(member.count())
            .from(member)
            .where(
                containName(name),
                eqTrackId(trackId),
                inDeleted(deleted),
                isAuthed(authed)
            )
            .fetchOne();
    }

    private BooleanExpression inDeleted(Boolean deleted) {
        return deleted == null ? null : member.isDeleted.eq(deleted);
    }

    private BooleanExpression isAuthed(Boolean authed) {
        return authed == null ? null : member.isAuthed.eq(authed);
    }

    private BooleanExpression containName(String name) {
        return name == null ? null : member.name.contains(name);
    }

    private BooleanExpression eqTrackId(Long trackId) {
        return trackId == null ? null : member.track.id.eq(trackId);
    }
}
