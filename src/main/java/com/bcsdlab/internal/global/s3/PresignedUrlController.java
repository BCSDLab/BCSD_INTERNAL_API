package com.bcsdlab.internal.global.s3;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcsdlab.internal.auth.Auth;

import static com.bcsdlab.internal.auth.Authority.ADMIN;
import static com.bcsdlab.internal.auth.Authority.MANAGER;
import static com.bcsdlab.internal.auth.Authority.NORMAL;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/infra/aws/s3/presigned-url")
public class PresignedUrlController implements PresignedUrlApi {

    private final PresignedUrlClient presignedUrlClient;

    @PostMapping
    public ResponseEntity<CreatePresignedUrlResponse> createPresignedUrl(
        @Auth(permit = {NORMAL, MANAGER, ADMIN}) Long memberId,
        @Valid @RequestBody CreatePresignedUrlRequest request
    ) {
        return ResponseEntity.ok(presignedUrlClient.create(request.fileName()));
    }
}
