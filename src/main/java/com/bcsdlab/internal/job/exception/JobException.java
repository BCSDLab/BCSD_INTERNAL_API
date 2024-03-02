package com.bcsdlab.internal.job.exception;

import com.bcsdlab.internal.global.exception.BcsdException;

public class JobException extends BcsdException {

    private final JobExceptionType jobExceptionType;

    public JobException(JobExceptionType jobExceptionType) {
        this.jobExceptionType = jobExceptionType;
    }

    public JobExceptionType getExceptionType() {
        return jobExceptionType;
    }
}
