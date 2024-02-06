package com.bcsdlab.internal.member;

import com.bcsdlab.internal.auth.Authority;
import com.bcsdlab.internal.auth.PasswordEncoder;
import com.bcsdlab.internal.global.RootEntity;
import com.bcsdlab.internal.member.exception.MemberException;

import static com.bcsdlab.internal.auth.Authority.NORMAL;
import static com.bcsdlab.internal.member.exception.MemberExceptionType.INVALID_LOGIN;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import static jakarta.persistence.EnumType.STRING;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import static lombok.AccessLevel.PROTECTED;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member extends RootEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "student_number", nullable = false, unique = true)
    private String studentNumber;

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Enumerated(STRING)
    @Column(name = "authority", nullable = false)
    private Authority authority;

    @Column(name = "is_authorized")
    private boolean isAuthorized;

    @Column(name = "is_delete")
    private boolean isDelete;

    public Member(
        String studentNumber,
        String password,
        String email,
        Authority authority
    ) {
        this.studentNumber = studentNumber;
        this.password = password;
        this.email = email;
        this.authority = authority;
        this.isAuthorized = false;
    }

    public void register(String studentNumber, String password, PasswordEncoder passwordEncoder) {
        this.studentNumber = studentNumber;
        this.password = passwordEncoder.encode(password);
        this.authority = NORMAL;
        this.isAuthorized = false;
    }

    public void matchPassword(String password, PasswordEncoder passwordEncoder) {
        if (passwordEncoder.match(password, this.password)) {
            throw new MemberException(INVALID_LOGIN);
        }
    }
}
