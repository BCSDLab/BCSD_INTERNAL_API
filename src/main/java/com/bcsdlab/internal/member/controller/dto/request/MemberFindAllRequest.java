package com.bcsdlab.internal.member.controller.dto.request;

public record MemberFindAllRequest(
    String name,
    String track,
    Boolean deleted,
    Boolean authed
) {

}
