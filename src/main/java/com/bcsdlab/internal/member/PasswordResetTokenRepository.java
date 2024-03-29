package com.bcsdlab.internal.member;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.bcsdlab.internal.member.exception.MemberException;
import com.bcsdlab.internal.member.exception.MemberExceptionType;

public interface PasswordResetTokenRepository extends Repository<PasswordResetToken, Long> {

    PasswordResetToken save(PasswordResetToken passwordResetToken);

    void deleteById(Long id);

    Optional<PasswordResetToken> findById(Long id);

    default PasswordResetToken getById(Long id) {
        return findById(id).orElseThrow(
            () -> new MemberException(MemberExceptionType.PASSWORD_RESET_TOKEN_NOT_FOUND));
    }
}
