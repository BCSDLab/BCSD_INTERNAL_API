package com.bcsdlab.internal.global.controller.dto;

import java.util.List;

import org.springframework.data.domain.Page;

public record PageResponse<T>(
    List<T> content,
    boolean hasNext,
    int currentPage,
    long totalElements,
    int totalPage,
    int pageSize
) {

    public static <T> PageResponse<T> from(Page<T> result) {
        return new PageResponse<>(
            result.getContent(),
            result.hasNext(),
            result.getNumber(),
            result.getTotalElements(),
            result.getTotalPages(),
            result.getSize()
        );
    }
}
