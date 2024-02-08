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

    @Column(name = "year")
    private String year;

    @Enumerated(STRING)
    @Column(name = "track")
    private Track track;

    @Column(name = "member_type")
    private MemberType memberType;

    @Column(name = "status")
    private String status;

    @Column(name = "name")
    private String name;

    @Column(name = "company")
    private String company;

    @Column(name = "department")
    private String department;

    @Column(name = "student_number")
    private String studentNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "authority")
    @Enumerated(STRING)
    private Authority authority;

    @Column(name = "github_name")
    private String githubName;

    @Column(name = "is_authorized")
    private boolean isAuthorized;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public Member(
        String year,
        Track track,
        MemberType memberType,
        String status,
        String name,
        String company,
        String department,
        String studentNumber,
        String phoneNumber,
        String email,
        String password,
        String githubName
    ) {
        this.year = year;
        this.track = track;
        this.memberType = memberType;
        this.status = status;
        this.name = name;
        this.company = company;
        this.department = department;
        this.studentNumber = studentNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.githubName = githubName;
    }

    public void register(String studentNumber, String password, PasswordEncoder passwordEncoder) {
        this.studentNumber = studentNumber;
        this.password = passwordEncoder.encode(password);
        this.authority = NORMAL;
        this.isAuthorized = false;
        this.isDeleted = false;
    }

    public void matchPassword(String password, PasswordEncoder passwordEncoder) {
        if (passwordEncoder.match(password, this.password)) {
            throw new MemberException(INVALID_LOGIN);
        }
    }
}
