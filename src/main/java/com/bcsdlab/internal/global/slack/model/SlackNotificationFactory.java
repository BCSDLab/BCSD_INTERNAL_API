package com.bcsdlab.internal.global.slack.model;

import static com.bcsdlab.internal.global.slack.model.SlackMessageBlockUtils.asBlocks;
import static com.bcsdlab.internal.global.slack.model.SlackMessageBlockUtils.divider;
import static com.bcsdlab.internal.global.slack.model.SlackMessageBlockUtils.markdownText;
import static com.bcsdlab.internal.global.slack.model.SlackMessageBlockUtils.section;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bcsdlab.internal.dues.controller.dto.request.SendSlackMessage;
import com.bcsdlab.internal.member.model.Member;
import com.slack.api.webhook.Payload;

@Component
public class SlackNotificationFactory {

    private final String duesPaymentDocumentLink;
    private final String duesManagementDocumentLink;
    private final String internalDuesLink;
    private final String bankAccount;
    private final String rulesLink;

    public SlackNotificationFactory(
        @Value("${google.docs.dues-payment}") String duesPaymentDocumentLink,
        @Value("${google.docs.dues-management}") String duesManagementDocumentLink,
        @Value("${internal.link.dues}") String internalDuesLink,
        @Value("${internal.link.rules}") String rulesLink,
        @Value("${bank.account}") String bankAccount
    ) {
        this.duesManagementDocumentLink = duesManagementDocumentLink;
        this.duesPaymentDocumentLink = duesPaymentDocumentLink;
        this.internalDuesLink = internalDuesLink;
        this.bankAccount = bankAccount;
        this.rulesLink = rulesLink;
    }

    public Payload generateSlackDuesNotificationByGlobal(
        SendSlackMessage content,
        String presidentSlackId,
        String vicePresidentSlackId
    ) {
        String header = String.format("""
                *%d년도 %d월 회비 정산 내역입니다.*
                """,
            content.year(),
            content.month());

        String center = String.format("""
                • <%s|회비 납부 문서>, <%s|회비 관리 문서>
                • 회비납부계좌: `%s`
                """,
            duesPaymentDocumentLink,
            duesManagementDocumentLink,
            bankAccount);

        String footer = String.format("""
                문의 사항이 있으시다면 <@%s>, <@%s>에게 문의해주시기 바랍니다.
                """,
            presidentSlackId,
            vicePresidentSlackId
        );
        if (content.explanation() == null || content.explanation().isBlank()) {
            return buildPayload(
                header,
                center,
                footer
            );
        } else {
            return buildPayloadContainExplanation(
                header,
                center,
                content.explanation(),
                footer
            );
        }
    }

    public Payload generateSlackDuesNotificationByDM(
        int price,
        int unPaidCount,
        String name,
        String presidentSlackId,
        String vicePresidentSlackId
    ) {
        String header = """
            *회비 미납 안내*
            """;

        String center = String.format("""
                %s님은 %d원(%d회)의 회비를 미납하였습니다.
                아래의 계좌를 통해 회비를 납부해 주시기 바랍니다.
                회비납부계좌: `%s`
                참고: <%s|회비 납부 내역>
                                
                회비 3회 이상 미납 시, 회원 자격이 박탈될 수 있습니다.
                회비 미납으로 인한 제명에 대한 자세한 내용은 회칙의 10조(제명) 1항 내용을 확인해주시기 바랍니다.
                회칙: <%s|BCSDLab 회칙>
                                
                """,
            name,
            price,
            unPaidCount,
            bankAccount,
            internalDuesLink,
            rulesLink
        );

        String footer = String.format("""
                문의 사항이 있으시다면 <@%s>, <@%s>에게 문의해주시기 바랍니다.
                """,
            presidentSlackId,
            vicePresidentSlackId
        );
        return buildPayload(
            header,
            center,
            footer
        );

    }

    private Payload buildPayloadContainExplanation(
        String header,
        String center,
        String explanation,
        String footer
    ) {
        return Payload.builder()
            .blocks(asBlocks(
                section(s -> s.text(markdownText(header))),
                divider(),
                section(s -> s.text(markdownText(center))),
                divider(),
                section(s -> s.text(markdownText(explanation))),
                divider(),
                section(s -> s.text(markdownText(footer)))
            ))
            .build();
    }

    private Payload buildPayload(
        String header,
        String center,
        String footer
    ) {
        return Payload.builder()
            .blocks(asBlocks(
                section(s -> s.text(markdownText(header))),
                divider(),
                section(s -> s.text(markdownText(center))),
                divider(),
                section(s -> s.text(markdownText(footer)))
            ))
            .build();
    }

    public Payload generateSlackCelebrateBirthday(
        List<String> birthdayPeopleSlackId
    ) {
        String header = String.format("""
            🎉 생일을 진심으로 축하드립니다!! 🎉
             """);
        String mentions = birthdayPeopleSlackId.stream()
            .map(id -> String.format("<@%s>", id))
            .collect(Collectors.joining(", "));

        String center = String.format("""
            🎂 오늘은 %s 님의 생일입니다 🎂
            """, mentions);

        String footer = String.format("""
            생일을 맞은 %s 님을 위해서 축하의 말을 남겨주세요!!
            """, mentions
        );
        return buildPayload(
            header,
            center,
            footer
        );
    }
}
