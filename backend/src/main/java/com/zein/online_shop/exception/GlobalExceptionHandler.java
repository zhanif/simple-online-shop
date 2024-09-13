package com.zein.online_shop.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    private ResponseEntity<?> generateResponse(Exception e, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.getReasonPhrase());
        body.put("code", status.value());
        body.put("message", e.getMessage());

        log.error(String.format("\n\nClass: %s", e.getClass().getCanonicalName()));

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return generateResponse(e, getStatusFromException(e));
    }

    private final HttpStatus getStatusFromException(Exception e) {
        if (e instanceof EntityNotFoundException) return HttpStatus.NOT_FOUND;
        else if (e instanceof EntityExistsException || e instanceof DataIntegrityViolationException) return HttpStatus.CONFLICT;
        else if (e instanceof NoHandlerFoundException) return HttpStatus.NOT_FOUND;
        else if (e instanceof HttpMessageNotReadableException) return HttpStatus.NOT_ACCEPTABLE;
        else if (e instanceof MethodArgumentNotValidException) return HttpStatus.BAD_REQUEST;
        else if (e instanceof HttpRequestMethodNotSupportedException) return HttpStatus.METHOD_NOT_ALLOWED;

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
