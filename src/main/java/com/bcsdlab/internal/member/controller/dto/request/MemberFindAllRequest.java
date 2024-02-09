package com.bcsdlab.internal.member.controller.dto.request;

import org.springframework.web.bind.annotation.RequestParam;

public record MemberFindAllRequest(
    @RequestParam(defaultValue = "_") String name,
    @RequestParam(defaultValue = "false") Boolean deleted,
    @RequestParam(defaultValue = "true") Boolean authorized
) {

}
