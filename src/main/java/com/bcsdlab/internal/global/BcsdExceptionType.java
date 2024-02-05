package com.bcsdlab.internal.global;

import org.springframework.http.HttpStatus;

public interface BcsdExceptionType {

    HttpStatus getHttpStatus();

    String getMessage();
}
