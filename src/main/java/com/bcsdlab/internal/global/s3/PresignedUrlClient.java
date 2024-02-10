package com.bcsdlab.internal.global.s3;

import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
@RequiredArgsConstructor
public class PresignedUrlClient {

    private final S3Presigner.Builder presignerBuilder;
    private final AwsS3Property s3Property;

    /**
     * @param fileName 파일명: example.png
     */
    public CreatePresignedUrlResponse create(String fileName) {
        String generatedFileName = generateFileName(fileName);
        try (S3Presigner presigner = presignerBuilder.build()) {
            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(s3Property.presignedUrlExpiresMinutes()))
                .putObjectRequest(builder -> builder
                    .bucket(s3Property.bucket())
                    .key(s3Property.imagePath() + generatedFileName)
                    .build()
                ).build();
            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            return new CreatePresignedUrlResponse(
                presignedRequest.url().toExternalForm(),
                generatedFileName
            );
        }
    }

    private static String generateFileName(String fileName) {
        String[] split = fileName.split("\\.");
        String fileExt = split[split.length - 1];
        return UUID.randomUUID() + "." + fileExt;
    }
}
