package com.bcsdlab.internal.auth;


import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.bcsdlab.internal.auth.exception.AuthException;

import static com.bcsdlab.internal.auth.exception.AuthExceptionType.UNAUTHORIZED;

@Component
@RequestScope
public class AuthContext {

    private Long memberId;

    private boolean unAuthenticated() {
        return memberId == null;
    }

    public Long getMemberId() {
        if (unAuthenticated()) {
            throw new AuthException(UNAUTHORIZED);
        }
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
