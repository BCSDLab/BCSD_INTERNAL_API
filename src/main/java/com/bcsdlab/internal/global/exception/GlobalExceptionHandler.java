package com.bcsdlab.internal.global.exception;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.jboss.logging.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import static com.bcsdlab.internal.global.log.RequestLoggingFilter.REQUEST_ID;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BcsdException.class})
    public ResponseEntity<ErrorResponse> handleBcsdException(HttpServletRequest request, BcsdException e) {
        BcsdExceptionType exceptionType = e.getExceptionType();
        logRequestAndResponse(request, e);
        return ResponseEntity.status(exceptionType.getHttpStatus()).body(new ErrorResponse(exceptionType.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        HttpServletRequest request,
        MethodArgumentNotValidException e
    ) {
        logRequestAndResponse(request, e);
        return ResponseEntity.status(BAD_REQUEST)
            .body(new ErrorResponse(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleRequestException(
        HttpServletRequest request,
        HttpMessageNotReadableException e
    ) {
        logRequestAndResponse(request, e);
        BcsdException cause = (BcsdException) e.getCause().getCause();
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(cause.getExceptionType().getMessage()));
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleJpaException(
        HttpServletRequest request,
        Exception e
    ) {
        logRequestAndResponse(request, e);
        return ResponseEntity.status(BAD_REQUEST)
            .body(new ErrorResponse("제약조건에 위배되는 값입니다."));
    }


    private void logRequestAndResponse(HttpServletRequest request, Exception e) {
        log.error("[{}] 잘못된 요청입니다. uri: {} {}, ",
            MDC.get(REQUEST_ID), request.getMethod(), request.getRequestURI(), e);
        log.info("[{}] request header: {}", MDC.get(REQUEST_ID), getHeaders(request));
        log.info("[{}] request body: {}", MDC.get(REQUEST_ID), getRequestBody(request));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception e) {
        log.error("[{}] 예상하지 못한 예외가 발생했습니다. uri: {} {}, ",
            MDC.get(REQUEST_ID), request.getMethod(), request.getRequestURI(), e);
        log.info("[{}] request header: {}", MDC.get(REQUEST_ID), getHeaders(request));
        log.info("[{}] request body: {}", MDC.get(REQUEST_ID), getRequestBody(request));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("알 수 없는 오류가 발생했습니다."));
    }

    private Map<String, Object> getHeaders(HttpServletRequest request) {
        Map<String, Object> headerMap = new HashMap<>();
        Enumeration<String> headerArray = request.getHeaderNames();

        while (headerArray.hasMoreElements()) {
            String headerName = headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }

        return headerMap;
    }

    private String getRequestBody(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper == null) {
            return " - ";
        } else {
            try {
                wrapper.getInputStream().readAllBytes();
                byte[] buf = wrapper.getContentAsByteArray();
                return buf.length == 0 ? " - " : new String(buf, wrapper.getCharacterEncoding());
            } catch (Exception var4) {
                return " - ";
            }
        }
    }
}
