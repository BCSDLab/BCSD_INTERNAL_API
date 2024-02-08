package com.bcsdlab.internal.global.dto;

import java.util.List;

import org.springframework.data.domain.Page;

public record PageResponse<T>(
    List<T> content,
    boolean hasNext,
    int currentPage,
    int pageSize
) {

    public static <T> PageResponse<T> from(Page<T> result) {
        return new PageResponse<>(
            result.getContent(),
            result.hasNext(),
            result.getNumber(),
            result.getSize()
        );
    }
}
