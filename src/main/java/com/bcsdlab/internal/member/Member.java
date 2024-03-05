package com.bcsdlab.internal.member;

import static com.bcsdlab.internal.auth.Authority.NORMAL;
import static com.bcsdlab.internal.member.exception.MemberExceptionType.INVALID_LOGIN;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.YearMonth;

import org.hibernate.annotations.SQLDelete;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bcsdlab.internal.auth.Authority;
import com.bcsdlab.internal.global.RootEntity;
import com.bcsdlab.internal.global.config.YearMonthDateAttributeConverter;
import com.bcsdlab.internal.member.exception.MemberException;
import com.bcsdlab.internal.track.Track;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(name = "join_date", columnDefinition = "date")
    @Convert(converter = YearMonthDateAttributeConverter.class)
    private YearMonth joinDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "track_id")
    private Track track;

    @Enumerated(STRING)
    @Column(name = "member_type", columnDefinition = "varchar(255)")
    private MemberType memberType;

    @Enumerated(STRING)
    @Column(name = "status", columnDefinition = "varchar(255)")
    private MemberStatus status;

    @Column(name = "name")
    private String name;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

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
    @Column(name = "authority", columnDefinition = "varchar(255)")
    private Authority authority;

    @Column(name = "github_name")
    private String githubName;

    @Column(name = "is_authed")
    private boolean isAuthed;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public Member(
        YearMonth joinDate,
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
        String githubName,
        String profileImageUrl,
        boolean isAuthed,
        boolean isDeleted
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
        this.profileImageUrl = profileImageUrl;
        this.isAuthed = isAuthed;
        this.isDeleted = isDeleted;
    }

    public void register(String studentNumber, String password, PasswordEncoder passwordEncoder) {
        this.studentNumber = studentNumber;
        this.password = passwordEncoder.encode(password);
        this.authority = NORMAL;
        this.isAuthed = false;
        this.isDeleted = false;
    }

    public void matchPassword(String password, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new MemberException(INVALID_LOGIN);
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
        this.profileImageUrl = updated.profileImageUrl;
    }

    public void updateAll(Member updated) {
        this.update(updated);
        this.isAuthed = updated.isAuthed;
        this.isDeleted = updated.isDeleted;
    }

    public void withdraw() {
        this.isDeleted = true;
    }

    public void accept() {
        this.isAuthed = true;
    }

    public void resetPassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }
}
