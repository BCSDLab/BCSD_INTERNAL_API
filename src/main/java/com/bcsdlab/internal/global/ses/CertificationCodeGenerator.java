package com.bcsdlab.internal.global.ses;

import java.util.concurrent.ThreadLocalRandom;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CertificationCodeGenerator {

    private static final int CERTIFICATION_CODE_ORIGIN = 0;
    private static final int CERTIFICATION_CODE_BOUND = 1_000_000;

    public static String get() {
        return String.format(
            "%06d",
            ThreadLocalRandom.current()
                .nextInt(CERTIFICATION_CODE_ORIGIN, CERTIFICATION_CODE_BOUND)
        );
    }
}
