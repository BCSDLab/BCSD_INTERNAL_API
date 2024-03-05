package com.bcsdlab.internal.member;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@RedisHash("passwordResetToken")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordResetToken {

    private static final long REFRESH_TOKEN_EXPIRE_HOUR = 2L;

    @Id
    private Long id;

    private String certificationCode;

    @TimeToLive(unit = TimeUnit.HOURS)
    private Long expiration;

    private PasswordResetToken(Long id, String certificationCode, Long expiration) {
        this.id = id;
        this.certificationCode = certificationCode;
        this.expiration = expiration;
    }

    public static PasswordResetToken create(Long userId, String certificationCode) {
        return new PasswordResetToken(userId, certificationCode, REFRESH_TOKEN_EXPIRE_HOUR);
    }
}
