package com.bcsdlab.internal.member;

import java.time.LocalDate;

import org.hibernate.annotations.SQLDelete;

import com.bcsdlab.internal.auth.Authority;
import com.bcsdlab.internal.auth.PasswordEncoder;
import com.bcsdlab.internal.global.RootEntity;
import com.bcsdlab.internal.member.exception.MemberException;

import static com.bcsdlab.internal.auth.Authority.NORMAL;
import static com.bcsdlab.internal.member.exception.MemberExceptionType.INVALID_LOGIN;
import static com.bcsdlab.internal.member.exception.MemberExceptionType.MEMBER_NOT_AUTHORIZED;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import static jakarta.persistence.EnumType.STRING;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.persistence.Id;
import static lombok.AccessLevel.PROTECTED;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE id = ?")
public class Member extends RootEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Enumerated(STRING)
    @Column(name = "track")
    private Track track;

    @Enumerated(STRING)
    @Column(name = "member_type")
    private MemberType memberType;

    @Enumerated(STRING)
    @Column(name = "status")
    private MemberStatus status;

    @Column(name = "name")
    private String name;

    @Column(name = "company")
    private String company;

    @Column(name = "department")
    private String department;

    @Column(name = "student_number", unique = true)
    private String studentNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(STRING)
    @Column(name = "authority")
    private Authority authority;

    @Column(name = "github_name")
    private String githubName;

    @Column(name = "is_authorized")
    private boolean isAuthed;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public Member(
        LocalDate joinDate,
        Track track,
        MemberType memberType,
        MemberStatus status,
        String name,
        String company,
        String department,
        String studentNumber,
        String phoneNumber,
        String email,
        String password,
        String githubName
    ) {
        this.joinDate = joinDate;
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
        this.isAuthed = false;
        this.isDeleted = false;
    }

    public void matchPassword(String password, PasswordEncoder passwordEncoder) {
        if (passwordEncoder.match(password, this.password)) {
            throw new MemberException(INVALID_LOGIN);
        }
    }

    public void checkAuthorized() {
        if (!isAuthed) {
            throw new MemberException(MEMBER_NOT_AUTHORIZED);
        }
    }

    public void update(Member updated) {
        this.joinDate = updated.joinDate;
        this.track = updated.track;
        this.memberType = updated.memberType;
        this.status = updated.status;
        this.name = updated.name;
        this.company = updated.company;
        this.department = updated.department;
        this.studentNumber = updated.studentNumber;
        this.phoneNumber = updated.phoneNumber;
        this.email = updated.email;
        this.githubName = updated.githubName;
    }

    public void withdraw() {
        this.isDeleted = true;
    }

    public void accept() {
        this.isAuthed = true;
    }
}
