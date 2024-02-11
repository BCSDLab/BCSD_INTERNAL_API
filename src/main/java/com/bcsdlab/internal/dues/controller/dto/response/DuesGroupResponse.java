package com.bcsdlab.internal.dues.controller.dto.response;

import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bcsdlab.internal.dues.Dues;
import com.bcsdlab.internal.dues.DuesStatus;
import com.bcsdlab.internal.member.Member;

import io.swagger.v3.oas.annotations.media.Schema;

public record DuesGroupResponse(
    String year,
    List<DuesResponse> dues
) {

    private record DuesResponse(
        @Schema(example = "1")
        Long memberId,

        @Schema(example = "최준호")
        String name,

        @Schema(example = "BACKEND")
        String track,

        @Schema(example = "3", description = "회비 미납 횟수")
        long unPaidCount,

        @Schema(description = "회비 납입여부 상세정보")
        List<DuesDetailResponse> detail
    ) {

    }

    private record DuesDetailResponse(
        @Schema(example = "1", description = "월")
        int month,

        @Schema(example = "1", description = "회비 납입 고유 ID")
        Long duesId,

        @Schema(example = "NOT_PAID", description = """
            NOT_PAID
            PAID
            SKIP
            """)
        String status,

        @Schema(example = "회장", description = "메모")
        String memo
    ) {

    }

    public static DuesGroupResponse of(String year, List<Member> members, List<Dues> dues) {
        Map<Member, List<Dues>> groupByMember = dues.stream()
            .collect(Collectors.groupingBy(Dues::getMember));

        members.stream()
            .filter(member -> !groupByMember.containsKey(member))
            .forEach(member -> groupByMember.put(member, List.of()));

        var result = groupByMember.entrySet().stream()
            .map(entry -> {
                    Member member = entry.getKey();
                    List<Dues> memberDues = entry.getValue();
                    return new DuesResponse(
                        member.getId(),
                        member.getName(),
                        member.getTrack().name(),
                        getUnPaidCount(memberDues),
                        getDetails(memberDues)
                    );
                }
            ).toList();

        return new DuesGroupResponse(year, result);
    }

    private static long getUnPaidCount(List<Dues> memberDues) {
        return memberDues.stream().filter(it -> it.getStatus() == DuesStatus.NOT_PAID).count();
    }

    private static List<DuesDetailResponse> getDetails(List<Dues> memberDues) {
        return Arrays.stream(Month.values()).map(
            month -> memberDues.stream()
                .filter(dues -> dues.getDate().getMonth() == month)
                .findFirst()
                .map(dues ->
                    new DuesDetailResponse(
                        dues.getDate().getMonthValue(),
                        dues.getId(),
                        dues.getStatus().name(),
                        dues.getMemo()
                    )
                )
                .orElseGet(() ->
                    new DuesDetailResponse(
                        month.getValue(),
                        null,
                        null,
                        null
                    )
                )
        ).toList();
    }
}
