package com.bcsdlab.internal.dues.controller.dto.request;

public record SendSlackMessage(
    int year,
    int month,
    String explanation
) {
}

