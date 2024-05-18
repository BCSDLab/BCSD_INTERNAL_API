package com.bcsdlab.internal.global.slack.model;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.SectionBlock.SectionBlockBuilder;
import com.slack.api.model.block.composition.MarkdownTextObject;

import lombok.Getter;

@Getter
public class SlackMessageBlockUtils {

    public static List<LayoutBlock> asBlocks(LayoutBlock... blocks) {
        return Arrays.asList(blocks);
    }

    public static SectionBlock section(Consumer<SectionBlockBuilder> consumer) {
        SectionBlockBuilder builder = SectionBlock.builder();
        consumer.accept(builder);
        return builder.build();
    }

    public static DividerBlock divider() {
        return DividerBlock.builder().build();
    }

    public static MarkdownTextObject markdownText(String text) {
        return MarkdownTextObject.builder().text(text).build();
    }
}
