package com.bcsdlab.internal.admin.controller.dto.request;

import java.time.YearMonth;

import com.bcsdlab.internal.member.Member;
import com.bcsdlab.internal.member.MemberStatus;
import com.bcsdlab.internal.member.MemberType;
import com.bcsdlab.internal.track.Track;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminMemberCreateRequest(
    @Schema(example = "2023", description = "가입 년도")
    @NotNull Integer joinedYear,

    @Schema(example = "3", description = "가입 월")
    @NotNull Integer joinedMonth,

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

    @Schema(example = "asdf1234!", description = "비밀번호 (SHA256으로 암호화)")
    @NotBlank String password,

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

    @Schema(example = "CHOI-JJUNHO", description = "깃허브 이름")
    @NotBlank String githubName,

    @Schema(example = "https://profile-image-url", description = "프로필 이미지 URL")
    String profileImageUrl,

    @Schema(example = "true", description = "인증 여부")
    @NotNull Boolean isAuthed,

    @Schema(example = "false", description = "삭제 여부")
    @NotNull Boolean isDeleted
) {

    public Member toEntity(Track track) {
        return new Member(
            YearMonth.of(joinedYear, joinedMonth),
            track,
            memberType,
            status,
            name,
            company,
            department,
            studentNumber,
            phoneNumber,
            email,
            null,
            githubName,
            profileImageUrl,
            isAuthed,
            isDeleted
        );
    }
}
