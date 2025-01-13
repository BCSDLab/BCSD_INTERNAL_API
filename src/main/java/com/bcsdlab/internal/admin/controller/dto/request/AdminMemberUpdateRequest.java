package com.bcsdlab.internal.admin.controller.dto.request;

import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;

import com.bcsdlab.internal.auth.Authority;
import com.bcsdlab.internal.member.MemberStatus;
import com.bcsdlab.internal.member.MemberType;
import com.bcsdlab.internal.member.model.Member;
import com.bcsdlab.internal.track.Track;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminMemberUpdateRequest(
    @Schema(example = "2023", description = "가입 년도")
    Integer joinedYear,

    @Schema(example = "3", description = "가입 월")
    Integer joinedMonth,

    @Schema(example = "1", description = "트랙 ID")
    @NotNull Long trackId,

    @Schema(example = "REGULAR", description = """
        BEGINNER
        REGULAR
        MENTOR
        """)
    @NotNull MemberType memberType,

    @Schema(example = "ATTEND", description = """
        ATTEND("재학")
        OFF("휴학")
        IPP("현장실습")
        ARMY("군 휴학")
        COMPLETION("수료")
        GRADUATE("졸업")
        """)
    @NotNull MemberStatus status,

    @Schema(example = "최준호", description = "이름")
    @NotBlank String name,

    @Schema(example = "한국기술교육대학교", description = "소속")
    @NotBlank String company,

    @Schema(example = "컴퓨터공학부", description = "학부")
    @NotBlank String department,

    @Schema(example = "2019136135", description = "학번")
    @NotBlank String studentNumber,

    @Schema(example = "010-1234-5678", description = "전화번호")
    @NotBlank String phoneNumber,

    @Schema(example = "example@koreatech.ac.kr", description = "이메일")
    @NotBlank String email,

    @Schema(example = "MANAGER", description = "권한")
    @NotNull Authority authority,

    @Schema(example = "CHOI-JJUNHO", description = "깃허브 이름")
    String githubName,

    @Schema(example = "https://profile-image-url", description = "프로필 이미지 URL")
    String profileImageUrl,

    @Schema(example = "true", description = "인증 여부")
    @NotNull Boolean isAuthed,

    @Schema(example = "false", description = "삭제 여부")
    @NotNull Boolean isDeleted,

    @Column(name = "birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday
) {

    public Member toEntity(Track track, Member origin) {
        return Member.builder()
            .joinDate((joinedYear == null || joinedMonth == null) ? null : YearMonth.of(joinedYear, joinedMonth))
            .track(track)
            .memberType(memberType)
            .status(status)
            .name(name)
            .company(company)
            .department(department)
            .studentNumber(studentNumber)
            .phoneNumber(phoneNumber)
            .email(email)
            .password(null)
            .authority(authority)
            .githubName(githubName)
            .profileImageUrl(profileImageUrl)
            .slackId(origin.getSlackId())
            .isAuthed(isAuthed)
            .isDeleted(isDeleted)
            .birthday(birthday)
            .build();
    }
}
