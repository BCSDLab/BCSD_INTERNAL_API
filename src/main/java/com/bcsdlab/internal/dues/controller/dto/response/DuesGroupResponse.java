package com.bcsdlab.internal.dues.controller.dto.response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bcsdlab.internal.dues.Dues;
import com.bcsdlab.internal.dues.DuesStatus;
import com.bcsdlab.internal.member.Member;

public record DuesGroupResponse(
    List<DuesResponse> dues
) {

    private record DuesResponse(
        Long memberId,
        String name,
        String track,
        long unPaidCount,
        List<DuesDetailResponse> detail
    ) {

    }

    private record DuesDetailResponse(
        int month,
        String status,
        String memo
    ) {

    }

    public static DuesGroupResponse of(List<Dues> dues) {
        Map<Member, List<Dues>> groupByMember = dues.stream()
            .collect(Collectors.groupingBy(Dues::getMember));
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

        return new DuesGroupResponse(result);
    }

    private static long getUnPaidCount(List<Dues> memberDues) {
        return memberDues.stream().filter(it -> it.getStatus() == DuesStatus.NOT_PAID).count();
    }

    private static List<DuesDetailResponse> getDetails(List<Dues> memberDues) {
        return memberDues.stream()
            .map(dues ->
                new DuesDetailResponse(
                    dues.getDate().getMonth().getValue(),
                    dues.getStatus().name(),
                    dues.getMemo()
                )
            ).toList();
    }
}
