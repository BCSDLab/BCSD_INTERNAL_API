package com.bcsdlab.internal.auth;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bcsdlab.internal.auth.exception.AuthException;
import com.bcsdlab.internal.member.Member;
import com.bcsdlab.internal.member.exception.MemberException;

import static com.bcsdlab.internal.auth.exception.AuthExceptionType.INVALID_TOKEN;
import static com.bcsdlab.internal.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final Long expirationTime;

    public JwtProvider(
        @Value("${jwt.secretKey}") String secretKey,
        @Value("${jwt.expirationTime}") Long expirationTime
    ) {
        String encoded = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(encoded.getBytes());
        this.expirationTime = expirationTime;
    }

    public String createToken(Member member) {
        if (member == null) {
            throw new MemberException(MEMBER_NOT_FOUND);
        } else {
            return Jwts.builder().signWith(secretKey)
                .header()
                .add("typ", "JWT")
                .add("alg", secretKey.getAlgorithm())
                .and()
                .claim("id", member.getId())
                .expiration(new Date(Instant.now().toEpochMilli() + expirationTime)).compact();
        }
    }

    public Long getUserId(String token) {
        try {
            String userId = Jwts.parser().verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id").toString();
            return Long.parseLong(userId);
        } catch (JwtException e) {
            throw new AuthException(INVALID_TOKEN);
        }
    }
}
