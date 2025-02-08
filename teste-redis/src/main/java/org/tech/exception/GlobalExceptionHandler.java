package org.tech.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.error("发生错误: ", e);
        Map<String, Object> response = new HashMap<>();
        response.put("message", e.getMessage());
        response.put("type", e.getClass().getSimpleName());
        return ResponseEntity.internalServerError().body(response);
    }
} 