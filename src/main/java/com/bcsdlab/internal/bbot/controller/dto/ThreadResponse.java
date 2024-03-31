package com.bcsdlab.internal.bbot.controller.dto;

import java.util.List;

import com.bcsdlab.internal.bbot.model.Thread;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record ThreadResponse(
    @Schema(example = "송선권, 최준호, 황현식, 김성재, 박다희", description = "리뷰어")
    List<String> reviewers,

    @Schema(example = "이현수", description = "작성자")
    @Size(max = 255) String writer,

    @Schema(example = "2024-03-31 9:27", description = "생성 일시")
    @Size(max = 255) String ts
) {
    public static ThreadResponse from(List<Thread> threads) {
        return new ThreadResponse(
          threads.stream().map(Thread::getReviewer).toList(),
          threads.get(0).getWriter(),
        threads.get(0).getTs()
        );
    }
}
