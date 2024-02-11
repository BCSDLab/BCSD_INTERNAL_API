package com.bcsdlab.internal.global.exception;

import org.springframework.http.HttpStatus;

public interface BcsdExceptionType {

    HttpStatus getHttpStatus();

    String getMessage();
}
