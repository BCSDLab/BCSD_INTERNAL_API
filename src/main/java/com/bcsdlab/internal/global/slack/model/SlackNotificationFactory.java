package com.bcsdlab.internal.global.slack.model;

import static com.bcsdlab.internal.global.slack.model.SlackMessageBlockUtils.asBlocks;
import static com.bcsdlab.internal.global.slack.model.SlackMessageBlockUtils.divider;
import static com.bcsdlab.internal.global.slack.model.SlackMessageBlockUtils.markdownText;
import static com.bcsdlab.internal.global.slack.model.SlackMessageBlockUtils.section;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bcsdlab.internal.dues.controller.dto.request.SendSlackMessage;
import com.slack.api.webhook.Payload;

@Component
public class SlackNotificationFactory {

    private final String duesPaymentDocumentLink;
    private final String duesManagementDocumentLink;
    private final String internalDuesLink;

    public SlackNotificationFactory(
        @Value("${google.docs.dues-payment}") String duesPaymentDocumentLink,
        @Value("${google.docs.dues-management}") String duesManagementDocumentLink,
        @Value("${internal.link.dues}") String internalDuesLink
        ) {
        this.duesManagementDocumentLink = duesManagementDocumentLink;
        this.duesPaymentDocumentLink = duesPaymentDocumentLink;
        this.internalDuesLink = internalDuesLink;
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
                • 회비납부계좌: `신한은행 100-031-597757 비씨에스디랩`
                """,
            duesPaymentDocumentLink,
            duesManagementDocumentLink);

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
        String presidentSlackId,
        String vicePresidentSlackId
    ) {
        String header = String.format("""
                *회비 미납 안내*
                """);

        String center = String.format("""
                %d원(%d회)의 회비를 미납하였습니다.
                아래의 계좌를 통해 회비를 납부해 주시기 바랍니다.
                회비납부계좌: `신한은행 100-031-597757 비씨에스디랩`
                참고: <%s|회비 납부 내역>
                """,
            price,
            unPaidCount,
            internalDuesLink);

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
}
