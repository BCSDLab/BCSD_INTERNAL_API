package com.bcsdlab.internal.global.exception;

import org.springframework.http.HttpStatus;

public interface BcsdExceptionType {

    String MESSAGE_FORMAT = "%s \n  -> detail: %s";

    HttpStatus getHttpStatus();

    String getMessage();

    BcsdExceptionType withDetail(String detailMessage);
}
